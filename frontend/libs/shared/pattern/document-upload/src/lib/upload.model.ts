import { Dokument, DokumentTyp } from '@dv/shared/model/gesuch';

export interface DocumentOptions {
  multiple: boolean;
  titleKey: string;
  gesuchId: string;
  dokumentTyp: DokumentTyp;
}

export interface DocumentUpload {
  file: Dokument;
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
  allowedFormats: string;
}
