import { Signal, computed } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { format } from 'date-fns/format';
import { filter, map } from 'rxjs';

import { FreiwilligDarlehen, GesuchTrancheSlim } from '@dv/shared/model/gesuch';
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

export const createParamsIdSig = (
  idKey: string,
  allRouteParamsSig: Signal<Record<string, string> | undefined>,
) =>
  computed(() => {
    const params = allRouteParamsSig();
    return params ? params[idKey] : undefined;
  });

export function buildGesuchNavItems(
  gesuchId: string | undefined,
  tranchen: Pick<GesuchTrancheSlim, 'id' | 'gueltigAb'>[],
  baseKey = 'shared',
): NavItem[] {
  if (!gesuchId) return [];

  if (tranchen.length > 1) {
    return [
      {
        type: 'menu',
        id: 'gesuch',
        label: { key: `${baseKey}.header.gesuch` },
        icon: 'description',
        children: tranchen.map((tranche, index) => ({
          type: 'link' as const,
          id: tranche.id,
          label: {
            key: `${baseKey}.header.tranche.item`,
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
        label: { key: `${baseKey}.header.gesuch` },
        icon: 'description',
        route: ['/gesuch', gesuchId, 'tranche', tranchen[0].id],
        active: !!gesuchId,
      },
    ];
  }

  return [];
}

export function buildDarlehenMenu(config: {
  darlehen: FreiwilligDarlehen[];
  canCreateDarlehen: boolean;
  fallId: string;
  isDarlehenRoute: boolean;
  createDarlehen: () => void;
}): NavItem {
  const {
    darlehen,
    canCreateDarlehen,
    fallId,
    isDarlehenRoute,
    createDarlehen,
  } = config;

  const darlehenMenuItems: NavItem[] = darlehenCompletedStates.flatMap(
    (status) => {
      const list = darlehen.filter(
        (dar) => dar.status && darlehenStatusMapping[dar.status] === status,
      );
      if (list.length === 0) return [];

      return [
        {
          type: 'separator' as const,
          id: `separator-${status}`,
          label: {
            key: `shared.header.darlehen.complete-states.${status}`,
          },
        },
        ...list.map((dar) => ({
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
    children: canCreateDarlehen
      ? darlehenMenuItems.concat([
          ...(darlehenMenuItems.length > 0
            ? [{ type: 'separator' as const, id: 'separator-create' }]
            : []),
          {
            type: 'action',
            id: 'create-darlehen',
            label: { key: 'shared.header.darlehen.create' },
            icon: 'add',
            action: createDarlehen,
          },
        ])
      : darlehenMenuItems,
    active: isDarlehenRoute,
  };
}
