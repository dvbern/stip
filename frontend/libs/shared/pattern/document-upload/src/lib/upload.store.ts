import { HttpEventType } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { patchState, signalState } from '@ngrx/signals';
import { Subject, merge, of } from 'rxjs';
import {
  catchError,
  exhaustMap,
  filter,
  map,
  mergeMap,
  shareReplay,
  take,
  takeUntil,
  tap,
} from 'rxjs/operators';

import {
  DokumentListView,
  DokumentOptions,
  DokumentState,
  SharedModelAdditionalGesuchDokument,
  SharedModelCustomGesuchDokument,
  SharedModelStandardGesuchDokument,
} from '@dv/shared/model/dokument';
import { Dokument, DokumentService } from '@dv/shared/model/gesuch';
import { assertUnreachable, isDefined } from '@dv/shared/model/type-util';
import { noGlobalErrorsIf, shouldIgnoreErrorsIf } from '@dv/shared/util/http';
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
} from './helpers/upload';

@Injectable()
export class UploadStore {
  readonly state = signalState<DokumentState>({
    dokumentModel: undefined,
    dokuments: [],
    errorKey: undefined,
  });

  /**
   * True if there are any entries in the documents array
   */
  hasEntriesSig = computed(() => {
    return this.state.dokuments().length > 0;
  });

  public documentChangedSig = signal({ hasChanged: false });

  /**
   * True if there are any documents in the state that are still uploading
   */
  isLoading = computed(() => {
    return this.state
      .dokuments()
      .some((d) => (!d.progress || d.progress < 100) && !d.error);
  });

  /**
   * The documents in the state, enriched with a state and a theme
   */
  dokumentListView = computed<DokumentListView>(() => {
    const { dokumentModel, dokuments } = this.state();
    return {
      dokumentModel,
      dokuments: dokuments.map((document) => {
        const state = checkDocumentState(document);

        return {
          ...document,
          state,
          theme: UPLOAD_THEME_MAP[state],
        };
      }),
    };
  });

  private documentService = inject(DokumentService);
  private loadDocuments$ = new Subject<DokumentOptions>();
  private removeDocument$ = new Subject<
    { dokumentId: string } & DokumentOptions
  >();
  private uploadDocument$ = new Subject<
    {
      fileUpload: File;
    } & DokumentOptions
  >();
  private cancelDocumentUpload$ = new Subject<
    {
      dokumentId: string;
    } & DokumentOptions
  >();

