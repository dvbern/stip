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
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { filter, firstValueFrom } from 'rxjs';

import { EinreichenStore } from '@dv/shared/data-access/einreichen';
import { SharedDataAccessGesuchEvents } from '@dv/shared/data-access/gesuch';
import {
  AenderungChangeState,
  GesuchAenderungStore,
} from '@dv/shared/data-access/gesuch-aenderung';
import { selectLanguage } from '@dv/shared/data-access/language';
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
import { formatBackendLocalDate } from '@dv/shared/util/validator-date';

import { selectSharedFeatureGesuchFormTrancheView } from './shared-feature-gesuch-form-tranche.selector';

@Component({
  selector: 'dv-shared-feature-gesuch-form-tranche',
  standalone: true,
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
  });

  currentTrancheNumberSig = computed(() => {
    const currentTranche = this.viewSig().tranche;

    if (!currentTranche) {
      return '…';
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

    return index >= 0 ? index + 1 : '…';
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
          periode,
          sachbearbeiter,
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
          gesuchsperiode: periode
            ? this.translate.instant(
                `shared.form.tranche.gesuchsperiode.semester.${periode.semester}`,
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
