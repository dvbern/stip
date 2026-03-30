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

export type WorkableQueryParam = 'TRUE' | 'FALSE';
export const workableEnabledTabFilters = [
  'GESUCHE',
  'DARLEHEN',
] as const satisfies readonly FilterTabQueryParam[];
export type WorkableEnabledTabFilters =
  (typeof workableEnabledTabFilters)[number];

export type QueryTypePrefix =
  DashboardQueryType extends `${infer Prefix}_${string}` ? Prefix : never;

type QueryTypeSuffix<T extends string> =
  T extends `${QueryTypePrefix}_${infer Suffix}` ? Suffix : never;

export type FilterGesucheQueryParam = QueryTypeSuffix<GetGesucheSBQueryType>;

export type FilterFreiwilligDarlehenQueryParam =
  QueryTypeSuffix<GetFreiwilligDarlehenSbQueryType>;

export type FilterTabQueryParam =
  | FilterGesucheQueryParam
  | FilterFreiwilligDarlehenQueryParam;

type FilterGesucheScopeSelectableQueryParam = QueryTypeSuffix<
  Extract<GetGesucheSBQueryType, `MEINE_${string}`>
>;

type FilterGesucheAlleOnlyQueryParam = Exclude<
  FilterGesucheQueryParam,
  FilterGesucheScopeSelectableQueryParam
>;

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

type GesuchQueryCandidate = `${QueryTypePrefix}_${FilterGesucheQueryParam}`;
type FreiwilligDarlehenQueryCandidate =
  `${QueryTypePrefix}_${FilterFreiwilligDarlehenQueryParam}`;

export const isGesuchQueryValid = (
  query: GesuchQueryCandidate,
): query is GetGesucheSBQueryType => {
  const valid = Object.values(GetGesucheSBQueryType).includes(
    query as GetGesucheSBQueryType,
  );
  return valid;
};

export const isFreiwilligDarlehenQueryValid = (
  query: FreiwilligDarlehenQueryCandidate,
): query is GetFreiwilligDarlehenSbQueryType => {
  return Object.values(GetFreiwilligDarlehenSbQueryType).includes(
    query as GetFreiwilligDarlehenSbQueryType,
  );
};

export const getGesuchQueryFromParams = (
  scope: QueryTypePrefix,
  filterTab: FilterGesucheQueryParam,
): GetGesucheSBQueryType => {
  const query = `${scope}_${filterTab}` as GesuchQueryCandidate;

  if (isGesuchQueryValid(query)) {
    return query;
  }

  // Some gesuche tabs only exist with ALLE_* variants.
  return `ALLE_${filterTab}` as GetGesucheSBQueryType;
};
