import { FallAuszahlung, Land } from '@dv/shared/model/gesuch';
import { Language } from '@dv/shared/model/language';

export type SharedModelAuszahlung = {
  auszahlung?: FallAuszahlung;
  isLoading: boolean;
  readonly: boolean;
  laender: Land[];
  language: Language;
  origin?: {
    backlink: string;
    gesuchId: string;
    gesuchTrancheId: string;
  };
  invalidFormularControls: string[] | undefined;
};
