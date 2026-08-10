import { DestroyRef, Injectable, computed, inject } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';

import { GesuchStore } from '@dv/sachbearbeitung-app/data-access/gesuch';
import { SachbearbeitungAppUiGrundAuswahlDialogComponent } from '@dv/sachbearbeitung-app/ui/grund-auswahl-dialog';
import { selectSharedDataAccessConfigsView } from '@dv/shared/data-access/config';
import { EinreichenStore } from '@dv/shared/data-access/einreichen';
import { assertUnreachable, lowercased } from '@dv/shared/model/type-util';
import { SharedUiKommentarDialogComponent } from '@dv/shared/ui/kommentar-dialog';
import { StatusUebergang } from '@dv/shared/util/gesuch';
import { isPending } from '@dv/shared/util/remote-data';

@Injectable({
  providedIn: 'root',
})
export class GesuchActionsService {
  private router = inject(Router);
  private store = inject(Store);
  private gesuchStore = inject(GesuchStore);
  private einreichenStore = inject(EinreichenStore);
  private dialog = inject(MatDialog);
  private destroyRef = inject(DestroyRef);
  private deploymentConfigSig = this.store.selectSignal(
    selectSharedDataAccessConfigsView,
  );
  isLoadingSig = computed(() => isPending(this.gesuchStore.lastStatusChange()));

