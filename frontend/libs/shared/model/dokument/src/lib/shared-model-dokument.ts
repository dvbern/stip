import { Dokument, DokumentTyp, GesuchDokument } from '@dv/shared/model/gesuch';
import { SharedModelGesuchFormStep } from '@dv/shared/model/gesuch-form';

export interface SharedModelTableDokument {
  formStep: SharedModelGesuchFormStep;
  titleKey: string;
  dokumentTyp: string;
  gesuchDokument?: GesuchDokument;
  documentOptions: DokumentOptions;
}

export interface DokumentOptions {
  singleUpload: boolean;
  titleKey: string;
  trancheId: string;
  allowTypes: string;
  dokumentTyp: DokumentTyp;
  gesuchDokument?: GesuchDokument;
  initialDocuments?: Dokument[];
  readonly: boolean;
}

export interface DokumentUpload {
  file: Dokument;
  isTemporary?: boolean;
  progress?: number;
  error?: { translationKey: string; values?: unknown };
}

export interface DokumentView extends DokumentUpload {
  state: 'uploading' | 'done' | 'error';
  theme:
    | { icon: 'warning'; type: 'warn'; color: 'warn' }
    | { icon: 'sync'; type: 'info'; color: 'info' }
    | { icon: 'check'; type: 'success'; color: 'success' };
}

export interface DokumentListView {
  gesuchDokument?: GesuchDokument;
  dokuments: DokumentView[];
}

export interface DokumentState {
  gesuchDokument?: GesuchDokument;
  dokuments: DokumentUpload[];
  errorKey?: string;
}

export interface UploadView {
  trancheId: string;
  type: DokumentTyp;
  readonly: boolean;
  gesuchDokument?: GesuchDokument;
  initialDocuments?: Dokument[];
  hasEntries: boolean;
  isSachbearbeitungApp: boolean;
}
