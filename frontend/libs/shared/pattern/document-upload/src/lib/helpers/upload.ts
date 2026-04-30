import { HttpEvent, HttpEventType } from '@angular/common/http';
import { Observable, combineLatest, interval, map, takeWhile } from 'rxjs';

import {
  DOKUMENT_THEMES,
  DokumentUpload,
  DokumentView,
} from '@dv/shared/model/dokument';

const PROGRESS_ANIMATION_TIME = 600;
const FORMAT_ERROR = 'shared.file.invalidFormat';
const GENERIC_ERROR = 'shared.genericError.general';

export function createGenericError() {
  return { errorKey: GENERIC_ERROR };
}

export function createTempId(document: File) {
  return `uploading-${document.name}${document.size}${document.type}`;
}

export function createHttpEvent(event: HttpEvent<unknown>): HttpEvent<unknown> {
  return event;
}

export function updateProgressFor(
  dokumente: DokumentUpload[],
  dokumentId: string,
  progress: number,
) {
  return dokumente.map((d) => {
    if (d.file.id === dokumentId) {
      return { ...d, progress };
    }
    return d;
  });
}

export function toHumanReadableError(
  name: string,
  allowTypes: string,
  status?: number,
) {
  return status === 400
    ? {
        translationKey: FORMAT_ERROR,
        values: {
          file: name,
          formats: allowTypes
            .split(',')
            .map((f) => '.' + f.split('/')[1])
            .join(', '),
        },
      }
    : { translationKey: GENERIC_ERROR };
}

export const UPLOAD_THEME_MAP = {
  error: {
    icon: 'warning',
    ...DOKUMENT_THEMES.danger,
  },
  deleting: {
    icon: 'sync',
    ...DOKUMENT_THEMES.info,
  },
  uploading: {
    icon: 'sync',
    ...DOKUMENT_THEMES.info,
  },
  done: {
    icon: 'check',
    ...DOKUMENT_THEMES.success,
  },
} satisfies Record<DokumentView['state'], unknown>;

export const checkDocumentState = (
  document: DokumentUpload,
): DokumentView['state'] => {
  if (document.error) {
    return 'error';
  }
  if (document.deleting) {
    return 'deleting';
  }
  if (document.progress === 100) {
    return 'done';
  }

  return 'uploading';
};

export const notCompletedOrError = (document: DokumentUpload) => {
  return document.progress !== 100 || document.error;
};

export const createFakeProgress$ = <T>(
  upload$: Observable<HttpEvent<T>>,
  action: { fileUpload: File },
) => {
  return combineLatest([upload$, interval(PROGRESS_ANIMATION_TIME)]).pipe(
    takeWhile(([event]) => event.type !== HttpEventType.Response),
    map(([, i]) =>
      createHttpEvent({
        type: HttpEventType.UploadProgress,
        loaded: action.fileUpload.size - action.fileUpload.size / (i + 1),
        total: action.fileUpload.size,
      }),
    ),
  );
};
