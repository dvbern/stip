import { Dokument, DokumentTyp } from '@dv/shared/model/gesuch';
import { SharedModelGesuchFormStep } from '@dv/shared/model/gesuch-form';

import { DocumentOptions } from './upload.model';

export interface TableDocument {
  id?: string;
  formStep: SharedModelGesuchFormStep;
  dokumentTyp: DokumentTyp;
  titleKey?: string;
  dokumente?: Dokument[];
  documentOptions: DocumentOptions | null;
}
