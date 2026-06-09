import { AvailableBenutzerRole } from '@dv/shared/model/benutzer';
import {
  FreiwilligDarlehenDashboard,
  GesuchTrancheStatus,
  GesuchTrancheTyp,
  Gesuchstatus,
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
  'id' | 'gesuchTrancheId' | 'gesuchStatus' | 'trancheStatus' | 'typ'
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

export type BooleanParam = 'TRUE' | 'FALSE';
export type DashboardQuery =
  | keyof typeof GetGesucheSBQueryType
  | 'AENDERUNGEN'
  | 'DARLEHEN'
  | 'FEHLGESCHLAGENE_ZAHLUNGEN'
  | 'JURISTISCHE_ABKLAERUNG'
  | 'ABKLAERUNG_DURCH_RECHSTABTEILUNG';

export type FilterConfig = {
  queryTyp?: DashboardQuery;
  bearbeitbar: BooleanParam[];
  filterTab: FilterTabParam[];
};

export type FilterTabParam = DashboardQuery;

export type ToggleConfig = { show: boolean; value: boolean };

export type DashFilterQueryParams = {
  filterTab: FilterTabParam;
  zugewiesen: BooleanParam;
  bearbeitbar: BooleanParam;
};

export type NullableDashFilterQueryParams = {
  filterTab?: FilterTabParam | null;
  zugewiesen?: BooleanParam | null;
  bearbeitbar?: BooleanParam | null;
};

export interface DashboardFilterTabItem extends TabNavItem {
  queryParams: NullableDashFilterQueryParams;
  class?: string;
}

export const isBearbeitbarEnabledTab = (filterTab: FilterTabParam): boolean => {
  const bearbeitbarTabs: FilterTabParam[] = ['ALLE', 'AENDERUNGEN', 'DARLEHEN'];
  return bearbeitbarTabs.includes(filterTab);
};

/**
 *
 * Tabs for Queries that only allow 'ALLE' as scope, such as all JUR Queries
 */
export const isZugewiesenEnabledTab = (filterTab: FilterTabParam): boolean => {
  return ![
    'JURISTISCHE_ABKLAERUNG',
    'ABKLAERUNG_DURCH_RECHSTABTEILUNG',
  ].includes(filterTab);
};

export const getGesucheSBQueryType = (
  dashboardQuery: DashboardQuery,
): GetGesucheSBQueryType => {
  switch (dashboardQuery) {
    case 'AENDERUNGEN':
    case 'ABKLAERUNG_DURCH_RECHSTABTEILUNG':
    case 'JURISTISCHE_ABKLAERUNG':
    case 'DARLEHEN':
    case 'FEHLGESCHLAGENE_ZAHLUNGEN':
      return 'ALLE';
    default:
      return dashboardQuery;
  }
};

export const getControlVisibility = (
  zugewiesen: BooleanParam,
  bearbeitbar: BooleanParam,
  filterTab: FilterTabParam,
): {
  zugewiesenConfig: ToggleConfig;
  bearbeitbarConfig: ToggleConfig;
} => {
  const isBearbeitbarEnabled = isBearbeitbarEnabledTab(filterTab);
  const isZugewiesenEnabled = isZugewiesenEnabledTab(filterTab);

  return {
    zugewiesenConfig: {
      show: isZugewiesenEnabled,
      value: isZugewiesenEnabled && zugewiesen === 'TRUE',
    },
    bearbeitbarConfig: {
      show: isBearbeitbarEnabled,
      value: !isBearbeitbarEnabled || bearbeitbar === 'TRUE',
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
  filterTab: 'ALLE',
  zugewiesen: 'TRUE',
  bearbeitbar: 'TRUE',
};

export const JuristDefautlQuery: DashFilterQueryParams = {
  filterTab: 'JURISTISCHE_ABKLAERUNG',
  zugewiesen: 'FALSE',
  bearbeitbar: 'FALSE',
};

export const getDefaultQueryForRole = (
  roles: Partial<Record<AvailableBenutzerRole, true>>,
): DashFilterQueryParams => {
  const isJurist = roles.V0_Jurist;

  if (isJurist) {
    return JuristDefautlQuery;
  }

  return SachbearbeiterDefaultQuery;
};
