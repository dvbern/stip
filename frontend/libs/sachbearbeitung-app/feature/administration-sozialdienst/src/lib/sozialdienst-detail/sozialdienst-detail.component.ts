import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  OnDestroy,
  inject,
  input,
  signal,
} from '@angular/core';
import {
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { ActivatedRoute, Router } from '@angular/router';
import { MaskitoDirective } from '@maskito/angular';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';

import { SozialdienstStore } from '@dv/sachbearbeitung-app/data-access/sozialdienst';
import { selectLanguage } from '@dv/shared/data-access/language';
import { Land, MASK_IBAN, PATTERN_EMAIL } from '@dv/shared/model/gesuch';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
  SharedUiFormSaveComponent,
} from '@dv/shared/ui/form';
import { SharedUiFormAddressComponent } from '@dv/shared/ui/form-address';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import {
  SharedUiRdIsPendingPipe,
  SharedUiRdIsPendingWithoutCachePipe,
} from '@dv/shared/ui/remote-data-pipe';
import { convertTempFormToRealValues } from '@dv/shared/util/form';
import { ibanValidator } from '@dv/shared/util/validator-iban';

@Component({
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    TranslateModule,
    MaskitoDirective,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SharedUiFormSaveComponent,
    SharedUiLoadingComponent,
    SharedUiRdIsPendingPipe,
    SharedUiRdIsPendingWithoutCachePipe,
    SharedUiFormAddressComponent,
  ],
  templateUrl: './sozialdienst-detail.component.html',
  styleUrl: './sozialdienst-detail.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SozialdienstDetailComponent implements OnDestroy {
  private formBuilder = inject(NonNullableFormBuilder);
  idSig = input.required<string | undefined>({ alias: 'id' });
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  store = inject(SozialdienstStore);
  globalStore = inject(Store);

  laenderSig = signal<Land[]>(['CH']);
  languageSig = this.globalStore.selectSignal(selectLanguage);

  MASK_IBAN = MASK_IBAN;

  form = this.formBuilder.group({
    name: [<string | null>null, [Validators.required]],
    adresse: SharedUiFormAddressComponent.buildAddressFormGroup(
      this.formBuilder,
    ),
    iban: [
      <string | undefined>undefined,
      [Validators.required, ibanValidator()],
    ],
    sozialdienstAdmin: this.formBuilder.group({
      vorname: [<string | null>null, [Validators.required]],
      nachname: [<string | null>null, [Validators.required]],
      email: [
        <string | null>null,
        [Validators.required, Validators.pattern(PATTERN_EMAIL)],
      ],
    }),
  });

  handleSubmit() {
    if (this.idSig()) {
      this.update();
    } else {
      this.create();
    }
  }

  // eslint-disable-next-line @typescript-eslint/no-empty-function
  update() {}

  create() {
    if (this.form.invalid) return;

    const values = convertTempFormToRealValues(this.form);

    const adresse = SharedUiFormAddressComponent.getRealValues(
      this.form.controls.adresse,
    );

    const sozialdienstAdmin = convertTempFormToRealValues(
      this.form.controls.sozialdienstAdmin,
    );

    const createRequest = {
      ...values,
      iban: 'CH' + values.iban,
      adresse,
      sozialdienstAdmin: {
        ...sozialdienstAdmin,
      },
    };

    this.store.createSozialdienst$({
      sozialdienstCreate: createRequest,
      onAfterSave: (sozialdienstId) => {
        this.router.navigate(['..', 'edit', sozialdienstId], {
          relativeTo: this.route,
          replaceUrl: true,
        });
      },
    });
  }

  trimEmail() {
    const email = this.form.controls.sozialdienstAdmin.controls.email;
    email.setValue(email.value?.trim() ?? null);
  }

  ngOnDestroy() {
    this.store.resetSozialdienst();
  }
}
