import type {
  ComponentPortal,
  DomPortal,
  TemplatePortal,
} from '@angular/cdk/portal';
import { QueryParamsHandling, UrlTree } from '@angular/router';

import {
  BenutzerRole,
  SozialdienstBenutzerRole,
} from '@dv/shared/model/benutzer';
import {
  GetFreiwilligDarlehenSbQueryType,
  GetGesucheSBQueryType,
} from '@dv/shared/model/gesuch';

type DashboardQueryType =
  | GetFreiwilligDarlehenSbQueryType
  | GetGesucheSBQueryType;

export type Portal<T = unknown> =
  | TemplatePortal
  | ComponentPortal<T>
  | DomPortal;

export type TranslateLabel = {
  key: string;
  context?: Record<string, string | number>;
};

interface NavItemBase {
  id: string;
  rolesAllowed?: BenutzerRole[] | SozialdienstBenutzerRole[];
  label?: TranslateLabel;
  icon?: string;
  active?: boolean | undefined;
  routerlinkActiveOptions?: { exact: boolean };
  testId?: string;
}

export interface NavItemLink extends NavItemBase {
  type: 'link';
  route: UrlTree | (string | undefined)[];
  queryParams?: Record<string, string>;
}

export interface NavItemAction extends NavItemBase {
  type: 'action';
  action: () => void;
  disabled?: boolean;
}

export interface NavItemGroup extends NavItemBase {
  type: 'menu';
  children: NavItem[];
}

export interface NavItemSeparator extends NavItemBase {
  type: 'separator';
  orientation?: 'horizontal' | 'vertical';
}

export type NavItem =
  | NavItemLink
  | NavItemAction
  | NavItemGroup
  | NavItemSeparator;

export interface TabNavItem {
  name: string; // todo: rename to key or labelKey?
  active: boolean | undefined;
  route: UrlTree | (string | undefined)[];
  roles?: BenutzerRole[] | SozialdienstBenutzerRole[];
  queryParams?: Record<string, string | undefined>;
  queryParamsHandling?: QueryParamsHandling;
  testId?: string;
}

export type GetGesucheSBQueryWithoutBearbeiter = Exclude<
  GetGesucheSBQueryType,
  'ALLE_BEARBEITBAR' | 'MEINE_BEARBEITBAR'
>;

export type GetFreiwilligDarlehenSbQueryWithoutBearbeiter = Exclude<
  GetFreiwilligDarlehenSbQueryType,
  'ALLE_BEARBEITBAR' | 'MEINE_BEARBEITBAR'
>;

export type WorkableQueryParam = 'TRUE' | 'FALSE';
export const workableEnabledTabFilters = [
  'GESUCHE',
  'DARLEHEN',
] as const satisfies readonly FilterTabQueryParam[];
export type WorkableEnabledTabFilters =
  (typeof workableEnabledTabFilters)[number];

export type QueryTypePrefix =
  DashboardQueryType extends `${infer Prefix}_${string}` ? Prefix : never;

export type QueryTypeSuffix<T extends string> =
  T extends `${QueryTypePrefix}_${infer Suffix}` ? Suffix : never;

export type FilterGesucheQueryParam =
  QueryTypeSuffix<GetGesucheSBQueryWithoutBearbeiter>;

export type ToQueryFilterInputGesucheParam =
  QueryTypeSuffix<GetGesucheSBQueryType>;

export type FilterFreiwilligDarlehenQueryParam =
  QueryTypeSuffix<GetFreiwilligDarlehenSbQueryWithoutBearbeiter>;

export type ToQueryFilterInputFreiwilligDarlehenParam =
  QueryTypeSuffix<GetFreiwilligDarlehenSbQueryType>;

export type FilterTabQueryParam =
  | FilterGesucheQueryParam
  | FilterFreiwilligDarlehenQueryParam;

type FilterGesucheScopeSelectableQueryParam = QueryTypeSuffix<
  Extract<GetGesucheSBQueryWithoutBearbeiter, `MEINE_${string}`>
>;

type FilterGesucheAlleOnlyQueryParam = Exclude<
  FilterGesucheQueryParam,
  FilterGesucheScopeSelectableQueryParam
>;
export const filterGesucheAlleOnlyTabFilters: FilterGesucheAlleOnlyQueryParam[] =
  ['JURISTISCHE_ABKLAERUNG', 'ABKLAERUNG_DURCH_RECHSTABTEILUNG'];

