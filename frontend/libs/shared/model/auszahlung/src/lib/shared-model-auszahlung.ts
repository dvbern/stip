import { FallAuszahlung, Land } from '@dv/shared/model/gesuch';

export type SharedModelAuszahlung = {
  auszahlung?: FallAuszahlung;
  isLoading: boolean;
  readonly: boolean;
  laender: Land[];
  language: string;
  backlink?: string | null;
  invalidFormularControls: string[] | undefined;
};
