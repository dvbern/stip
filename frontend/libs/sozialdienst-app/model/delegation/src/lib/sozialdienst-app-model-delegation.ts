import { InputSignal, InputSignalWithTransform } from '@angular/core';
import {
  Adresse,
  Delegierung,
  FallWithDelegierung,
  GetDelegierungSozQueryType,
  PersoenlicheAngaben,
  SortOrder,
  SozDashboardColumn,
} from '@dv/shared/model/gesuch';
import { AppendFromTo } from '@dv/shared/model/type-util';

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

export type LetzteAktivitaetKey = keyof Pick<
  FallWithDelegierung,
  'letzteAktivitaet'
>;

export type LetzteAktivitaetFromToKeys = AppendFromTo<LetzteAktivitaetKey>;

export type SozCockpitFilterFormKeys =
  | FallWithDelegierungKeys
  | PersoehnelicheAngabenKeys
  | OrtKey
  | StatusKey;

export type SozCockpitFilterKeys =
  | FallWithDelegierungKeys
  | PersoehnelicheAngabenKeys
  | OrtKey
  | LetzteAktivitaetFromToKeys;

export type SozCockpitFilterInputs = Record<
  SozCockpitFilterKeys,
  InputSignal<string | undefined>
>;

export interface SozCockpitBaseFilterInputs {
  show: InputSignal<GetDelegierungSozQueryType | undefined>;
  sortColumn: InputSignal<SozDashboardColumn | undefined>;
  sortOrder: InputSignal<SortOrder | undefined>;
  page: InputSignalWithTransform<number | undefined, string | undefined>;
  pageSize: InputSignalWithTransform<number | undefined, string | undefined>;
}

export interface SozCockitComponentInputs
  extends SozCockpitFilterInputs,
    SozCockpitBaseFilterInputs,
    StatusFilter {}
