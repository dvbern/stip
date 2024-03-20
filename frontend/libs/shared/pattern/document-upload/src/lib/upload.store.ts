import { HttpEventType } from '@angular/common/http';
import { Injectable, computed, inject } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { patchState, signalState } from '@ngrx/signals';
import { Subject, merge, of } from 'rxjs';
import {
  catchError,
  filter,
  map,
  mergeMap,
  shareReplay,
  switchMap,
  take,
  takeUntil,
} from 'rxjs/operators';

import { GesuchService } from '@dv/shared/model/gesuch';
import { shouldIgnoreErrorsIf } from '@dv/shared/util/http';
import { sharedUtilFnErrorTransformer } from '@dv/shared/util-fn/error-transformer';

import {
  UPLOAD_THEME_MAP,
  checkDocumentState,
  createFakeProgress$,
  createGenericError,
  createHttpEvent,
  createTempId,
  notCompletedOrError,
  toHumanReadableError,
  updateProgressFor,
} from './helpers/store';
import { DocumentOptions, DocumentState, DocumentView } from './upload.model';

@Injectable()
export class UploadStore {
  readonly state = signalState<DocumentState>({
    documents: [],
    errorKey: undefined,
    // TODO: load allowed formats from the backend
    allowedFormats: 'image/tiff,image/jpeg,image/png,application/pdf',
  });

  /**
   * True if there are any entries in the documents array
   */
  hasEntriesSig = computed(() => {
    return this.state.documents().length > 0;
  });

  /**
   * True if there are any documents in the state that are still uploading
   */
  isLoading = computed(() => {
    return this.state
      .documents()
      .some((d) => !d.progress || d.progress < 100 || d.error);
  });

  /**
   * The documents in the state, enriched with a state and a theme
   */
  documentsView = computed<DocumentView[]>(() => {
    return this.state.documents().map((document) => {
      const state = checkDocumentState(document);

      return {
        ...document,
        state,
        theme: UPLOAD_THEME_MAP[state],
      };
    });
  });

  private documentService = inject(GesuchService);
  private loadDocuments$ = new Subject<DocumentOptions>();
  private removeDocument$ = new Subject<
    { dokumentId: string } & DocumentOptions
  >();
  private uploadDocument$ = new Subject<
    {
      fileUpload: File;
    } & DocumentOptions
  >();
  private cancelDocumentUpload$ = new Subject<
    {
      dokumentId: string;
    } & DocumentOptions
  >();

