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
  Validators,
} from '@angular/forms';
import { MatOption } from '@angular/material/autocomplete';
import { MatDialog } from '@angular/material/dialog';
import { MatError, MatFormField, MatLabel } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { MatSelect } from '@angular/material/select';
import { Router, RouterLink } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { filter, firstValueFrom } from 'rxjs';

import { EinreichenStore } from '@dv/shared/data-access/einreichen';
import { SharedDataAccessGesuchEvents } from '@dv/shared/data-access/gesuch';
import {
  AenderungChangeState,
  GesuchAenderungStore,
} from '@dv/shared/data-access/gesuch-aenderung';
import { selectLanguage } from '@dv/shared/data-access/language';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { SharedUiHeaderSuffixDirective } from '@dv/shared/ui/header-suffix';
import { SharedUiIfSachbearbeiterDirective } from '@dv/shared/ui/if-app-type';
import { SharedUiKommentarDialogComponent } from '@dv/shared/ui/kommentar-dialog';
import {
  getLatestGesuchIdFromGesuch$,
  getLatestTrancheIdFromGesuchOnUpdate$,
} from '@dv/shared/util/gesuch';
import { formatBackendLocalDate } from '@dv/shared/util/validator-date';
import { isDefined } from '@dv/shared/util-fn/type-guards';

import { selectSharedFeatureGesuchFormTrancheView } from './shared-feature-gesuch-form-tranche.selector';

@Component({
  selector: 'dv-shared-feature-gesuch-form-tranche',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatError,
    MatFormField,
    MatInput,
    MatLabel,
    MatOption,
    MatSelect,
    ReactiveFormsModule,
    RouterLink,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SharedUiHeaderSuffixDirective,
    SharedUiIfSachbearbeiterDirective,
    TranslateModule,
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

  einreichenStore = inject(EinreichenStore);
  gesuchAenderungStore = inject(GesuchAenderungStore);

  languageSig = this.store.selectSignal(selectLanguage);
  viewSig = this.store.selectSignal(selectSharedFeatureGesuchFormTrancheView);

  form = this.formBuilder.group({
    status: ['', [Validators.required]],
    pia: ['', [Validators.required]],
    gesuchsnummer: ['', [Validators.required]],
    fallnummer: ['', [Validators.required]],
    sachbearbeiter: ['', [Validators.required]],
    von: ['', [Validators.required]],
    bis: ['', [Validators.required]],
    bemerkung: ['', [Validators.required]],
  });

  currentTrancheIndexSig = computed(() => {
    const currentTranche = this.viewSig().tranche;

    if (!currentTranche) {
      return 0;
    }

    const tranchen = this.gesuchAenderungStore.tranchenViewSig();
    const aenderungen = this.gesuchAenderungStore.aenderungenViewSig();
    const list = {
      TRANCHE: tranchen.list,
      AENDERUNG: aenderungen.list,
    };
    const index = list[currentTranche.typ].findIndex(
      (aenderung) => aenderung.id === currentTranche.id,
    );

    return index ?? 0;
  });

  constructor() {
    getLatestGesuchIdFromGesuch$(this.viewSig)
      .pipe(takeUntilDestroyed())
      .subscribe((gesuchId) => {
        this.gesuchAenderungStore.getAllTranchenForGesuch$({ gesuchId });
      });

    effect(
      () => {
        const {
          isEditingTranche,
          gesuch,
          tranche,
          gesuchsNummer,
          fallNummer,
          sachbearbeiter,
        } = this.viewSig();

        // React to language change
        this.languageSig();

        const defaultComment = this.defaultCommentSig();
        if (!tranche) {
          return;
        }
        const pia = tranche.gesuchFormular?.personInAusbildung;
        const useTrancheStatus =
          isEditingTranche && tranche.status !== 'UEBERPRUEFEN';
        const status = useTrancheStatus ? tranche.status : gesuch?.gesuchStatus;
        const type = useTrancheStatus ? 'tranche' : 'contract';

        this.form.patchValue({
          status: this.translate.instant(
            `shared.gesuch.status.${type}.${status ?? 'IN_BEARBEITUNG_GS'}`,
          ),
          pia: pia ? `${pia.vorname} ${pia.nachname}` : '',
          gesuchsnummer: gesuchsNummer,
          fallnummer: fallNummer,
          sachbearbeiter: sachbearbeiter,
          von: formatBackendLocalDate(tranche.gueltigAb, this.languageSig()),
          bis: formatBackendLocalDate(tranche.gueltigBis, this.languageSig()),
          bemerkung: tranche.comment ?? defaultComment,
        });
      },
      { allowSignalWrites: true },
    );

    this.store.dispatch(SharedDataAccessGesuchEvents.loadGesuch());

    getLatestTrancheIdFromGesuchOnUpdate$(this.viewSig)
      .pipe(filter(isDefined), takeUntilDestroyed())
      .subscribe((gesuchTrancheId) => {
        this.einreichenStore.validateEinreichen$({
          gesuchTrancheId,
        });
      });
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
