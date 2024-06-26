import { Dokument, DokumentTyp } from '@dv/shared/model/gesuch';

export interface DocumentOptions {
  singleUpload: boolean;
  titleKey: string;
  gesuchId: string;
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
  gesuchId: string;
  type: DokumentTyp;
  readonly: boolean;
}
