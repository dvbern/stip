import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  EventEmitter,
  Output,
  computed,
  effect,
  inject,
  input,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MatDialog } from '@angular/material/dialog';
import { MatMenuModule } from '@angular/material/menu';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslatePipe } from '@ngx-translate/core';

import { GesuchStore } from '@dv/sachbearbeitung-app/data-access/gesuch';
import { SachbearbeitungAppUiGrundAuswahlDialogComponent } from '@dv/sachbearbeitung-app/ui/grund-auswahl-dialog';
import { DokumentsStore } from '@dv/shared/data-access/dokuments';
import { GesuchAenderungStore } from '@dv/shared/data-access/gesuch-aenderung';
import { SharedModelGesuch } from '@dv/shared/model/gesuch';
import { PermissionMap } from '@dv/shared/model/permission-state';
import { assertUnreachable } from '@dv/shared/model/type-util';
import {
  SharedPatternAppHeaderComponent,
  SharedPatternAppHeaderPartsDirective,
} from '@dv/shared/pattern/app-header';
import { SharedUiAenderungMeldenDialogComponent } from '@dv/shared/ui/aenderung-melden-dialog';
import { SharedUiKommentarDialogComponent } from '@dv/shared/ui/kommentar-dialog';
import {
  StatusUebergaengeMap,
  StatusUebergaengeOptions,
  StatusUebergang,
} from '@dv/shared/util/gesuch';

