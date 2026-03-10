import { Signal, computed, effect, inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { format } from 'date-fns/format';
import { filter, map } from 'rxjs';

import { DarlehenStatus, GesuchTrancheSlim } from '@dv/shared/model/gesuch';
import {
  darlehenCompletedStates,
  darlehenStatusMapping,
} from '@dv/shared/model/ui';

import { NavItem } from './navigation-types';

export const createAllRouteParamsSig = (router: Router) =>
  toSignal(
    router.events.pipe(
      filter((event) => event instanceof NavigationEnd),
      map(() => {
        let route: ActivatedRoute | null = router.routerState.root;
        const params: Record<string, string> = {};

        while (route) {
          Object.assign(params, route.snapshot.params);
          route = route.firstChild;
        }

        return params;
      }),
    ),
  );

// --- Shared navigation builder types ---

export interface NavigationParts {
  gesuchNav: NavItem[];
  darlehenMenu: NavItem;
  fallId: string;
}

interface DarlehenItem {
  id: string;
  status?: DarlehenStatus;
  timestampErstellt?: string;
}

interface DarlehenView {
  list: DarlehenItem[];
  canCreateDarlehen: boolean;
}

export interface NavigationEffectConfig {
  /** Signal providing the fallId (source differs per app) */
  fallIdSig: Signal<string | undefined>;
  /** Translation key for the gesuch nav item label */
  gesuchLabelKey?: string;
  /** Assemble the final ordered navigation from the shared building blocks */
  assembleNavItems: (parts: NavigationParts) => NavItem[];
  /** Signals and callbacks bridging the stores (avoids importing data-access from util) */
  stores: {
    fallStore: {
      loadCurrentFall$: () => void;
    };
    darlehenStore: {
      darlehenGsViewSig: Signal<DarlehenView>;
      getAllDarlehenGs$: (config: { fallId: string }) => void;
      createDarlehen$: (config: { fallId: string }) => void;
    };
    gesuchHeaderStore: {
      viewGsSig: Signal<{
        currentTranchen?: GesuchTrancheSlim[] | null;
      }>;
      loadHeaderGs$: (config: { gesuchTrancheId: string }) => void;
    };
    navigationStore: {
      setNavigationItems: (items: NavItem[]) => void;
    };
    permissionStore: {
      rolesMapSig: Signal<Record<string, boolean>>;
    };
  };
}

// --- Pure builder functions ---

export function buildGesuchNavItems(
  gesuchId: string | undefined,
  tranchen: Pick<GesuchTrancheSlim, 'id' | 'gueltigAb'>[],
  labelKey = 'shared.header.gesuch',
): NavItem[] {
  if (!gesuchId) return [];

  if (tranchen.length > 1) {
    return [
      {
        type: 'menu',
        id: 'gesuch',
        label: { key: labelKey },
        icon: 'description',
        children: tranchen.map((tranche, index) => ({
          type: 'link' as const,
          id: tranche.id,
          label: {
            key: 'shared.header.tranche.item',
            context: {
              date: format(tranche.gueltigAb, 'dd.MM.yyyy'),
              index: index + 1,
            },
          },
          route: ['/gesuch', gesuchId, 'tranche', tranche.id],
        })),
      },
    ];
  }

  if (tranchen.length === 1) {
    return [
      {
        type: 'link',
        id: 'gesuch',
        label: { key: labelKey },
        icon: 'description',
        route: ['/gesuch', gesuchId, 'tranche', tranchen[0].id],
        active: !!gesuchId,
      },
    ];
  }

  return [];
}

export function buildDarlehenMenu(config: {
  darlehenView: DarlehenView;
  fallId: string;
  isDarlehenRoute: boolean;
  onCreateDarlehen: () => void;
}): NavItem {
  const { darlehenView, fallId, isDarlehenRoute, onCreateDarlehen } = config;

  const darlehenMenuItems: NavItem[] = darlehenCompletedStates.flatMap(
    (status) => {
      const darlehen = darlehenView.list.filter(
        (dar) => dar.status && darlehenStatusMapping[dar.status] === status,
      );
      if (darlehen.length === 0) return [];

      return [
        {
          type: 'separator' as const,
          id: `separator-${status}`,
          label: {
            key: 'shared.header.darlehen.complete-states.' + status,
          },
        },
        ...darlehen.map((dar) => ({
          type: 'link' as const,
          id: dar.id,
          label: {
            key: 'shared.header.darlehen.item',
            context: {
              date: dar.timestampErstellt
                ? format(dar.timestampErstellt, 'dd.MM.yyyy')
                : '',
            },
          },
          route: ['/darlehen', dar.id, 'fall', fallId],
        })),
      ];
    },
  );

  return {
    type: 'menu',
    icon: 'account_balance',
    id: 'darlehen',
    label: { key: 'shared.header.darlehen' },
    children: darlehenView.canCreateDarlehen
      ? darlehenMenuItems.concat([
          ...(darlehenMenuItems.length > 0
            ? [{ type: 'separator' as const, id: 'separator-create' }]
            : []),
          {
            type: 'action',
            id: 'create-darlehen',
            label: { key: 'shared.header.darlehen.create' },
            icon: 'add',
            action: onCreateDarlehen,
          },
        ])
      : darlehenMenuItems,
    active: isDarlehenRoute,
  };
}

export function filterNavItemsByRoles(
  items: NavItem[],
  rolesMap: Record<string, boolean>,
): NavItem[] {
  return items.filter((item) => {
    if (!item.rolesAllowed || item.rolesAllowed.length === 0) {
      return true;
    }
    return item.rolesAllowed.some((role) => rolesMap[role]);
  });
}

// --- Navigation effect creator ---

/**
 * Creates the standard navigation effects (load fall, load darlehen,
 * build & set nav items, load gesuch header).
 * Must be called in an injection context (component constructor).
 */
export function createNavigationEffect(config: NavigationEffectConfig) {
  const router = inject(Router);
  const allRouteParamsSig = createAllRouteParamsSig(router);
  const { stores } = config;
  const gesuchLabelKey = config.gesuchLabelKey ?? 'shared.header.gesuch';

  const gesuchIdSig = computed(() => allRouteParamsSig()?.['gesuchId']);
  const trancheIdSig = computed(() => allRouteParamsSig()?.['trancheId']);
  const isDarlehenRouteSig = computed(
    () => !!allRouteParamsSig()?.['darlehenId'],
  );

  stores.fallStore.loadCurrentFall$();

  // Load darlehen when fallId becomes available
  effect(() => {
    const fallId = config.fallIdSig();
    if (fallId) {
      stores.darlehenStore.getAllDarlehenGs$({ fallId });
    }
  });

  // Build and set navigation items
  effect(() => {
    const gesuchId = gesuchIdSig();
    const darlehenView = stores.darlehenStore.darlehenGsViewSig();
    const fallId = config.fallIdSig() ?? '';
    const isDarlehenRoute = isDarlehenRouteSig();
    const rolesMap = stores.permissionStore.rolesMapSig();
    const tranchen = stores.gesuchHeaderStore.viewGsSig().currentTranchen ?? [];

    const gesuchNav = buildGesuchNavItems(gesuchId, tranchen, gesuchLabelKey);
    const darlehenMenu = buildDarlehenMenu({
      darlehenView,
      fallId,
      isDarlehenRoute,
      onCreateDarlehen: () => stores.darlehenStore.createDarlehen$({ fallId }),
    });

    const navItems = filterNavItemsByRoles(
      config.assembleNavItems({ gesuchNav, darlehenMenu, fallId }),
      rolesMap,
    );

    stores.navigationStore.setNavigationItems(navItems);
  });

  // Load gesuch header when trancheId changes
  effect(() => {
    const gesuchTrancheId = trancheIdSig();
    if (gesuchTrancheId) {
      stores.gesuchHeaderStore.loadHeaderGs$({ gesuchTrancheId });
    }
  });

  return { allRouteParamsSig };
}
