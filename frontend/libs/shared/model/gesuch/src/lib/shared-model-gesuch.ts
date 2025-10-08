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

export type GesuchFormularType = GesuchFormular;
export type SharedModelGesuchFormularUpdate = GesuchFormularUpdate;

// for sb
export type SBSteuerdatenSteps =
  `steuerdaten${Capitalize<Lowercase<SteuerdatenTyp>>}`;

// for gs
export type GSSteuererklaerungSteps =
  `steuererklaerung${Capitalize<Lowercase<SteuerdatenTyp>>}`;

export type FormPropsExcluded = Exclude<
  keyof GesuchFormularType,
  'steuerdatenTabs' | 'steuererklaerung'
>;

export type GSFormStepProps =
  | FormPropsExcluded
  | GSSteuererklaerungSteps
  | 'auszahlung'
  | 'dokuments'
  | 'abschluss';

export type SBFormStepProps =
  | FormPropsExcluded
  | SBSteuerdatenSteps
  | 'auszahlung'
  | 'dokuments';

export type AllFormSteps = GSFormStepProps | SBFormStepProps;

export const TRANCHE_TYPE_INITIAL = 'INITIAL' as const;
export type GesuchUrlType = GesuchTrancheTyp | typeof TRANCHE_TYPE_INITIAL;

export type GesuchFilter = keyof typeof GetGesucheSBQueryType;

export type AenderungMelden = {
  gesuch: GesuchDashboardItem;
};

export type TrancheSetting = {
  type: GesuchTrancheTyp;
  gesuchUrlTyp: GesuchUrlType;
  routesSuffix: string[];
};
