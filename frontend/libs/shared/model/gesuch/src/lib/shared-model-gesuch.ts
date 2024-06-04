import { Gesuch } from './openapi/model/gesuch';
import { GesuchFormular } from './openapi/model/gesuchFormular';
import { GesuchFormularUpdate } from './openapi/model/gesuchFormularUpdate';

export interface SharedModelGesuch extends Gesuch {
  view?: {
    // view specific props
  };
}

export type SharedModelGesuchFormular = GesuchFormular;
export type SharedModelGesuchFormularUpdate = GesuchFormularUpdate;

export type SharedModelGesuchFormularProps =
  | keyof SharedModelGesuchFormular
  | 'dokuments';

// TODO extract to env or generate with OpenAPI?
export const SHARED_MODEL_GESUCH_RESOURCE = `/gesuch`;
export const SHARED_MODEL_GESUCHSPERIODE_RESOURCE = `/gesuchsperiode`;

export enum GesuchsperiodeSemester {
  HERBST = 'HERBST',
  FRUEHLING = 'FRUEHLING',
}
