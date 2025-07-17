import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  inject,
} from '@angular/core';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import {
  FormsModule,
  NonNullableFormBuilder,
  ReactiveFormsModule,
} from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Router, RouterLink } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { filter, firstValueFrom } from 'rxjs';

import { EinreichenStore } from '@dv/shared/data-access/einreichen';
import { SharedDataAccessGesuchEvents } from '@dv/shared/data-access/gesuch';
import {
  AenderungChangeState,
  GesuchAenderungStore,
} from '@dv/shared/data-access/gesuch-aenderung';
import { selectLanguage } from '@dv/shared/data-access/language';
import { SharedDialogEinreichedatumAendernComponent } from '@dv/shared/dialog/einreichedatum-aendern';
import { SharedDialogTrancheErstellenComponent } from '@dv/shared/dialog/tranche-erstellen';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import { GesuchUrlType, SharedModelGesuch } from '@dv/shared/model/gesuch';
import { isDefined } from '@dv/shared/model/type-util';
import {
  SharedUiFormFieldDirective,
  SharedUiFormReadonlyDirective,
} from '@dv/shared/ui/form';
import { SharedUiHeaderSuffixDirective } from '@dv/shared/ui/header-suffix';
import { SharedUiIfSachbearbeiterDirective } from '@dv/shared/ui/if-app-type';
import { SharedUiKommentarDialogComponent } from '@dv/shared/ui/kommentar-dialog';
import {
  getLatestGesuchIdFromGesuch$,
  getLatestTrancheIdFromGesuchOnUpdate$,
} from '@dv/shared/util/gesuch';
import {
  formatBackendLocalDate,
  parseBackendLocalDateAndPrint,
} from '@dv/shared/util/validator-date';

import { selectSharedFeatureGesuchFormTrancheView } from './shared-feature-gesuch-form-tranche.selector';

