import { GesuchFormular } from './openapi/model/gesuchFormular';
import { GesuchFormularUpdate } from './openapi/model/gesuchFormularUpdate';
import { GesuchWithChanges } from './openapi/model/gesuchWithChanges';
import { GetGesucheSBQueryType } from './openapi/model/getGesucheSBQueryType';
import { GesuchTranche } from './openapi/model/models';
import { SteuerdatenTyp } from './openapi/model/steuerdatenTyp';

export interface SharedModelGesuch extends GesuchWithChanges {
  // eslint-disable-next-line @typescript-eslint/no-empty-object-type
  view?: {
    // view specific props
  };
}

export type TrancheChange = {
  tranche: GesuchTranche;
  affectedSteps: string[];
};

export type AppTrancheChange = {
  gs: TrancheChange | undefined;
  sb: TrancheChange | undefined;
};

export type SharedModelGesuchFormular = GesuchFormular;
export type SharedModelGesuchFormularUpdate = GesuchFormularUpdate;

export type SteuerdatenSteps =
  `steuerdaten${Capitalize<Lowercase<Exclude<SteuerdatenTyp, 'FAMILIE'>>> | ''}`;

export type SharedModelGesuchFormularProps = Exclude<
  keyof SharedModelGesuchFormular,
  'steuerdatenTabs'
>;

export type SharedModelGesuchFormularPropsSteuerdatenSteps =
  | SharedModelGesuchFormularProps
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
