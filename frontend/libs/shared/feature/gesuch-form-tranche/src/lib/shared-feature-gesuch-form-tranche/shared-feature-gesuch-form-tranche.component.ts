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
import { Store } from '@ngrx/store';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { filter } from 'rxjs';

import { SharedDataAccessGesuchEvents } from '@dv/shared/data-access/gesuch';
import { GesuchAenderungStore } from '@dv/shared/data-access/gesuch-aenderung';
import { selectLanguage } from '@dv/shared/data-access/language';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { SharedUiHeaderSuffixDirective } from '@dv/shared/ui/header-suffix';
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
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SharedUiHeaderSuffixDirective,
    TranslateModule,
  ],
  templateUrl: './shared-feature-gesuch-form-tranche.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchFormTrancheComponent {
  private store = inject(Store);
  private translate = inject(TranslateService);
  private formBuilder = inject(NonNullableFormBuilder);
  private defaultCommentSig = toSignal(
    this.translate.stream('shared.form.tranche.bemerkung.initialgesuch'),
  );
  gesuchAenderungStore = inject(GesuchAenderungStore);

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

  languageSig = this.store.selectSignal(selectLanguage);
  viewSig = this.store.selectSignal(selectSharedFeatureGesuchFormTrancheView);

  form = this.formBuilder.group({
    von: ['', [Validators.required]],
    bis: ['', [Validators.required]],
    bemerkung: ['', [Validators.required]],
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
  }
}