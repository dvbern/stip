import { AvailableBenutzerRole } from '@dv/shared/model/benutzer';
import {
  FreiwilligDarlehenDashboard,
  GesuchTrancheStatus,
  GesuchTrancheTyp,
  Gesuchstatus,
  GetFreiwilligDarlehenSbQueryType,
  GetGesucheSBQueryType,
  SbDashboardGesuch,
} from '@dv/shared/model/gesuch';
import { AppendFromTo } from '@dv/shared/model/type-util';
import { TabNavItem } from '@dv/shared/util/navigation';

export const gesucheStatusByTyp = {
  TRANCHE: Object.values(Gesuchstatus).filter(
    (key: Gesuchstatus) => key !== 'IN_BEARBEITUNG_GS',
  ),
  AENDERUNG: Object.values(GesuchTrancheStatus).filter(
    (key: GesuchTrancheStatus) => key !== 'IN_BEARBEITUNG_GS',
  ),
} satisfies Record<GesuchTrancheTyp, unknown>;

type DashboardFormStatus = Gesuchstatus | GesuchTrancheStatus;

type DashboardGesuchEntry = Omit<
  SbDashboardGesuch,
  'id' | 'gesuchTrancheId' | 'gesuchStatus' | 'trancheStatus'
> & { status: DashboardFormStatus };
type DashboardGesuchEntryFields = keyof DashboardGesuchEntry;

type DashboardDarlehenEntry = Omit<
  FreiwilligDarlehenDashboard,
  'id' | 'fallId' | 'relatedGesuchId'
>;
export type DashboardDarlehenEntryFields = keyof DashboardDarlehenEntry;

export type DashboardTableEntryFields =
  | DashboardDarlehenEntryFields
  | DashboardGesuchEntryFields;

/**
 * Special date fields which are treated as start-end fields only during filtering
 */
export type StartEndFields = keyof Pick<
  DashboardGesuchEntry | DashboardDarlehenEntry,
  'letzteAktivitaet'
>;
export type DashboardFormSimpleFields = Exclude<
  DashboardTableEntryFields,
  StartEndFields
>;
export type DashboardFormStartEndFields = AppendFromTo<StartEndFields>;
export type DashboardFormFields =
  | DashboardFormSimpleFields
  | DashboardFormStartEndFields;

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
  ALLE_BEARBEITBAR                      : { scope: 'ALLE',  workable: ['TRUE'],          filterTab: ['GESUCHE', 'DARLEHEN']},
  MEINE_BEARBEITBAR                     : { scope: 'MEINE', workable: ['TRUE'],          filterTab: ['GESUCHE', 'DARLEHEN'] },
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

export type ToggleConfig = { show: boolean; value: boolean };

export type DashFilterQueryParams = {
  filterTab: FilterTabParam;
  scope: ScopeParam;
  workable: WorkableParam;
};

export type NullableDashFilterQueryParams = {
  filterTab?: FilterTabParam | null;
  scope?: ScopeParam | null;
  workable?: WorkableParam | null;
};

export interface DashboardFilterTabItem extends TabNavItem {
  queryParams?: NullableDashFilterQueryParams;
}

export const isValidDashboardQuery = (
  query: string,
): query is DashboardQuery => {
  return dashboardQueries.some((q) => q === query);
};

export const isWorkableEnabledTab = (filterTab: FilterTabParam): boolean => {
  return ['GESUCHE', 'DARLEHEN'].includes(filterTab);
};

/**
 *
 * Tabs for Queries that only allow 'ALLE' as scope, such as all JUR Queries
 */
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
  workable: WorkableParam,
  filterTab: FilterTabParam,
): DashboardQuery => {
  let worableVal = workable;

  // force workable to FALSE if the filterTab is not workable-enabled to avoid invalid query combinations
  // this does not reset the query param!
  if (!isWorkableEnabledTab(filterTab)) {
    worableVal = 'FALSE';
  }

  const query = dashboardQueries.find((q) => {
    const config = dashboardFilterQueryWithParamsMap[q];
    return (
      config.filterTab.includes(filterTab) &&
      config.workable.includes(worableVal) &&
      config.scope === scope
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
  workable: WorkableParam,
  filterTab: FilterTabParam,
): {
  scopeConfig: ToggleConfig;
  workableConfig: ToggleConfig;
} => {
  const isWorkableEnabled = isWorkableEnabledTab(filterTab);

  return {
    scopeConfig: {
      show: !isAlleOnlyTab(filterTab),
      value: scope === 'MEINE',
    },
    workableConfig: {
      show: isWorkableEnabled,
      value: workable === 'TRUE',
    },
  };
};

export const isGesuchQuery = (
  query: DashboardQuery,
): query is Exclude<DashboardQuery, 'ALLE_DARLEHEN' | 'MEINE_DARLEHEN'> => {
  return !query.includes('DARLEHEN');
};

export const isDarlehenQuery = (
  query: DashboardQuery,
): query is Extract<DashboardQuery, 'ALLE_DARLEHEN' | 'MEINE_DARLEHEN'> => {
  return query.includes('DARLEHEN') || query.includes('BEARBEITBAR');
};

export const SachbearbeiterDefaultQery: DashFilterQueryParams = {
  filterTab: 'GESUCHE',
  scope: 'MEINE',
  workable: 'TRUE',
};

export const JuristDefautlQuery: DashFilterQueryParams = {
  filterTab: 'JURISTISCHE_ABKLAERUNG',
  scope: 'ALLE',
  workable: 'FALSE',
};

export const getDefaultQueryForRole = (
  roles: Partial<Record<AvailableBenutzerRole, true>>,
): DashFilterQueryParams => {
  const isSachbearbeiter = roles.V0_Sachbearbeiter;
  const isJurist = roles.V0_Jurist;

  if (isJurist) {
    return JuristDefautlQuery;
  }

  if (isSachbearbeiter) {
    return SachbearbeiterDefaultQery;
  }

  return SachbearbeiterDefaultQery;
};
