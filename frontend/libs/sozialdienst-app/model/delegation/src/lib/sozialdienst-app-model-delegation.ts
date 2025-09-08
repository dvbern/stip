import { InputSignal, InputSignalWithTransform } from '@angular/core';
import {
  Adresse,
  DelegierenServiceGetDelegierungsOfSozialdienstAdminRequestParams,
  DelegierenServiceGetDelegierungsOfSozialdienstMitarbeiterRequestParams,
  Delegierung,
  FallWithDelegierung,
  GetDelegierungSozQueryTypeAdmin,
  GetDelegierungSozQueryTypeMitarbeiter,
  PersoenlicheAngaben,
  SozDashboardColumn,
} from '@dv/shared/model/gesuch';
import { SortAndPageInputs } from '@dv/shared/model/table';

export type LoadPaginatedDashboardByRoles =
  | DelegierenServiceGetDelegierungsOfSozialdienstAdminRequestParams
  | DelegierenServiceGetDelegierungsOfSozialdienstMitarbeiterRequestParams;

export type GetDelegierungSozQueryType =
  | GetDelegierungSozQueryTypeMitarbeiter
  | GetDelegierungSozQueryTypeAdmin;

export type PersoehnelicheAngabenKeys = keyof Omit<
  PersoenlicheAngaben,
  'adresse' | 'anrede'
>;

export type StatusKey = keyof Pick<Delegierung, 'delegierungAngenommen'>;
export type StatusFilter = {
  [key in StatusKey]: InputSignalWithTransform<
    boolean | undefined,
    string | undefined
  >;
};

export type FallWithDelegierungKeys = keyof Pick<
  FallWithDelegierung,
  'fallNummer'
>;

export type OrtKey = keyof Pick<Adresse, 'ort'>;

export type WohnortKey = `wohn${OrtKey}`;

export type SozCockpitFilterFormKeys =
  | FallWithDelegierungKeys
  | Exclude<PersoehnelicheAngabenKeys, 'email' | 'sprache'>
  | WohnortKey
  | StatusKey;

export type SozCockpitFilterKeys =
  | FallWithDelegierungKeys
  | Exclude<PersoehnelicheAngabenKeys, 'email' | 'sprache'>
  | WohnortKey;

export type SozCockpitFilterInputs = Record<
  SozCockpitFilterKeys,
  InputSignal<string | undefined>
>;

export interface SozCockpitBaseFilterInputs
  extends SortAndPageInputs<SozDashboardColumn> {
  show: InputSignal<GetDelegierungSozQueryType | undefined>;
}

export interface SozCockitComponentInputs
  extends SozCockpitFilterInputs,
    SozCockpitBaseFilterInputs,
    StatusFilter {}
