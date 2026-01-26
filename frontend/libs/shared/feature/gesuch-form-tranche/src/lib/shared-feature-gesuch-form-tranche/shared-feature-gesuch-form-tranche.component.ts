import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  inject,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import {
  FormsModule,
  NonNullableFormBuilder,
  ReactiveFormsModule,
} from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Router, RouterLink } from '@angular/router';
import {
  TranslocoPipe,
  TranslocoService,
  translateSignal,
} from '@jsverse/transloco';
import { Store } from '@ngrx/store';
import { addMonths, endOfMonth } from 'date-fns';
import { filter, firstValueFrom } from 'rxjs';

import { EinreichenStore } from '@dv/shared/data-access/einreichen';
import {
  SharedDataAccessGesuchEvents,
  selectRevision,
} from '@dv/shared/data-access/gesuch';
import {
  AenderungChangeState,
  GesuchAenderungStore,
} from '@dv/shared/data-access/gesuch-aenderung';
import { GesuchInfoStore } from '@dv/shared/data-access/gesuch-info';
import { selectLanguage } from '@dv/shared/data-access/language';
import { SharedDialogChangeGesuchsperiodeComponent } from '@dv/shared/dialog/change-gesuchsperiode';
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
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import {
  getLatestGesuchIdFromGesuch$,
  getLatestTrancheIdFromGesuchOnUpdate$,
} from '@dv/shared/util/gesuch';
import { isPending } from '@dv/shared/util/remote-data';
import {
  dateFromMonthYearString,
  formatBackendLocalDate,
  parseBackendLocalDateAndPrint,
} from '@dv/shared/util/validator-date';
import { findIndexInOneOf } from '@dv/shared/util-fn/array-helper';

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
    SharedUiLoadingComponent,
    TranslocoPipe,
  ],
  templateUrl: './shared-feature-gesuch-form-tranche.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchFormTrancheComponent {
  private store = inject(Store);
  private router = inject(Router);
  private dialog = inject(MatDialog);
  private translate = inject(TranslocoService);
  private formBuilder = inject(NonNullableFormBuilder);
  private defaultCommentSig = translateSignal(
    'shared.form.tranche.bemerkung.initialgesuch',
  );

  isSbApp = inject(SharedModelCompileTimeConfig).isSachbearbeitungApp;
  einreichenStore = inject(EinreichenStore);
  gesuchAenderungStore = inject(GesuchAenderungStore);
  gesuchInfoStore = inject(GesuchInfoStore, {
    optional: true,
  });

  languageSig = this.store.selectSignal(selectLanguage);
  viewSig = this.store.selectSignal(selectSharedFeatureGesuchFormTrancheView);
  revisionSig = this.store.selectSignal(selectRevision);
  isAenderungUpdatingSig = computed(() => {
    const { loading } = this.viewSig();
    return (
      loading || isPending(this.gesuchAenderungStore.cachedGesuchAenderung())
    );
  });

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
    const { tranche: currentTranche, trancheSetting } = this.viewSig();

    if (!currentTranche) {
      return '…';
    }

    const gesuchUrlTyp = trancheSetting?.gesuchUrlTyp;
    const tranchen = this.gesuchAenderungStore.tranchenViewSig();
    const aenderungen = this.gesuchAenderungStore.aenderungenViewSig();
    const list = {
      TRANCHE: [tranchen.list],
      AENDERUNG: [aenderungen.aenderungen, aenderungen.abgelehnteAenderungen],
      INITIAL: [aenderungen.initialGesuche],
    } satisfies Record<GesuchUrlType, unknown>;
    const index = gesuchUrlTyp
      ? findIndexInOneOf(
          (aenderung) =>
            aenderung.id === currentTranche.id &&
            isDefined(aenderung.revision) === isDefined(this.revisionSig()),
          ...list[gesuchUrlTyp],
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
        this.gesuchInfoStore?.loadGesuchInfo$({ gesuchId });
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
      const isAbgelehnt = this.revisionSig();

      // Also used to react to language change
      // if not used anymore, still call it if this.translate is still used
      const language = this.languageSig();

      const defaultComment = this.defaultCommentSig();
      if (!tranche || !gesuch) {
        return;
      }
      const pia = tranche.gesuchFormular?.personInAusbildung;
      const status = isEditingAenderung ? tranche.status : gesuch.gesuchStatus;
      const type = isEditingAenderung ? 'tranche' : 'contract';
      const appPrefix = type === 'contract' ? appType : 'shared';

      this.form.patchValue({
        status: this.translate.translate(
          `${appPrefix}.gesuch.status.${type}.${isAbgelehnt ? 'ABGELEHNT' : (status ?? 'IN_BEARBEITUNG_GS')}`,
        ),
        pia: pia ? `${pia.vorname} ${pia.nachname}` : '',
        gesuchsnummer: gesuchsNummer,
        fallnummer: fallNummer,
        gesuchsperiode: periode
          ? this.translate.translate(
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
      gesuchTrancheToWorkWith: { id, gueltigAb, gueltigBis, gesuchFormular },
      gesuchsperiode: { gesuchsperiodeStart },
    } = gesuch;

    const begin = dateFromMonthYearString(
      gesuchFormular?.ausbildung?.ausbildungBegin,
    );
    if (!begin) return;

    const maxDate = endOfMonth(addMonths(new Date(begin), 11));

    SharedDialogTrancheErstellenComponent.open(this.dialog, {
      type: 'updateAenderungVonBis',
      id: id,
      minDate: new Date(gesuchsperiodeStart),
      maxDate,
      currentGueligAb: new Date(gueltigAb),
      currentGueligBis: new Date(gueltigBis),
    })
      .afterClosed()
      .subscribe();
  }

  changeGesuchsperiode(gesuchTrancheId: string | undefined) {
    const { gesuchId, trancheSetting } = this.viewSig();
    const gesuchFormular =
      this.viewSig().gesuch?.gesuchTrancheToWorkWith.gesuchFormular;

    if (!gesuchTrancheId || !gesuchFormular || !gesuchId || !trancheSetting) {
      return;
    }

    SharedDialogChangeGesuchsperiodeComponent.open(this.dialog, {
      gesuchTrancheId,
      gesuchId,
      trancheSetting,
      gesuchFormular,
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
