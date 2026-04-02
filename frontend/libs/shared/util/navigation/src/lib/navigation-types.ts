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
  route: (string | undefined)[];
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

export type WorkableParam = 'TRUE' | 'FALSE';
export type ScopeParam = 'ALLE' | 'MEINE';
export type DashboardQuery =
  | keyof typeof GetGesucheSBQueryType
  | keyof typeof GetFreiwilligDarlehenSbQueryType;

export type FilterConfig = {
  scope: ScopeParam;
  workable: WorkableParam[];
  filterTab: FilterTabParam[];
};

// prettier-ignore
export const dashboardFilterQueryWithParamsMap: Record<DashboardQuery, FilterConfig> = {
  ALLE_DARLEHEN                         : { scope: 'ALLE',  workable: ['FALSE'],         filterTab: ['DARLEHEN'] },
  MEINE_DARLEHEN                        : { scope: 'MEINE', workable: ['FALSE'],         filterTab: ['DARLEHEN'] },
  ALLE_BEARBEITBAR                      : { scope: 'ALLE',  workable: ['TRUE', 'FALSE'], filterTab: ['GESUCHE', 'DARLEHEN']},
  MEINE_BEARBEITBAR                     : { scope: 'MEINE', workable: ['TRUE', 'FALSE'], filterTab: ['GESUCHE', 'DARLEHEN'] },
  ALLE_GESUCHE                          : { scope: 'ALLE',  workable: ['FALSE'],         filterTab: ['GESUCHE'] },
  MEINE_GESUCHE                         : { scope: 'MEINE', workable: ['FALSE'],         filterTab: ['GESUCHE'] },
  ALLE_PENDENTE_GESUCHE                 : { scope: 'ALLE',  workable: ['FALSE'],         filterTab: ['PENDENTE_GESUCHE'] },
  MEINE_PENDENTE_GESUCHE                : { scope: 'MEINE', workable: ['FALSE'],         filterTab: ['PENDENTE_GESUCHE'] },
  ALLE_JURISTISCHE_ABKLAERUNG           : { scope: 'ALLE',  workable: ['FALSE'],         filterTab: ['JURISTISCHE_ABKLAERUNG'] },
  ALLE_ABKLAERUNG_DURCH_RECHSTABTEILUNG : { scope: 'ALLE',  workable: ['FALSE'],         filterTab: ['ABKLAERUNG_DURCH_RECHSTABTEILUNG'] },
  ALLE_DRUCKBAR_VERFUEGUNGEN            : { scope: 'ALLE',  workable: ['FALSE'],         filterTab: ['DRUCKBAR_VERFUEGUNGEN'] },
  MEINE_DRUCKBAR_VERFUEGUNGEN           : { scope: 'MEINE', workable: ['FALSE'],         filterTab: ['DRUCKBAR_VERFUEGUNGEN'] },
  ALLE_DRUCKBAR_DATENSCHUTZBRIEFE       : { scope: 'ALLE',  workable: ['FALSE'],         filterTab: ['DRUCKBAR_DATENSCHUTZBRIEFE'] },
  MEINE_DRUCKBAR_DATENSCHUTZBRIEFE      : { scope: 'MEINE', workable: ['FALSE'],         filterTab: ['DRUCKBAR_DATENSCHUTZBRIEFE'] },
}

export const dashboardQueries = Object.keys(
  dashboardFilterQueryWithParamsMap,
) as Array<keyof typeof dashboardFilterQueryWithParamsMap>;

export type QueryTypeSuffix<T extends string> =
  T extends `${ScopeParam}_${infer Suffix}` ? Suffix : never;

export type FilterTabParam = QueryTypeSuffix<DashboardQuery>;

export type ControlVisibility = { show: boolean; value: boolean };

export type DashQueryParams = {
  filterTab?: FilterTabParam;
  scope?: ScopeParam;
  workable?: WorkableParam;
};

export interface DashboardFilterTabItem extends TabNavItem {
  queryParams?: DashQueryParams;
}

export const isValidDashboardQuery = (
  query: string,
): query is DashboardQuery => {
  return dashboardQueries.some((q) => q === query);
};

export const isWorkableEnabledTab = (filterTab: FilterTabParam): boolean => {
  return ['GESUCHE', 'DARLEHEN'].includes(filterTab);
};

export const isAlleOnlyTab = (filterTab: FilterTabParam): boolean => {
  return [
    'JURISTISCHE_ABKLAERUNG',
    'ABKLAERUNG_DURCH_RECHSTABTEILUNG',
  ].includes(filterTab);
};

export const extractConfigFromQuery = (query: DashboardQuery): FilterConfig => {
  return dashboardFilterQueryWithParamsMap[query];
};

export const getQueryFromParams = (
  scope: ScopeParam,
  filterTab: FilterTabParam,
  workable: WorkableParam,
): DashboardQuery => {
  let worableVal = workable;

  if (!isWorkableEnabledTab(filterTab)) {
    worableVal = 'FALSE';
  }

  const query = dashboardQueries.find((q) => {
    const config = dashboardFilterQueryWithParamsMap[q];
    return (
      config.scope === scope &&
      config.workable.includes(worableVal) &&
      config.filterTab.includes(filterTab)
    );
  });

  if (!query) {
    const message = `No matching query found for scope=${scope}, filterTab=${filterTab}, workable=${workable}`;
    console.error(message);
    throw new Error(message);
  }

  return query;
};

export const getControlVisibility = (
  scope: ScopeParam,
  filterTab: FilterTabParam,
  workable: WorkableParam,
): {
  scopeControl: ControlVisibility;
  workableControl: ControlVisibility;
} => {
  const isWorkableEnabled = isWorkableEnabledTab(filterTab);

  return {
    scopeControl: {
      show: !isAlleOnlyTab(filterTab),
      value: scope === 'MEINE',
    },
    workableControl: {
      show: isWorkableEnabled,
      value: workable === 'TRUE',
    },
  };
};

export const isGesuchQuery = (
  query: DashboardQuery,
): query is Exclude<DashboardQuery, 'ALLE_DARLEHEN' | 'MEINE_DARLEHEN'> => {
  return !query.includes('DARLEHEN') || query.includes('BEARBEITBAR');
};

export const isDarlehenQuery = (
  query: DashboardQuery,
): query is Extract<DashboardQuery, 'ALLE_DARLEHEN' | 'MEINE_DARLEHEN'> => {
  return query === 'ALLE_DARLEHEN' || query === 'MEINE_DARLEHEN';
};

export const getQueryParamsFromToggleValues = (
  scopeValue: boolean | undefined,
  workableValue: boolean | undefined,
  filterTab: FilterTabParam,
): DashQueryParams => {
  const scope = scopeValue ? 'MEINE' : 'ALLE';
  const workable = workableValue ? 'TRUE' : 'FALSE';

  // additional check
  getQueryFromParams(scope, filterTab, workable);

  return { scope, filterTab, workable };
};