  constructor() {
    this.loadDocuments$
      .pipe(
        switchMap((options) =>
          this.documentService.getDokumenteForTyp$(options),
        ),
        takeUntilDestroyed(),
      )
      .subscribe({
        error: () => {
          patchState(this.state, createGenericError());
        },
        next: (documents) =>
          patchState(this.state, {
            documents: [
              ...documents.map((file) => ({ file, progress: 100 })),
              ...this.state.documents().filter(notCompletedOrError),
            ],
          }),
      });

    this.removeDocument$
      .pipe(
        mergeMap((action) => {
          // Ignore http errors if the upload is already in error state
          const hasError = !!this.state
            .documents()
            .find((d) => d.file.id === action.dokumentId)?.error;
          return this.documentService
            .deleteDokument$(action, undefined, undefined, {
              context: shouldIgnoreErrorsIf(hasError),
            })
            .pipe(map(() => action));
        }),
        takeUntilDestroyed(),
      )
      .subscribe({
        error: () => {
          patchState(this.state, createGenericError());
        },
        next: (action) => {
          patchState(this.state, {
            documents: this.state
              .documents()
              .filter(({ file }) => file.id !== action.dokumentId),
          });
        },
      });

    this.uploadDocument$
      .pipe(
        mergeMap((action) => this.handleUpload$(action)),
        takeUntilDestroyed(),
      )
      .subscribe({
        error: () => {
          patchState(this.state, createGenericError());
        },
        next: ({ event, action, tempDokumentId }) => {
          if ('error' in event) {
            const state = this.state();
            const parsedError = sharedUtilFnErrorTransformer(event.error);
            const status = parsedError.status;
            patchState(this.state, {
              documents: state.documents.map((d) =>
                d.file.id === createTempId(action.fileUpload)
                  ? {
                      file: d.file,
                      error: toHumanReadableError(
                        action.fileUpload.name,
                        state,
                        status,
                      ),
                      progress: 100,
                    }
                  : d,
              ),
            });
            return;
          }
          switch (event.type) {
            case HttpEventType.Sent: {
              patchState(this.state, {
                documents: [
                  ...this.state.documents(),
                  {
                    file: {
                      id: tempDokumentId,
                      filename: action.fileUpload.name,
                      filesize: action.fileUpload.size.toString(),
                      filepfad: '',
                      objectId: '',
                      timestampErstellt: new Date().toISOString(),
                    },
                    progress: 0,
                  },
                ],
              });
              break;
            }
            case HttpEventType.UploadProgress: {
              patchState(this.state, {
                documents: updateProgressFor(
                  this.state.documents(),
                  tempDokumentId,
                  (event.loaded / action.fileUpload.size) * 100,
                ),
              });
              break;
            }
            case HttpEventType.User: {
              patchState(this.state, {
                documents: this.state
                  .documents()
                  .filter((d) => d.file.id !== tempDokumentId),
              });
              break;
            }
            case HttpEventType.Response: {
              patchState(this.state, {
                documents: updateProgressFor(
                  this.state.documents(),
                  tempDokumentId,
                  100,
                ),
              });
              this.loadDocuments(action);
              break;
            }
          }
        },
      });
  }

  /**
   * Load the documents with the given options
   */
  loadDocuments(options: DocumentOptions) {
    this.loadDocuments$.next(options);
  }

  /**
   * Upload a document
   */
  uploadDocument(fileUpload: File, options: DocumentOptions) {
    this.uploadDocument$.next({ fileUpload, ...options });
  }

  /**
   * Cancel the upload of a document determined by the given options
   */
  cancelDocumentUpload(dokumentId: string, options: DocumentOptions) {
    this.cancelDocumentUpload$.next({ dokumentId, ...options });
  }

  /**
   * Remove a document by ID using the given options
   */
  removeDocument(dokumentId: string, options: DocumentOptions) {
    this.removeDocument$.next({ dokumentId, ...options });
  }

  /**
   * Upload a document and add a fake progress stream.
   * On error, the stream will continue with an event that contains the error.
   */
  private handleUpload$(action: { fileUpload: File } & DocumentOptions) {
    const tempDokumentId = createTempId(action.fileUpload);
    // Prepare a cancel stream for this document upload
    const cancellingThisDocument$ = this.cancelDocumentUpload$.pipe(
      filter((d) => d.dokumentId === tempDokumentId),
      take(1),
      shareReplay({ bufferSize: 1, refCount: true }),
    );
    const upload$ = this.documentService
      .createDokument$(action, 'events')
      .pipe(shareReplay({ bufferSize: 1, refCount: true }));
    // Merge the upload stream with a fake progress stream
    const uploading$ = merge(
      upload$,
      createFakeProgress$(upload$, action),
    ).pipe(
      // Cancel the upload if the cancel stream emits
      takeUntil(cancellingThisDocument$),
    );

    return merge(
      uploading$,
      // On cancel, emit a User event to signal the upload was cancelled
      cancellingThisDocument$.pipe(
        map(() =>
          createHttpEvent({
            type: HttpEventType.User,
          }),
        ),
      ),
    ).pipe(
      map((event) => ({ event, action, tempDokumentId })),
      // On error, emit an event with the error
      catchError((error) =>
        of({
          event: { error },
          action,
          tempDokumentId,
        }),
      ),
    );
  }
}
