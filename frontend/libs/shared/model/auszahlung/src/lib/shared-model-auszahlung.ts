import { Auszahlung, Land } from '@dv/shared/model/gesuch';

export type SharedModelAuszahlung = {
  auszahlung?: Auszahlung;
  isLoading: boolean;
  readonly: boolean;
  laender: Land[];
  language: string;
  backlink?: string | null;
  invalidFormularControls: string[] | undefined;
};
