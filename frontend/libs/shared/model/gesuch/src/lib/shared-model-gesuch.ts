import { GesuchFormular } from './openapi/model/gesuchFormular';
import { GesuchFormularUpdate } from './openapi/model/gesuchFormularUpdate';
import { GesuchWithChanges } from './openapi/model/gesuchWithChanges';
import { GetGesucheSBQueryType } from './openapi/model/getGesucheSBQueryType';
import {
  GesuchDashboardItem,
  GesuchTranche,
  GesuchTrancheTyp,
} from './openapi/model/models';
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

export const TRANCHE_TYPE_INITIAL = 'INITIAL' as const;
export type GesuchUrlType = GesuchTrancheTyp | typeof TRANCHE_TYPE_INITIAL;

export type GesuchFilter = keyof Omit<
  typeof GetGesucheSBQueryType,
  'ALLE_MEINE'
>;

export type AenderungMelden = {
  gesuch: GesuchDashboardItem;
};

export type TrancheSetting = {
  type: GesuchTrancheTyp;
  routesSuffix: string[];
};