@Component({
  selector: 'dv-shared-feature-gesuch-form-tranche',
  imports: [
    CommonModule,
    FormsModule,
    RouterLink,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    SharedUiFormFieldDirective,
    SharedUiHeaderSuffixDirective,
    SharedUiIfSachbearbeiterDirective,
    SharedUiFormReadonlyDirective,
    TranslatePipe,
  ],
  templateUrl: './shared-feature-gesuch-form-tranche.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchFormTrancheComponent {
  private store = inject(Store);
  private router = inject(Router);
  private dialog = inject(MatDialog);
  private translate = inject(TranslateService);
  private formBuilder = inject(NonNullableFormBuilder);
  private defaultCommentSig = toSignal(
    this.translate.stream('shared.form.tranche.bemerkung.initialgesuch'),
  );

  isSbApp = inject(SharedModelCompileTimeConfig).isSachbearbeitungApp;
  einreichenStore = inject(EinreichenStore);
  gesuchAenderungStore = inject(GesuchAenderungStore);

  languageSig = this.store.selectSignal(selectLanguage);
  viewSig = this.store.selectSignal(selectSharedFeatureGesuchFormTrancheView);

  form = this.formBuilder.group({
    status: [''],
    pia: [''],
    gesuchsnummer: [''],
    fallnummer: [''],
    gesuchsperiode: [''],
    einreichefrist: [''],
    sachbearbeiter: [''],
    von: [''],
    bis: [''],
    bemerkung: [''],
    einreichedatum: [''],
  });

  currentTrancheNumberSig = computed(() => {
    const { tranche: currentTranche, gesuchUrlTyp } = this.viewSig();

    if (!currentTranche) {
      return '…';
    }

    const tranchen = this.gesuchAenderungStore.tranchenViewSig();
    const aenderungen = this.gesuchAenderungStore.aenderungenViewSig();
    const initialTranchen = this.gesuchAenderungStore.initialTranchenViewSig();
    const list = {
      TRANCHE: tranchen.list,
      AENDERUNG: aenderungen.list,
      INITIAL: initialTranchen.list ?? [],
    } satisfies Record<GesuchUrlType, unknown>;
    const index = gesuchUrlTyp
      ? list[gesuchUrlTyp].findIndex(
          (aenderung) => aenderung.id === currentTranche.id,
        )
      : -1;

    return index >= 0 ? index + 1 : '…';
  });

  currentGesuchSig = computed(
    () => {
      const { gesuch } = this.viewSig();
      return { status: gesuch?.gesuchStatus, gesuchId: gesuch?.id };
    },
    { equal: (a, b) => a.status === b.status && a.gesuchId === b.gesuchId },
  );

  constructor() {
    getLatestGesuchIdFromGesuch$(this.viewSig)
      .pipe(takeUntilDestroyed())
      .subscribe((gesuchId) => {
        this.gesuchAenderungStore.getAllTranchenForGesuch$({ gesuchId });
      });

    effect(() => {
      const { gesuchId } = this.currentGesuchSig();
      if (gesuchId && this.isSbApp) {
        this.einreichenStore.checkEinreichedatumAendern$({ gesuchId });
      }
    });

    effect(() => {
      const {
        isEditingAenderung,
        gesuch,
        tranche,
        gesuchsNummer,
        fallNummer,
        periode,
        sachbearbeiter,
        appType,
      } = this.viewSig();

      // Also used to react to language change
      // if not used anymore, still call it if this.translate is still used
      const language = this.languageSig();

      const defaultComment = this.defaultCommentSig();
      if (!tranche) {
        return;
      }
      const pia = tranche.gesuchFormular?.personInAusbildung;
      const useTrancheStatus =
        isEditingAenderung && tranche.status !== 'UEBERPRUEFEN';
      const status = useTrancheStatus ? tranche.status : gesuch?.gesuchStatus;
      const type = useTrancheStatus ? 'tranche' : 'contract';
      const appPrefix = type === 'contract' ? appType : 'shared';

      this.form.patchValue({
        status: this.translate.instant(
          `${appPrefix}.gesuch.status.${type}.${status ?? 'IN_BEARBEITUNG_GS'}`,
        ),
        pia: pia ? `${pia.vorname} ${pia.nachname}` : '',
        gesuchsnummer: gesuchsNummer,
        fallnummer: fallNummer,
        gesuchsperiode: periode
          ? this.translate.instant(
              'shared.form.tranche.gesuchsperiode',
              periode,
            )
          : '',
        einreichefrist: formatBackendLocalDate(
          periode?.einreichefrist,
          language,
        ),
        sachbearbeiter: sachbearbeiter,
        von: formatBackendLocalDate(tranche.gueltigAb, language),
        bis: formatBackendLocalDate(tranche.gueltigBis, language),
        bemerkung: tranche.comment ?? defaultComment,
        einreichedatum: parseBackendLocalDateAndPrint(
          gesuch?.einreichedatum,
          language,
        ),
      });
    });

    this.store.dispatch(SharedDataAccessGesuchEvents.loadGesuch());

    getLatestTrancheIdFromGesuchOnUpdate$(this.viewSig)
      .pipe(filter(isDefined), takeUntilDestroyed())
      .subscribe((gesuchTrancheId) => {
        this.einreichenStore.validateEinreichen$({
          gesuchTrancheId,
        });
      });
  }

  changeEinreichedatum(
    gesuchId: string,
    einreichedatum: string,
    minDate: string,
    maxDate: string,
  ) {
    SharedDialogEinreichedatumAendernComponent.open(this.dialog, {
      einreichedatum,
      minDate,
      maxDate,
    })
      .afterClosed()
      .subscribe((result) => {
        if (result) {
          this.einreichenStore.einreichedatumManuellAendern$({
            gesuchId,
            change: result,
          });
        }
      });
  }

  updateAenderungVonBis(gesuch: SharedModelGesuch) {
    const {
      gesuchTrancheToWorkWith: { id },
      gesuchsperiode: { gesuchsperiodeStart, gesuchsperiodeStopp },
    } = gesuch;
    SharedDialogTrancheErstellenComponent.open(this.dialog, {
      type: 'updateAenderungVonBis',
      id: id,
      minDate: new Date(gesuchsperiodeStart),
      maxDate: new Date(gesuchsperiodeStopp),
    })
      .afterClosed()
      .subscribe();
  }

  async changeAenderungState(
    aenderungId: string,
    target: AenderungChangeState,
    gesuchId: string,
  ) {
    let comment = undefined;
    if (target === 'ABGELEHNT') {
      comment = (
        await firstValueFrom(
          SharedUiKommentarDialogComponent.open(this.dialog, {
            entityId: aenderungId,
            titleKey: 'shared.dialog.gesuch-aenderung.ABGELEHNT.title',
            messageKey: 'shared.dialog.gesuch-aenderung.ABGELEHNT.description',
            labelKey: 'shared.dialog.gesuch-aenderung.ABGELEHNT.comment.label',
            placeholderKey: '',
            confirmKey: 'shared.form.send',
          }).afterClosed(),
        )
      )?.kommentar;

      if (!comment) {
        return;
      }
    }

    this.gesuchAenderungStore.changeAenderungState$({
      aenderungId,
      target,
      comment: comment ?? '',
      gesuchId,
      onSuccess: (trancheId) => {
        const routesMap = {
          AKZEPTIERT: ['gesuch', gesuchId, 'tranche', trancheId],
          ABGELEHNT: ['/'],
          MANUELLE_AENDERUNG: ['gesuch', gesuchId],
        } satisfies Record<AenderungChangeState, unknown>;

        this.store.dispatch(SharedDataAccessGesuchEvents.loadGesuch());
        this.router.navigate(routesMap[target]);
      },
    });
  }
}
