import { DatePipe } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  effect,
  inject,
  input,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { NonNullableFormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatChipsModule } from '@angular/material/chips';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { TranslocoService } from '@jsverse/transloco';
import { Store } from '@ngrx/store';

import { translatableShared } from '@dv/shared/assets/i18n';
import { DelegationStore } from '@dv/shared/data-access/delegation';
import { selectLanguage } from '@dv/shared/data-access/language';
import { SharedEventGesuchFormDelegierungen } from '@dv/shared/event/gesuch-form-delegierungen';
import { Delegierung } from '@dv/shared/model/gesuch';
import { SharedUiAdvTranslocoDirective } from '@dv/shared/ui/adv-transloco-directive';
import { SharedUiFormReadonlyDirective } from '@dv/shared/ui/form';
import { SharedUiFormAddressComponent } from '@dv/shared/ui/form-address';
import { SharedUiTruncateTooltipDirective } from '@dv/shared/ui/truncate-tooltip';
import { formatBackendLocalDate } from '@dv/shared/util/validator-date';

@Component({
  selector: 'dv-shared-feature-gesuch-form-delegierungen',
  imports: [
    DatePipe,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatListModule,
    MatChipsModule,
    SharedUiFormReadonlyDirective,
    SharedUiAdvTranslocoDirective,
    SharedUiFormAddressComponent,
    SharedUiTruncateTooltipDirective,
  ],
  templateUrl: './shared-feature-gesuch-form-delegierungen.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchFormDelegierungenComponent {
  private fb = inject(NonNullableFormBuilder);
  private store = inject(Store);
  private translate = inject(TranslocoService);
  // eslint-disable-next-line @angular-eslint/no-input-rename
  gesuchIdSig = input.required<string>({ alias: 'gesuchId' });

  languageSig = this.store.selectSignal(selectLanguage);
  delegationStore = inject(DelegationStore);
  selectedDelegierung = this.fb.control(<Delegierung[] | null>null);
  form = this.fb.group({
    start: [''],
    end: [''],
    name: [''],
    status: [''],
    telefonnummer: [''],
    adresse: SharedUiFormAddressComponent.buildAddressFormGroup(this.fb),
    email: [''],
    nachname: [''],
    vorname: [''],
  });

  private selectedDelegierungChangedSig = toSignal(
    this.selectedDelegierung.valueChanges,
  );

  constructor() {
    this.store.dispatch(SharedEventGesuchFormDelegierungen.init());
    effect(() => {
      const gesuchId = this.gesuchIdSig();
      if (!gesuchId) {
        return;
      }

      this.delegationStore.loadDelegationen$({ gesuchId });
    });

    effect(() => {
      const delegierungen = this.delegationStore.delegationenViewSig();

      if (!delegierungen?.length) {
        return;
      }

      this.selectedDelegierung.patchValue([delegierungen[0]]);
    });

    effect(() => {
      const delegierung = this.selectedDelegierungChangedSig()?.[0];
      const language = this.languageSig();

      this.form.patchValue({
        start: formatBackendLocalDate(delegierung?.startDate, language),
        end: formatBackendLocalDate(delegierung?.endDate, language),
        name: delegierung?.sozialdienst?.name,
        status: delegierung?.status
          ? this.translate.translate(
              translatableShared(
                `shared.delegierung.status.${delegierung.status}`,
              ),
            )
          : undefined,
        email: delegierung?.delegierterMitarbeiter?.email,
        telefonnummer: undefined,
        vorname: delegierung?.delegierterMitarbeiter?.vorname,
        nachname: delegierung?.delegierterMitarbeiter?.nachname,
      });
      SharedUiFormAddressComponent.patchForm(
        this.form.controls.adresse,
        delegierung?.sozialdienst?.zahlungsverbindung.adresse ?? {},
      );
    });
  }
}
