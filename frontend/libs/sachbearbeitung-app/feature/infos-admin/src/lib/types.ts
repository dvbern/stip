import { DokumentOptions } from '@dv/shared/model/dokument';
import { AusbildungUnterbruchAntragSB } from '@dv/shared/model/gesuch';

export type AusbildungUnterbruchAntragEntry = AusbildungUnterbruchAntragSB & {
  documentDownloadOptions: DokumentOptions;
};
