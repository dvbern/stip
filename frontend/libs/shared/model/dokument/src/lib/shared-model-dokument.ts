import { Dokument, DokumentTyp, GesuchDokument } from '@dv/shared/model/gesuch';
import { SharedModelGesuchFormStep } from '@dv/shared/model/gesuch-form';

export interface SharedModelTableDokument {
  formStep: SharedModelGesuchFormStep;
  titleKey: string;
  dokumentTyp: string;
  gesuchDokument?: GesuchDokument;
  documentOptions: DocumentOptions;
}

export interface DocumentOptions {
  singleUpload: boolean;
  titleKey: string;
  trancheId: string;
  allowTypes: string;
  dokumentTyp: DokumentTyp;
  initialDocuments?: Dokument[];
  readonly: boolean;
}

export interface DocumentUpload {
  file: Dokument;
  isTemporary?: boolean;
  progress?: number;
  error?: { translationKey: string; values?: unknown };
}

export interface DocumentView extends DocumentUpload {
  state: 'uploading' | 'done' | 'error';
  theme:
    | { icon: 'warning'; type: 'warn'; color: 'warn' }
    | { icon: 'sync'; type: 'info'; color: 'info' }
    | { icon: 'check'; type: 'success'; color: 'success' };
}

export interface DocumentState {
  documents: DocumentUpload[];
  errorKey?: string;
}

export interface UploadView {
  trancheId: string;
  type: DokumentTyp;
  readonly: boolean;
  initialDocuments?: Dokument[];
  hasEntries: boolean;
  isSachbearbeitungApp: boolean;
}