  setStatusUebergang(
    nextStatus: StatusUebergang,
    gesuchId: string,
    gesuchTrancheId: string,
  ) {
    switch (nextStatus) {
      case 'SET_TO_BEARBEITUNG':
        this.gesuchStore.setStatus$[nextStatus]({
          gesuchTrancheId,
          onSuccess: () => {
            this.einreichenStore.validateSteps$({ gesuchTrancheId });
          },
        });
        break;
      case 'ANSPRUCH_PRUEFEN':
      case 'BEARBEITUNG_ABSCHLIESSEN':
      case 'STATUS_PRUEFUNG_AUSLOESEN':
        this.gesuchStore.setStatus$[nextStatus]({
          gesuchTrancheId,
          onSuccess: () => {
            this.einreichenStore.validateSteps$({ gesuchTrancheId });
          },
        });
        break;
      case 'BEREIT_FUER_BEARBEITUNG':
      case 'VERFUEGT':
      case 'VERSENDET':
        this.gesuchStore.setStatus$[nextStatus]({ gesuchTrancheId });
        break;
      case 'BEREIT_FUER_BEARBEITUNG_AS_AENDERUNG':
        SharedUiKommentarDialogComponent.open(this.dialog, {
          titleKey:
            'shared.header.status-uebergang.BEREIT_FUER_BEARBEITUNG_AS_AENDERUNG.title',
          messageKey:
            'shared.header.status-uebergang.BEREIT_FUER_BEARBEITUNG_AS_AENDERUNG.message',
          placeholderKey:
            'shared.header.status-uebergang.BEREIT_FUER_BEARBEITUNG_AS_AENDERUNG.placeholder',
          confirmKey: 'shared.ui.yes',
        })
          .afterClosed()
          .pipe(takeUntilDestroyed(this.destroyRef))
          .subscribe((result) => {
            if (result) {
              this.gesuchStore.setStatus$.BEREIT_FUER_BEARBEITUNG_AS_AENDERUNG({
                gesuchTrancheId,
                text: result.kommentar,
              });
            }
          });
        break;
      case 'SET_TO_DATENSCHUTZBRIEF_DRUCKBEREIT':
        SharedUiKommentarDialogComponent.open(this.dialog, {
          titleKey: `shared.header.status-uebergang.SET_TO_DATENSCHUTZBRIEF_DRUCKBEREIT.title`,
          messageKey: `shared.header.status-uebergang.SET_TO_DATENSCHUTZBRIEF_DRUCKBEREIT.message`,
          placeholderKey: `shared.header.status-uebergang.SET_TO_DATENSCHUTZBRIEF_DRUCKBEREIT.placeholder`,
          confirmKey: `shared.header.status-uebergang.SET_TO_DATENSCHUTZBRIEF_DRUCKBEREIT.confirm`,
        })
          .afterClosed()
          .pipe(takeUntilDestroyed(this.destroyRef))
          .subscribe((result) => {
            if (result) {
              this.gesuchStore.setStatus$.SET_TO_DATENSCHUTZBRIEF_DRUCKBEREIT({
                gesuchTrancheId,
                text: result.kommentar,
              });
            }
          });
        break;
      case 'ZURUECK_ZU_BEREIT_FUER_BEARBEITUNG':
        SharedUiKommentarDialogComponent.openOptional(this.dialog, {
          titleKey: `shared.header.status-uebergang.BEREIT_FUER_BEARBEITUNG.title`,
          messageKey: `shared.header.status-uebergang.BEREIT_FUER_BEARBEITUNG.message`,
          placeholderKey: `shared.header.status-uebergang.BEREIT_FUER_BEARBEITUNG.placeholder`,
          confirmKey: `shared.header.status-uebergang.BEREIT_FUER_BEARBEITUNG.confirm`,
        })
          .afterClosed()
          .pipe(takeUntilDestroyed(this.destroyRef))
          .subscribe((result) => {
            if (result) {
              this.gesuchStore.setStatus$.ZURUECK_ZU_BEREIT_FUER_BEARBEITUNG({
                gesuchTrancheId,
                text: result.kommentar,
              });
            }
          });
        break;
      case 'ZURUECKWEISEN_OR_UNDO':
        SharedUiKommentarDialogComponent.open(this.dialog, {
          titleKey: `shared.header.status-uebergang.ZURUECKWEISEN.title`,
          messageKey: `shared.header.status-uebergang.ZURUECKWEISEN.message`,
          placeholderKey: `shared.header.status-uebergang.ZURUECKWEISEN.placeholder`,
          confirmKey: `shared.header.status-uebergang.ZURUECKWEISEN.confirm`,
        })
          .afterClosed()
          .pipe(takeUntilDestroyed(this.destroyRef))
          .subscribe((result) => {
            if (result) {
              this.gesuchStore.setStatus$.ZURUECKWEISEN_OR_UNDO({
                gesuchTrancheId,
                text: result.kommentar,
                onSuccess: (newGesuchTrancheId, trancheTyp) => {
                  this.router.navigate([
                    'gesuch',
                    'info',
                    gesuchId,
                    lowercased(trancheTyp),
                    newGesuchTrancheId,
                  ]);
                  this.einreichenStore.validateSteps$({ gesuchTrancheId });
                },
              });
            }
          });
        break;
      case 'NEGATIVE_VERFUEGUNG_ERSTELLEN':
        SachbearbeitungAppUiGrundAuswahlDialogComponent.open(this.dialog, {
          titleKey: `shared.header.status-uebergang.${nextStatus}.title`,
          labelKey: `shared.header.status-uebergang.${nextStatus}.label`,
          messageKey: `shared.header.status-uebergang.${nextStatus}.message`,
          confirmKey: `shared.header.status-uebergang.${nextStatus}.confirm`,
          allowedTypes:
            this.deploymentConfigSig().deploymentConfig?.allowedMimeTypes,
        })
          .afterClosed()
          .pipe(takeUntilDestroyed(this.destroyRef))
          .subscribe((result) => {
            if (result) {
              switch (result.type) {
                case 'manuell': {
                  this.gesuchStore.createManuelleVerfuegung$({
                    gesuchTrancheId,
                    fileUpload: result.verfuegungUpload,
                    kommentar: result.kommentar,
                  });
                  break;
                }
                case 'grund': {
                  this.gesuchStore.setStatus$[nextStatus]({
                    gesuchTrancheId,
                    grundId: result.entityId,
                    kanton: result.kanton,
                  });
                  break;
                }
                default:
                  assertUnreachable(result);
              }
            }
          });
        break;
      default:
        assertUnreachable(nextStatus);
    }
  }
}
