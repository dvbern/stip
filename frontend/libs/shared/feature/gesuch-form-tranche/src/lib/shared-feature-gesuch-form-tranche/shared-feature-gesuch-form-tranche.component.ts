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
import { TranslocoService, translateSignal } from '@jsverse/transloco';
import { Store } from '@ngrx/store';
import { addMonths, endOfMonth } from 'date-fns';
import { filter } from 'rxjs';

import { translatableShared } from '@dv/shared/assets/i18n';
import { EinreichenStore } from '@dv/shared/data-access/einreichen';
import {
  SharedDataAccessGesuchEvents,
  selectRevision,
} from '@dv/shared/data-access/gesuch';
import { GesuchHeaderStore } from '@dv/shared/data-access/gesuch-header';
import { GesuchInfoStore } from '@dv/shared/data-access/gesuch-info';
import { selectLanguage } from '@dv/shared/data-access/language';
import { SharedDialogChangeGesuchsperiodeComponent } from '@dv/shared/dialog/change-gesuchsperiode';
import { SharedDialogEinreichedatumAendernComponent } from '@dv/shared/dialog/einreichedatum-aendern';
import { SharedDialogTrancheErstellenComponent } from '@dv/shared/dialog/tranche-erstellen';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import { SharedModelGesuch } from '@dv/shared/model/gesuch';
import { isDefined } from '@dv/shared/model/type-util';
import { SharedUiAdvTranslocoDirective } from '@dv/shared/ui/adv-transloco-directive';
import {
  SharedUiFormFieldDirective,
  SharedUiFormReadonlyDirective,
} from '@dv/shared/ui/form';
import { SharedUiHeaderSuffixDirective } from '@dv/shared/ui/header-suffix';
import { getLatestTrancheIdFromGesuchOnUpdate$ } from '@dv/shared/util/gesuch';
import {
  dateFromMonthYearString,
  formatBackendLocalDate,
  parseBackendLocalDateAndPrint,
} from '@dv/shared/util/validator-date';

import { selectSharedFeatureGesuchFormTrancheView } from './shared-feature-gesuch-form-tranche.selector';

@Component({
  selector: 'dv-shared-feature-gesuch-form-tranche',
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    SharedUiFormFieldDirective,
    SharedUiHeaderSuffixDirective,
    SharedUiFormReadonlyDirective,
    SharedUiAdvTranslocoDirective,
  ],
  templateUrl: './shared-feature-gesuch-form-tranche.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchFormTrancheComponent {
  private store = inject(Store);
  private dialog = inject(MatDialog);
  private translate = inject(TranslocoService);
  private formBuilder = inject(NonNullableFormBuilder);
  private defaultCommentSig = translateSignal(
    translatableShared('shared.form.tranche.bemerkung.initialgesuch'),
  );

  isSbApp = inject(SharedModelCompileTimeConfig).isSachbearbeitungApp;
  einreichenStore = inject(EinreichenStore);
  gesuchHeaderStore = inject(GesuchHeaderStore);
  gesuchInfoStore = inject(GesuchInfoStore, {
    optional: true,
  });

  languageSig = this.store.selectSignal(selectLanguage);
  viewSig = this.store.selectSignal(selectSharedFeatureGesuchFormTrancheView);
  revisionSig = this.store.selectSignal(selectRevision);

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

  currentGesuchSig = computed(
    () => {
      const { gesuch } = this.viewSig();
      return { status: gesuch?.gesuchStatus, gesuchId: gesuch?.id };
    },
    { equal: (a, b) => a.status === b.status && a.gesuchId === b.gesuchId },
  );

  constructor() {
    effect(() => {
      const { gesuchId } = this.currentGesuchSig();
      if (gesuchId && this.isSbApp) {
        this.gesuchHeaderStore.loadHeader$({ gesuchId });
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

      // Also used to react to language change
      // if not used anymore, still call it if this.translate is still used
      const language = this.languageSig();
      const isAbgelehnteAenderung = this.revisionSig() && isEditingAenderung;

      const defaultComment = this.defaultCommentSig();
      if (!tranche || !gesuch) {
        return;
      }
      const pia = tranche.gesuchFormular?.personInAusbildung;
      const status = isEditingAenderung ? tranche.status : gesuch.gesuchStatus;
      const type = isEditingAenderung ? 'tranche' : 'contract';
      const appPrefix = type === 'contract' ? appType : 'shared';
      const overridenStatus = gesuch.hasPendingAusbildungUnterbruchAntrag
        ? this.translate.translate('shared.gesuch.status.unterbruchAnfrage')
        : null;

      this.form.patchValue({
        status:
          overridenStatus ??
          this.translate.translate(
            `${appPrefix}.gesuch.status.${type}.${isAbgelehnteAenderung ? 'ABGELEHNT' : (status ?? 'IN_BEARBEITUNG_GS')}`,
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
        sachbearbeiter,
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
      id: gesuchId,
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
      trancheId: id,
      gesuchId,
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
}
