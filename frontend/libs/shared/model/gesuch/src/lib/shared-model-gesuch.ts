import { GesuchFormular } from './openapi/model/gesuchFormular';
import { GesuchFormularUpdate } from './openapi/model/gesuchFormularUpdate';
import { GesuchWithChanges } from './openapi/model/gesuchWithChanges';
import { GetGesucheSBQueryType } from './openapi/model/getGesucheSBQueryType';
import { SteuerdatenTyp } from './openapi/model/steuerdatenTyp';

export interface SharedModelGesuch extends GesuchWithChanges {
  view?: {
    // view specific props
  };
}

export type SharedModelGesuchFormular = GesuchFormular;
export type SharedModelGesuchFormularUpdate = GesuchFormularUpdate;

export type SteuerdatenSteps =
  `steuerdaten${Capitalize<Lowercase<Exclude<SteuerdatenTyp, 'FAMILIE'>>> | ''}`;
export type SharedModelGesuchFormularProps =
  | Exclude<keyof SharedModelGesuchFormular, 'steuerdatenTabs'>
  | SteuerdatenSteps
  | 'dokuments';

// TODO extract to env or generate with OpenAPI?
export const SHARED_MODEL_GESUCH_RESOURCE = `/gesuch`;
export const SHARED_MODEL_GESUCHSPERIODE_RESOURCE = `/gesuchsperiode`;

export enum GesuchsperiodeSemester {
  HERBST = 'HERBST',
  FRUEHLING = 'FRUEHLING',
}

export type GesuchFilter = keyof Omit<
  typeof GetGesucheSBQueryType,
  'ALLE_MEINE'
>;
