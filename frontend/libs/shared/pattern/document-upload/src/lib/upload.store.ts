import { HttpEventType } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { patchState, signalState } from '@ngrx/signals';
import { Subject, merge, of, throwError } from 'rxjs';
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

import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import {
  DokumentListView,
  DokumentOptions,
  DokumentState,
  SharedModelAdditionalGesuchDokument,
  SharedModelCustomGesuchDokument,
  SharedModelDarlehenDokument,
  SharedModelStandardGesuchDokument,
} from '@dv/shared/model/dokument';
import {
  DarlehenService,
  Dokument,
  DokumentService,
  DokumentTyp,
} from '@dv/shared/model/gesuch';
import { byAppType } from '@dv/shared/model/permission-state';
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

type ServiceDefaultParams = readonly [
  'events',
  undefined,
  {
    readonly context: ReturnType<typeof noGlobalErrorsIf>;
  },
];

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
      .some((d) => (!d.progress || d.progress < 100 || d.deleting) && !d.error);
  });

  /**
   * The documents in the state, enriched with a state and a theme
   */
  dokumentListView = computed<DokumentListView>(() => {
    const { dokumentModel: gesuchDokument, dokuments } = this.state();
    return {
      gesuchDokument,
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
  private darlehenService = inject(DarlehenService);
  private config = inject(SharedModelCompileTimeConfig);
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

  private getRequiredGesuchDokumenteByAppType(params: {
    dokumentTyp: DokumentTyp;
    gesuchTrancheId: string;
  }) {
    return byAppType(this.config.appType, {
      'gesuch-app': () =>
        this.documentService.getGesuchDokumentForTypGS$(params),
      'sachbearbeitung-app': () =>
        this.documentService.getGesuchDokumentForTypSB$(params),
      'demo-data-app': () =>
        throwError(() => new Error('Not implemented for this AppType')),
    });
  }

  private getCustomGesuchDokumenteByAppType$(params: {
    customDokumentTypId: string;
  }) {
    return byAppType(this.config.appType, {
      'gesuch-app': () =>
        this.documentService.getCustomGesuchDokumentForTypGS$(params),
      'sachbearbeitung-app': () =>
        this.documentService.getCustomGesuchDokumentForTypSB$(params),
      'demo-data-app': () =>
        throwError(() => new Error('Not implemented for this AppType')),
    });
  }

  private deleteDokumentByAppType$(dokumentId: string, ignoreErrors?: boolean) {
    const deleteCallParams = [
      { dokumentId },
      undefined,
      undefined,
      {
        context: shouldIgnoreErrorsIf(!!ignoreErrors),
      },
    ] as const;

    return byAppType(this.config.appType, {
      'gesuch-app': () =>
        this.documentService.deleteDokumentGS$(...deleteCallParams),
      'sachbearbeitung-app': () =>
        this.documentService.deleteDokumentSB$(...deleteCallParams),
      'demo-data-app': () =>
        throwError(() => new Error('Not implemented for this AppType')),
    });
  }

  private createDokumentByAppType$(
    fileUpload: File,
    dokument: SharedModelStandardGesuchDokument,
    serviceDefaultParams: ServiceDefaultParams,
  ) {
    return byAppType(this.config.appType, {
      'gesuch-app': () =>
        this.documentService.createDokumentGS$(
          {
            fileUpload,
            gesuchTrancheId: dokument.trancheId,
            dokumentTyp: dokument.dokumentTyp,
          },
          ...serviceDefaultParams,
        ),
      'sachbearbeitung-app': () =>
        this.documentService.createDokumentSB$(
          {
            fileUpload,
            gesuchTrancheId: dokument.trancheId,
            dokumentTyp: dokument.dokumentTyp,
          },
          ...serviceDefaultParams,
        ),
      'demo-data-app': () =>
        throwError(() => new Error('Not implemented for this AppType')),
    });
  }

  private createCustomDokumentByAppType$(
    fileUpload: File,
    dokument: SharedModelCustomGesuchDokument,
    serviceDefaultParams: ServiceDefaultParams,
  ) {
    return byAppType(this.config.appType, {
      'gesuch-app': () =>
        this.documentService.uploadCustomGesuchDokumentGS$(
          {
            fileUpload,
            customDokumentTypId: dokument.dokumentTyp.id,
          },
          ...serviceDefaultParams,
        ),
      'sachbearbeitung-app': () =>
        this.documentService.uploadCustomGesuchDokumentSB$(
          {
            fileUpload,
            customDokumentTypId: dokument.dokumentTyp.id,
          },
          ...serviceDefaultParams,
        ),
      'demo-data-app': () =>
        throwError(() => new Error('Not implemented for this AppType')),
    });
  }

  constructor() {
    this.loadDocuments$
      .pipe(
        exhaustMap((options) =>
          (() => {
            const dokument = options.dokument;
            switch (dokument.art) {
              case 'DARLEHEN_DOKUMENT': {
                return this.darlehenService
                  .getDarlehenDokument$({
                    darlehenId: dokument.darlehenId,
                    dokumentType: dokument.dokumentTyp,
                  })
                  .pipe(
                    map(
                      ({ value }) =>
                        ({
                          art: 'DARLEHEN_DOKUMENT',
                          gesuchDokument: value,
                          dokumentTyp: dokument.dokumentTyp,
                          darlehenId: dokument.darlehenId,
                          permissions: dokument.permissions,
                        }) satisfies SharedModelDarlehenDokument,
                    ),
                  );
              }
              case 'GESUCH_DOKUMENT': {
                return this.getRequiredGesuchDokumenteByAppType({
                  dokumentTyp: dokument.dokumentTyp,
                  gesuchTrancheId: dokument.trancheId,
                }).pipe(
                  map(
                    ({ value }) =>
                      ({
                        art: 'GESUCH_DOKUMENT',
                        gesuchDokument: value,
                        dokumentTyp: dokument.dokumentTyp,
                        trancheId: dokument.trancheId,
                        permissions: dokument.permissions,
                      }) satisfies SharedModelStandardGesuchDokument,
                  ),
                );
              }
              case 'CUSTOM_DOKUMENT':
                return this.getCustomGesuchDokumenteByAppType$({
                  customDokumentTypId: dokument.dokumentTyp.id,
                }).pipe(
                  map(
                    ({ value }) =>
                      ({
                        art: 'CUSTOM_DOKUMENT',
                        gesuchDokument: value,
                        dokumentTyp: dokument.dokumentTyp,
                        gesuchId: dokument.gesuchId,
                        trancheId: dokument.trancheId,
                        permissions: dokument.permissions,
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
                          permissions: dokument.permissions,
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
          patchState(this.state, (state) => ({
            dokumentModel,
            dokuments: [
              ...(dokumentModel.gesuchDokument?.dokumente.map((file) => ({
                file,
                progress: 100,
              })) ?? []),
              ...state.dokuments.filter(notCompletedOrError),
            ],
          })),
      });

    this.removeDocument$
      .pipe(
        tap((action) => {
          patchState(this.state, (state) => ({
            dokuments: state.dokuments.map((dokument) => {
              if (dokument.file.id === action.dokumentId) {
                return {
                  ...dokument,
                  deleting: true,
                };
              }
              return dokument;
            }),
          }));
        }),
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
                return this.deleteDokumentByAppType$(
                  action.dokumentId,
                  !!dokumentToDelete?.error,
                );
              case 'DARLEHEN_DOKUMENT':
                return this.darlehenService.deleteDarlehenDokument$(
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
          return serviceCall$.pipe(map(() => action));
        }),
        takeUntilDestroyed(),
      )
      .subscribe({
        error: () => {
          patchState(this.state, createGenericError());
        },
        next: (action) => {
          patchState(this.state, (state) => ({
            dokuments: state.dokuments.filter(
              ({ file }) => file.id !== action.dokumentId,
            ),
          }));
          this.documentChangedSig.set({ hasChanged: true });
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
            const parsedError = sharedUtilFnErrorTransformer(event.error);
            const status = parsedError.status;
            patchState(this.state, (state) => ({
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
            }));
            return;
          }
          switch (event.type) {
            case HttpEventType.Sent: {
              patchState(this.state, (state) => ({
                dokuments: [
                  ...state.dokuments,
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
              }));
              break;
            }
            case HttpEventType.UploadProgress: {
              patchState(this.state, (state) => ({
                dokuments: updateProgressFor(
                  state.dokuments,
                  tempDokumentId,
                  (event.loaded / action.fileUpload.size) * 100,
                ),
              }));
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
              patchState(this.state, (state) => ({
                dokuments: updateProgressFor(
                  state.dokuments,
                  tempDokumentId,
                  100,
                ),
              }));
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

    const { fileUpload, dokument } = action;

    const serviceDefaultParams = [
      'events',
      undefined,
      {
        context: noGlobalErrorsIf(true),
      },
    ] as const;

    const uploadByType = () => {
      switch (dokument.art) {
        case 'DARLEHEN_DOKUMENT':
          return this.darlehenService.createDarlehenDokument$(
            {
              fileUpload,
              darlehenId: dokument.darlehenId,
              dokumentType: dokument.dokumentTyp,
            },
            ...serviceDefaultParams,
          );
        case 'GESUCH_DOKUMENT':
          return this.createDokumentByAppType$(
            fileUpload,
            dokument,
            serviceDefaultParams,
          );
        case 'CUSTOM_DOKUMENT':
          return this.createCustomDokumentByAppType$(
            fileUpload,
            dokument,
            serviceDefaultParams,
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
