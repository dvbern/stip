import { createActionGroup, props } from '@ngrx/store';

import { SharedModelError } from '@dv/shared/model/error';
import { DokumentTyp, GesuchDokument } from '@dv/shared/model/gesuch';

export const SharedDataAccessDokumenteApiEvents = createActionGroup({
  source: 'Dokumente API',
  events: {
    dokumentesLoadedSuccess: props<{ dokumentes: GesuchDokument[] }>(),
    dokumentesLoadedFailure: props<{ error: SharedModelError }>(),
    getRequiredDocumentTypeSuccess: props<{
      requiredDocumentTypes: DokumentTyp[];
    }>(),
    getRequiredDocumentTypeFailure: props<{ error: SharedModelError }>(),
  },
});
