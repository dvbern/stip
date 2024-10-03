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
import { MatError, MatFormField, MatLabel } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { MatSelect } from '@angular/material/select';
import { Router, RouterLink } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { filter } from 'rxjs';

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
import { getLatestGesuchIdFromGesuch$ } from '@dv/shared/util/gesuch';
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
  private translate = inject(TranslateService);
  private formBuilder = inject(NonNullableFormBuilder);
  private defaultCommentSig = toSignal(
    this.translate.stream('shared.form.tranche.bemerkung.initialgesuch'),
  );
  private einreichenStore = inject(EinreichenStore);

  gesuchAenderungStore = inject(GesuchAenderungStore);
  einreichenViewSig = this.einreichenStore.einreichenViewSig;

  languageSig = this.store.selectSignal(selectLanguage);
  viewSig = this.store.selectSignal(selectSharedFeatureGesuchFormTrancheView);

  form = this.formBuilder.group({
    von: ['', [Validators.required]],
    bis: ['', [Validators.required]],
    bemerkung: ['', [Validators.required]],
  });

  currentTrancheIndexSig = computed(() => {
    const currentTranche = this.viewSig().gesuchstranche;

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
      .pipe(filter(isDefined), takeUntilDestroyed())
      .subscribe((gesuchId) => {
        this.gesuchAenderungStore.getAllTranchenForGesuch$({ gesuchId });
      });

    effect(
      () => {
        const tranche = this.viewSig().gesuchstranche;
        const defaultComment = this.defaultCommentSig();
        if (!tranche) {
          return;
        }

        this.form.patchValue({
          von: formatBackendLocalDate(tranche.gueltigAb, this.languageSig()),
          bis: formatBackendLocalDate(tranche.gueltigBis, this.languageSig()),
          bemerkung: tranche.comment ?? defaultComment,
        });
      },
      { allowSignalWrites: true },
    );

    this.store.dispatch(SharedDataAccessGesuchEvents.loadGesuch());

    this.einreichenStore.onCheckIsPossible$.subscribe(([gesuchTrancheId]) => {
      this.einreichenStore.validateEinreichen$({
        gesuchTrancheId,
      });
    });
  }

  changeAenderungState(
    aenderungId: string,
    target: AenderungChangeState,
    gesuchId: string,
  ) {
    this.gesuchAenderungStore.changeAenderungState$({
      aenderungId,
      target,
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
