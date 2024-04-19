import { Dokument, DokumentTyp } from '@dv/shared/model/gesuch';

export interface DocumentOptions {
  singleUpload: boolean;
  titleKey: string;
  gesuchId: string;
  allowTypes: string;
  dokumentTyp: DokumentTyp;
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
    | { icon: 'warning'; type: 'danger'; color: 'warn' }
    | { icon: 'sync'; type: 'info'; color: 'accent' }
    | { icon: 'check'; type: 'success'; color: 'primary' };
}

export interface DocumentState {
  documents: DocumentUpload[];
  errorKey?: string;
}

export interface UploadView {
  gesuchId: string;
  type: DokumentTyp;
}