const isFilterGesucheAlleOnlyQueryParam = (
  param: FilterGesucheQueryParam,
): param is FilterGesucheAlleOnlyQueryParam => {
  return filterGesucheAlleOnlyTabFilters.includes(
    param as FilterGesucheAlleOnlyQueryParam,
  );
};

// todo: make non optional
type GesucheDashQueryParams =
  | {
      filterTab?: FilterGesucheScopeSelectableQueryParam;
      scope?: QueryTypePrefix;
      workable?: WorkableQueryParam;
    }
  | {
      filterTab?: FilterGesucheAlleOnlyQueryParam;
      scope?: 'ALLE';
      workable?: WorkableQueryParam;
    };

type DarlehenDashQueryParams = {
  filterTab?: FilterFreiwilligDarlehenQueryParam;
  scope?: QueryTypePrefix;
  workable?: WorkableQueryParam;
};

export type DashQueryParams = GesucheDashQueryParams | DarlehenDashQueryParams;

export interface DashboardFilterTabItem extends TabNavItem {
  queryParams?: DashQueryParams;
}

export const isGesuchQueryValid = (
  query: string,
): query is GetGesucheSBQueryType => {
  return Object.values(GetGesucheSBQueryType).includes(
    query as GetGesucheSBQueryType,
  );
};

export const isFreiwilligDarlehenQueryValid = (
  query: string,
): query is GetFreiwilligDarlehenSbQueryType => {
  return Object.values(GetFreiwilligDarlehenSbQueryType).includes(
    query as GetFreiwilligDarlehenSbQueryType,
  );
};

export const getGesuchQueryFromParams = (
  scope: QueryTypePrefix,
  filterTab: FilterGesucheQueryParam,
  workable?: WorkableQueryParam,
): {
  query: GetGesucheSBQueryType;
  scopeControl: { show: boolean; value: boolean };
  workableControl: { show: boolean; value: boolean };
} => {
  if (
    workableEnabledTabFilters.includes(filterTab as WorkableEnabledTabFilters)
  ) {
    if (workable === 'TRUE') {
      const workableQuery = `${scope}_BEARBEITBAR`;
      if (isGesuchQueryValid(workableQuery)) {
        return {
          query: workableQuery,
          scopeControl: { show: true, value: scope === 'MEINE' },
          workableControl: { show: true, value: true },
        };
      }
    } else {
      const nonWorkableQuery = `${scope}_${filterTab}`;
      if (isGesuchQueryValid(nonWorkableQuery)) {
        return {
          query: nonWorkableQuery,
          scopeControl: { show: true, value: scope === 'MEINE' },
          workableControl: { show: true, value: false },
        };
      }
    }
  }

  if (isFilterGesucheAlleOnlyQueryParam(filterTab)) {
    const alleOnlyQuery = `ALLE_${filterTab}`;
    if (isGesuchQueryValid(alleOnlyQuery)) {
      return {
        query: alleOnlyQuery,
        scopeControl: { show: false, value: false },
        workableControl: { show: false, value: false },
      };
    }
  }

  const query = `${scope}_${filterTab}`;

  if (!isGesuchQueryValid(query)) {
    throw new Error(`Invalid query generated from params: ${query}`);
  }

  return {
    query,
    scopeControl: { show: true, value: scope === 'MEINE' },
    workableControl: { show: false, value: false },
  };
};

export const getGesucheQueryFromValues = (
  scopeValue: boolean | undefined,
  workableValue: boolean | undefined,
  // filterTab is just returned for convenicence, but not changed
  filterTab?: FilterGesucheQueryParam,
): GesucheDashQueryParams => {
  if (!filterTab) {
    throw new Error('filterTab is required to determine query params');
  }

  const scope = scopeValue ? 'MEINE' : 'ALLE';
  const workable = workableValue ? 'TRUE' : 'FALSE';

  if (isFilterGesucheAlleOnlyQueryParam(filterTab)) {
    return { scope: 'ALLE', workable: 'FALSE', filterTab };
  }

  if (
    workable === 'TRUE' &&
    !workableEnabledTabFilters.includes(filterTab as WorkableEnabledTabFilters)
  ) {
    return { scope, workable: 'FALSE', filterTab };
  }

  return { scope, workable, filterTab };
};