@Component({
  selector: 'dv-sachbearbeitung-app-pattern-gesuch-header',
  standalone: true,
  imports: [
    CommonModule,
    TranslatePipe,
    RouterLink,
    RouterLinkActive,
    MatMenuModule,
    SharedPatternAppHeaderComponent,
    SharedPatternAppHeaderPartsDirective,
  ],
  providers: [GesuchStore],
  templateUrl: './sachbearbeitung-app-pattern-gesuch-header.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppPatternGesuchHeaderComponent {
  currentGesuchSig = input.required<SharedModelGesuch | null>({
    alias: 'currentGesuch',
  });
  gesuchPermissionsSig = input.required<PermissionMap>({
    alias: 'gesuchPermissions',
  });
  isLoadingSig = input.required<boolean>({ alias: 'isLoading' });

  private store = inject(Store);
  private router = inject(Router);
  private destroyRef = inject(DestroyRef);
  private dialog = inject(MatDialog);
  private dokumentsStore = inject(DokumentsStore);
  private gesuchStore = inject(GesuchStore);
  gesuchAenderungStore = inject(GesuchAenderungStore);

  @Output() openSidenav = new EventEmitter<void>();

  private hasAcceptedAllDocumentsSig = computed(() => {
    const gesuchStatus = this.currentGesuchSig()?.gesuchStatus;
    if (!gesuchStatus) {
      return false;
    }

    return this.dokumentsStore.hasAcceptedAllDokumentsSig();
  });

  /** Extract the gesuch id from the view to prevent unecessary signal updates */
  private currentGesuchIdSig = computed(() => {
    return this.currentGesuchSig()?.id;
  });

  isTrancheRouteSig = computed(() => {
    const gesuch = this.currentGesuchSig();
    if (!gesuch) {
      return false;
    }

    return this.router.url.includes('/tranche/');
  });

  isAenderungRouteSig = computed(() => {
    const gesuch = this.currentGesuchSig();
    if (!gesuch) {
      return false;
    }

    return (
      this.router.url.includes('/aenderung/') ||
      this.router.url.includes('/initial/')
    );
  });

  isInfosRouteSig = computed(() => {
    const isActive = this.router.isActive('infos', {
      paths: 'subset',
      fragment: 'ignored',
      matrixParams: 'ignored',
      queryParams: 'ignored',
    });
    return isActive;
  });

  constructor() {
    effect(
      () => {
        const gesuchId = this.currentGesuchIdSig();
        if (gesuchId) {
          this.gesuchAenderungStore.getAllTranchenForGesuch$({ gesuchId });
        }
      },
      { allowSignalWrites: true },
    );

    effect(
      () => {
        const gesuchTrancheId =
          this.currentGesuchSig()?.gesuchTrancheToWorkWith.id;
        if (gesuchTrancheId) {
          this.dokumentsStore.getGesuchDokumente$({ gesuchTrancheId });
        }
      },
      { allowSignalWrites: true },
    );
  }

  availableTrancheInteractionSig = computed(() => {
    const gesuchStatus = this.currentGesuchSig()?.gesuchStatus;

    switch (gesuchStatus) {
      case 'IN_BEARBEITUNG_SB':
        return 'CREATE_TRANCHE';
      default:
        return null;
    }
  });

  statusUebergaengeOptionsSig = computed(() => {
    const gesuch = this.currentGesuchSig();
    const hasAcceptedAllDokuments = this.hasAcceptedAllDocumentsSig();
    if (!gesuch) {
      return {};
    }

    const list = StatusUebergaengeMap[gesuch.gesuchStatus]?.map((status) =>
      StatusUebergaengeOptions[status]({ hasAcceptedAllDokuments }),
    );

    return {
      list,
      isNotEmpty: !!list?.length,
    };
  });

  setStatusUebergang(nextStatus: StatusUebergang, gesuchTrancheId?: string) {
    if (!gesuchTrancheId) {
      return;
    }

    switch (nextStatus) {
      case 'SET_TO_BEARBEITUNG':
      case 'EINGEREICHT':
      case 'BEARBEITUNG_ABSCHLIESSEN':
      case 'VERFUEGT':
      case 'VERSENDET':
        this.gesuchStore.setStatus$[nextStatus]({ gesuchTrancheId });
        break;
      case 'BEREIT_FUER_BEARBEITUNG':
        SharedUiKommentarDialogComponent.openOptional(this.dialog, {
          entityId: gesuchTrancheId,
          titleKey: `sachbearbeitung-app.header.status-uebergang.BEREIT_FUER_BEARBEITUNG.title`,
          messageKey: `sachbearbeitung-app.header.status-uebergang.BEREIT_FUER_BEARBEITUNG.message`,
          placeholderKey: `sachbearbeitung-app.header.status-uebergang.BEREIT_FUER_BEARBEITUNG.placeholder`,
          confirmKey: `sachbearbeitung-app.header.status-uebergang.BEREIT_FUER_BEARBEITUNG.confirm`,
        })
          .afterClosed()
          .pipe(takeUntilDestroyed(this.destroyRef))
          .subscribe((result) => {
            if (result) {
              this.gesuchStore.setStatus$['BEREIT_FUER_BEARBEITUNG']({
                gesuchTrancheId,
                text: result.kommentar,
              });
            }
          });
        break;
      case 'ZURUECKWEISEN':
        SharedUiKommentarDialogComponent.open(this.dialog, {
          entityId: gesuchTrancheId,
          titleKey: `sachbearbeitung-app.header.status-uebergang.ZURUECKWEISEN.title`,
          messageKey: `sachbearbeitung-app.header.status-uebergang.ZURUECKWEISEN.message`,
          placeholderKey: `sachbearbeitung-app.header.status-uebergang.ZURUECKWEISEN.placeholder`,
          confirmKey: `sachbearbeitung-app.header.status-uebergang.ZURUECKWEISEN.confirm`,
        })
          .afterClosed()
          .pipe(takeUntilDestroyed(this.destroyRef))
          .subscribe((result) => {
            if (result) {
              this.gesuchStore.setStatus$['ZURUECKWEISEN']({
                gesuchTrancheId,
                text: result.kommentar,
              });
            }
          });
        break;
      case 'NEGATIVE_VERFUEGUNG_ERSTELLEN':
        SachbearbeitungAppUiGrundAuswahlDialogComponent.open(this.dialog, {
          titleKey: `sachbearbeitung-app.header.status-uebergang.${nextStatus}.title`,
          labelKey: `sachbearbeitung-app.header.status-uebergang.${nextStatus}.label`,
          messageKey: `sachbearbeitung-app.header.status-uebergang.${nextStatus}.message`,
          confirmKey: `sachbearbeitung-app.header.status-uebergang.${nextStatus}.confirm`,
        })
          .afterClosed()
          .pipe(takeUntilDestroyed(this.destroyRef))
          .subscribe((result) => {
            if (result) {
              this.gesuchStore.setStatus$[nextStatus]({
                gesuchTrancheId,
                grundId: result.entityId,
              });
            }
          });
        break;
      default:
        assertUnreachable(nextStatus);
    }
  }

  createTranche() {
    const gesuch = this.currentGesuchSig();
    if (!gesuch) return;

    SharedUiAenderungMeldenDialogComponent.open(this.dialog, {
      minDate: new Date(gesuch.gesuchsperiode.gesuchsperiodeStart),
      maxDate: new Date(gesuch.gesuchsperiode.gesuchsperiodeStopp),
    })
      .afterClosed()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((result) => {
        if (result) {
          this.gesuchAenderungStore.createGesuchTrancheCopy$({
            gesuchId: gesuch.id,
            createGesuchTrancheRequest: result,
          });
        }
      });
  }
}