  constructor() {
    this.loadDocuments$
      .pipe(
        exhaustMap((options) =>
          (() => {
            const dokument = options.dokument;
            switch (dokument.art) {
              case 'GESUCH_DOKUMENT': {
                return this.documentService
                  .getGesuchDokumenteForTyp$({
                    dokumentTyp: dokument.dokumentTyp,
                    gesuchTrancheId: dokument.trancheId,
                  })
                  .pipe(
                    map(
                      ({ value }) =>
                        ({
                          art: 'GESUCH_DOKUMENT',
                          gesuchDokument: value,
                          dokumentTyp: dokument.dokumentTyp,
                          trancheId: dokument.trancheId,
                        }) satisfies SharedModelStandardGesuchDokument,
                    ),
                  );
              }
              case 'CUSTOM_DOKUMENT':
                return this.documentService
                  .getCustomGesuchDokumenteForTyp$({
                    // eslint-disable-next-line @typescript-eslint/no-non-null-assertion
                    customDokumentTypId: dokument.dokumentTyp.id!,
                    gesuchTrancheId: dokument.trancheId,
                  })
                  .pipe(
                    map(
                      ({ value }) =>
                        ({
                          art: 'CUSTOM_DOKUMENT',
                          gesuchDokument: value,
                          dokumentTyp: dokument.dokumentTyp,
                          gesuchId: dokument.gesuchId,
                          trancheId: dokument.trancheId,
                        }) satisfies SharedModelCustomGesuchDokument,
                    ),
                  );
              case 'UNTERSCHRIFTENBLATT':
                return this.documentService
                  .getUnterschriftenblaetterForGesuch$({
                    gesuchId: dokument.gesuchId,
                  })
                  .pipe(
                    map((list) =>
                      list.find((d) => d.dokumentTyp === dokument.dokumentTyp),
                    ),
                    filter(isDefined),
                    map(
                      (gesuchDokument) =>
                        ({
                          art: 'UNTERSCHRIFTENBLATT',
                          gesuchDokument,
                          dokumentTyp: dokument.dokumentTyp,
                          gesuchId: dokument.gesuchId,
                          trancheId: dokument.trancheId,
                        }) satisfies SharedModelAdditionalGesuchDokument,
                    ),
                  );
              default:
                assertUnreachable(dokument);
            }
          })(),
        ),
        takeUntilDestroyed(),
      )
      .subscribe({
        error: () => {
          patchState(this.state, createGenericError());
        },
        next: (dokumentModel) =>
          patchState(this.state, {
            dokumentModel,
            dokuments: [
              ...(dokumentModel.gesuchDokument?.dokumente.map((file) => ({
                file,
                progress: 100,
              })) ?? []),
              ...this.state.dokuments().filter(notCompletedOrError),
            ],
          }),
      });

    this.removeDocument$
      .pipe(
        mergeMap((action) => {
          const dokumentToDelete = this.state
            .dokuments()
            .find((d) => d.file.id === action.dokumentId);
          if (dokumentToDelete?.isTemporary) {
            return of(action);
          }
          const deleteCallParams = [
            {
              dokumentId: action.dokumentId,
            },
            undefined,
            undefined,
            {
              // Ignore http errors if the upload is already in error state
              context: shouldIgnoreErrorsIf(!!dokumentToDelete?.error),
            },
          ] as const;
          const serviceCall$ = (() => {
            switch (action.dokument.art) {
              case 'CUSTOM_DOKUMENT':
              case 'GESUCH_DOKUMENT':
                return this.documentService.deleteDokument$(
                  ...deleteCallParams,
                );
              case 'UNTERSCHRIFTENBLATT':
                return this.documentService.deleteUnterschriftenblattDokument$(
                  ...deleteCallParams,
                );
              default:
                assertUnreachable(action.dokument);
            }
          })();
          return serviceCall$.pipe(
            map(() => action),
            tap(() => this.documentChangedSig.set({ hasChanged: true })),
          );
        }),
        takeUntilDestroyed(),
      )
      .subscribe({
        error: () => {
          patchState(this.state, createGenericError());
        },
        next: (action) => {
          patchState(this.state, {
            dokuments: this.state
              .dokuments()
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
              dokuments: state.dokuments.map((d) =>
                d.file.id === createTempId(action.fileUpload)
                  ? {
                      ...d,
                      error: toHumanReadableError(
                        action.fileUpload.name,
                        action.allowTypes,
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
                dokuments: [
                  ...this.state.dokuments(),
                  {
                    file: {
                      id: tempDokumentId,
                      filename: action.fileUpload.name,
                      filesize: action.fileUpload.size.toString(),
                      filepath: '',
                      objectId: '',
                      timestampErstellt: new Date().toISOString(),
                    },
                    isTemporary: true,
                    progress: 0,
                  },
                ],
              });
              break;
            }
            case HttpEventType.UploadProgress: {
              patchState(this.state, {
                dokuments: updateProgressFor(
                  this.state.dokuments(),
                  tempDokumentId,
                  (event.loaded / action.fileUpload.size) * 100,
                ),
              });
              break;
            }
            // On cancel (User event), remove the temporary document from the state
            case HttpEventType.User: {
              patchState(this.state, {
                dokuments: this.state
                  .dokuments()
                  .filter((d) => d.file.id !== tempDokumentId),
              });
              break;
            }
            case HttpEventType.Response: {
              patchState(this.state, {
                dokuments: updateProgressFor(
                  this.state.dokuments(),
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

  setInitialDocuments(documents: Dokument[]) {
    patchState(this.state, {
      dokuments: documents.map((file) => ({ file, progress: 100 })),
    });
  }

  /**
   * Load the documents with the given options
   */
  loadDocuments(options: DokumentOptions) {
    this.loadDocuments$.next(options);
  }

  /**
   * Upload a document
   */
  uploadDocument(fileUpload: File, options: DokumentOptions) {
    this.uploadDocument$.next({ fileUpload, ...options });
  }

  /**
   * Cancel the upload of a document determined by the given options
   */
  cancelDocumentUpload(dokumentId: string, options: DokumentOptions) {
    this.cancelDocumentUpload$.next({ dokumentId, ...options });
  }

  /**
   * Remove a document by ID using the given options
   */
  removeDocument(dokumentId: string, options: DokumentOptions) {
    this.removeDocument$.next({ dokumentId, ...options });
  }

  /**
   * Upload a document and add a fake progress stream.
   * On error, the stream will continue with an event that contains the error.
   */
  private handleUpload$(action: { fileUpload: File } & DokumentOptions) {
    const tempDokumentId = createTempId(action.fileUpload);

    // Prepare a cancel stream for this document upload
    const cancellingThisDocument$ = this.cancelDocumentUpload$.pipe(
      filter((d) => d.dokumentId === tempDokumentId),
      take(1),
      shareReplay({ bufferSize: 1, refCount: true }),
    );
    const dokument = action.dokument;
    const serviceDefaultParams = [
      'events',
      undefined,
      {
        context: noGlobalErrorsIf(true),
      },
    ] as const;

    const uploadByType = () => {
      switch (dokument.art) {
        case 'GESUCH_DOKUMENT':
          return this.documentService.createDokument$(
            {
              ...action,
              gesuchTrancheId: dokument.trancheId,
              dokumentTyp: dokument.dokumentTyp,
            },
            ...serviceDefaultParams,
          );
        case 'CUSTOM_DOKUMENT':
          return this.documentService.createCustomGesuchDokument$(
            {
              ...action,
              gesuchTrancheId: dokument.trancheId,
              // eslint-disable-next-line @typescript-eslint/no-non-null-assertion
              customDokumentTypId: dokument.dokumentTyp.id!,
            },
            ...serviceDefaultParams,
          );
        case 'UNTERSCHRIFTENBLATT':
          return this.documentService.createUnterschriftenblatt$(
            {
              ...action,
              gesuchId: dokument.gesuchId,
              unterschriftenblattTyp: dokument.dokumentTyp,
            },
            ...serviceDefaultParams,
          );
        default:
          assertUnreachable(dokument);
      }
    };
    const upload$ = uploadByType().pipe(
      shareReplay({ bufferSize: 1, refCount: true }),
    );

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
      tap(({ event }) => {
        if (event.type === HttpEventType.Response) {
          this.documentChangedSig.set({ hasChanged: true });
        }
      }),
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
