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

export type BearbeitbarParam = 'TRUE' | 'FALSE';
export type ScopeParam = 'ALLE' | 'MEINE';
export type DashboardQuery =
  | keyof typeof GetGesucheSBQueryType
  | keyof typeof GetFreiwilligDarlehenSbQueryType;

export type FilterConfig = {
  scope: ScopeParam;
  bearbeitbar: BearbeitbarParam[];
  filterTab: FilterTabParam[];
};

// prettier-ignore
export const dashboardFilterQueryWithParamsMap: Record<DashboardQuery, FilterConfig> = {
  ALLE_DARLEHEN                         : { scope: 'ALLE',  bearbeitbar: ['FALSE'], filterTab: ['DARLEHEN'] },
  MEINE_DARLEHEN                        : { scope: 'MEINE', bearbeitbar: ['FALSE'], filterTab: ['DARLEHEN'] },
  ALLE_BEARBEITBAR                      : { scope: 'ALLE',  bearbeitbar: ['TRUE'],  filterTab: ['GESUCHE', 'DARLEHEN']},
  MEINE_BEARBEITBAR                     : { scope: 'MEINE', bearbeitbar: ['TRUE'],  filterTab: ['GESUCHE', 'DARLEHEN'] },
  ALLE_GESUCHE                          : { scope: 'ALLE',  bearbeitbar: ['FALSE'], filterTab: ['GESUCHE'] },
  MEINE_GESUCHE                         : { scope: 'MEINE', bearbeitbar: ['FALSE'], filterTab: ['GESUCHE'] },
  ALLE_PENDENTE_GESUCHE                 : { scope: 'ALLE',  bearbeitbar: ['FALSE'], filterTab: ['PENDENTE_GESUCHE'] },
  MEINE_PENDENTE_GESUCHE                : { scope: 'MEINE', bearbeitbar: ['FALSE'], filterTab: ['PENDENTE_GESUCHE'] },
  ALLE_JURISTISCHE_ABKLAERUNG           : { scope: 'ALLE',  bearbeitbar: ['FALSE'], filterTab: ['JURISTISCHE_ABKLAERUNG'] },
  ALLE_ABKLAERUNG_DURCH_RECHSTABTEILUNG : { scope: 'ALLE',  bearbeitbar: ['FALSE'], filterTab: ['ABKLAERUNG_DURCH_RECHSTABTEILUNG'] },
  ALLE_DRUCKBAR_VERFUEGUNGEN            : { scope: 'ALLE',  bearbeitbar: ['FALSE'], filterTab: ['DRUCKBAR_VERFUEGUNGEN'] },
  MEINE_DRUCKBAR_VERFUEGUNGEN           : { scope: 'MEINE', bearbeitbar: ['FALSE'], filterTab: ['DRUCKBAR_VERFUEGUNGEN'] },
  ALLE_DRUCKBAR_DATENSCHUTZBRIEFE       : { scope: 'ALLE',  bearbeitbar: ['FALSE'], filterTab: ['DRUCKBAR_DATENSCHUTZBRIEFE'] },
  MEINE_DRUCKBAR_DATENSCHUTZBRIEFE      : { scope: 'MEINE', bearbeitbar: ['FALSE'], filterTab: ['DRUCKBAR_DATENSCHUTZBRIEFE'] },
}

export const dashboardQueries = Object.keys(
  dashboardFilterQueryWithParamsMap,
) as Array<keyof typeof dashboardFilterQueryWithParamsMap>;

export type QueryTypeSuffix<T extends string> =
  T extends `${ScopeParam}_${infer Suffix}` ? Suffix : never;

export type FilterTabParam =
  | QueryTypeSuffix<DashboardQuery>
  | 'FEHLGESCHLAGENE_ZAHLUNGEN';

export type ToggleConfig = { show: boolean; value: boolean };

export type DashFilterQueryParams = {
  filterTab: FilterTabParam;
  zugewiesen: ScopeParam;
  bearbeitbar: BearbeitbarParam;
};

export type NullableDashFilterQueryParams = {
  filterTab?: FilterTabParam | null;
  scope?: ScopeParam | null;
  bearbeitbar?: BearbeitbarParam | null;
};

export interface DashboardFilterTabItem extends TabNavItem {
  queryParams: NullableDashFilterQueryParams;
  class?: string;
}

export const isValidDashboardQuery = (
  query: string,
): query is DashboardQuery => {
  return dashboardQueries.some((q) => q === query);
};

export const isBearbeitbarEnabledTab = (filterTab: FilterTabParam): boolean => {
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
  bearbeitbar: BearbeitbarParam,
  filterTab: FilterTabParam,
): DashboardQuery => {
  let worableVal = bearbeitbar;

  // force bearbeitbar to FALSE if the filterTab is not bearbeitbar-enabled to avoid invalid query combinations
  // this does not reset the query param!
  if (!isBearbeitbarEnabledTab(filterTab)) {
    worableVal = 'FALSE';
  }

  const query = dashboardQueries.find((q) => {
    const config = dashboardFilterQueryWithParamsMap[q];
    return (
      config.filterTab.includes(filterTab) &&
      config.bearbeitbar.includes(worableVal) &&
      config.scope === scope
    );
  });

  if (!query) {
    const message = `No matching query found for scope=${scope}, filterTab=${filterTab}, bearbeitbar=${bearbeitbar}`;
    console.error(message);
    throw new Error(message);
  }

  return query;
};

export const getControlVisibility = (
  zugewiesen: ScopeParam,
  bearbeitbar: BearbeitbarParam,
  filterTab: FilterTabParam,
): {
  zugewiesenConfig: ToggleConfig;
  bearbeitbarConfig: ToggleConfig;
} => {
  const isBearbeitbarEnabled = isBearbeitbarEnabledTab(filterTab);

  return {
    zugewiesenConfig: {
      show: !isAlleOnlyTab(filterTab),
      value: zugewiesen === 'MEINE',
    },
    bearbeitbarConfig: {
      show: isBearbeitbarEnabled,
      value: bearbeitbar === 'TRUE',
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

export const SachbearbeiterDefaultQuery: DashFilterQueryParams = {
  filterTab: 'GESUCHE',
  zugewiesen: 'MEINE',
  bearbeitbar: 'TRUE',
};

export const JuristDefautlQuery: DashFilterQueryParams = {
  filterTab: 'JURISTISCHE_ABKLAERUNG',
  zugewiesen: 'ALLE',
  bearbeitbar: 'FALSE',
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
    return SachbearbeiterDefaultQuery;
  }

  return SachbearbeiterDefaultQuery;
};
