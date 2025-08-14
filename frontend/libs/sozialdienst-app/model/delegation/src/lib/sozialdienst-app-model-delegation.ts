import { InputSignal, InputSignalWithTransform } from '@angular/core';
import {
  Adresse,
  Delegierung,
  FallWithDelegierung,
  GetDelegierungSozQueryType,
  PersoenlicheAngaben,
  SozDashboardColumn,
} from '@dv/shared/model/gesuch';
import { SortAndPageInputs } from '@dv/shared/model/table';

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
  | PersoehnelicheAngabenKeys
  | WohnortKey
  | StatusKey;

export type SozCockpitFilterKeys =
  | FallWithDelegierungKeys
  | PersoehnelicheAngabenKeys
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
