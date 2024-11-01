import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  effect,
  inject,
  input,
  viewChild,
} from '@angular/core';
import {
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSort } from '@angular/material/sort';
import { MatCell } from '@angular/material/table';
import { RouterLink } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';

import { NotizStore } from '@dv/sachbearbeitung-app/data-access/notiz';
import { SharedDataAccessGesuchEvents } from '@dv/shared/data-access/gesuch';
import { selectLanguage } from '@dv/shared/data-access/language';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
  SharedUiFormSaveComponent,
} from '@dv/shared/ui/form';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import {
  SharedUiRdIsPendingPipe,
  SharedUiRdIsPendingWithoutCachePipe,
} from '@dv/shared/ui/remote-data-pipe';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';
import { convertTempFormToRealValues } from '@dv/shared/util/form';
import { parseBackendLocalDateAndPrint } from '@dv/shared/util/validator-date';
import { isDefined } from '@dv/shared/util-fn/type-guards';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-infos-notizen',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    MatFormFieldModule,
    ReactiveFormsModule,
    TranslateModule,
    SharedUiLoadingComponent,
    SharedUiRdIsPendingPipe,
    SharedUiRdIsPendingWithoutCachePipe,
    SharedUiFormSaveComponent,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    MatInputModule,
    MatCell,
    TypeSafeMatCellDefDirective,
  ],
  templateUrl:
    './sachbearbeitung-app-feature-infos-notizen-detail.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureInfosNotizenDetailComponent {
  private store = inject(Store);
  private formBuilder = inject(NonNullableFormBuilder);
  notizStore = inject(NotizStore);

  form = this.formBuilder.group({
    datum: [<string | undefined>undefined, [Validators.required]],
    user: [<string | null>null, [Validators.required]],
    betreff: [<string | null>null, [Validators.required]],
    text: [<string | null>null, [Validators.required]],
  });
  notizIdSig = input.required<string>({ alias: 'notizId' });
  languageSig = this.store.selectSignal(selectLanguage);

  constructor() {
    this.store.dispatch(SharedDataAccessGesuchEvents.loadGesuch());

    effect(() => {
      const notiz = this.notizStore.notizViewSig();

      if (!isDefined(notiz)) {
        return;
      }

      this.form.patchValue({
        datum: parseBackendLocalDateAndPrint(
          notiz.timestampErstellt,
          this.languageSig(),
        ),
        betreff: notiz.betreff,
        text: notiz.text,
        user: notiz.userErstellt,
      });
    });

    effect(
      () => {
        const notizId = this.notizIdSig();

        if (notizId === 'create') {
          return;
        }

        this.notizStore.loadNotiz$({
          notizId,
        });
      },
      { allowSignalWrites: true },
    );
  }

  handleSave() {
    if (!this.form.valid) {
      return;
    }

    const notizDaten = convertTempFormToRealValues(this.form);

    this.notizStore.saveNotiz$({
      notizDaten: {
        id: this.notizIdSig(),
        text: notizDaten.text,
        betreff: notizDaten.betreff,
      },
    });
  }

  sortSig = viewChild(MatSort);
}
