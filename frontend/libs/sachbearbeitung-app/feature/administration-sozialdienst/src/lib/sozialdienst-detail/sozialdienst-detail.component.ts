import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  OnDestroy,
  effect,
  inject,
  input,
  signal,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import {
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { ActivatedRoute, Router } from '@angular/router';
import { MaskitoDirective } from '@maskito/angular';
import { Store } from '@ngrx/store';
import { TranslatePipe } from '@ngx-translate/core';

import { selectLanguage } from '@dv/shared/data-access/language';
import { SozialdienstStore } from '@dv/shared/data-access/sozialdienst';
import {
  Land,
  MASK_IBAN,
  PATTERN_EMAIL,
  Sozialdienst,
} from '@dv/shared/model/gesuch';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
  SharedUiFormSaveComponent,
} from '@dv/shared/ui/form';
import { SharedUiFormAddressComponent } from '@dv/shared/ui/form-address';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import { SharedUiRdIsPendingPipe } from '@dv/shared/ui/remote-data-pipe';
import { convertTempFormToRealValues } from '@dv/shared/util/form';
import { ibanValidator } from '@dv/shared/util/validator-iban';

import { ReplaceSozialdienstAdminDialogComponent } from '../replace-sozialdienst-admin-dialog/replace-sozialdienst-admin-dialog.component';

@Component({
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    TranslatePipe,
    MaskitoDirective,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SharedUiFormSaveComponent,
    SharedUiLoadingComponent,
    SharedUiRdIsPendingPipe,
    SharedUiFormAddressComponent,
    SharedUiMaxLengthDirective,
  ],
  templateUrl: './sozialdienst-detail.component.html',
  styleUrl: './sozialdienst-detail.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SozialdienstDetailComponent implements OnDestroy {
  private formBuilder = inject(NonNullableFormBuilder);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private dialog = inject(MatDialog);
  private destroyRef = inject(DestroyRef);
  private globalStore = inject(Store);

  // eslint-disable-next-line @angular-eslint/no-input-rename
  idSig = input.required<string | undefined>({ alias: 'id' });
  store = inject(SozialdienstStore);
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

  constructor() {
    effect(
      () => {
        const id = this.idSig();

        if (id) {
          this.store.loadSozialdienst$({ sozialdienstId: id });
          // disable email field
          this.form.controls.sozialdienstAdmin.controls.email.disable({
            emitEvent: false,
          });
        } else {
          // set country to CH, since all sozialdienst are in CH
          this.form.controls.adresse.controls.land.setValue('CH', {
            emitEvent: false,
          });
        }
        this.form.controls.adresse.controls.land.disable({ emitEvent: false });
      },
      { allowSignalWrites: true },
    );

    effect(
      () => {
        const sozialdienst = this.store.sozialdienst().data;
        if (sozialdienst) {
          this.form.patchValue({
            name: sozialdienst.name,
            iban: sozialdienst.iban?.substring(2),
            sozialdienstAdmin: {
              vorname: sozialdienst.sozialdienstAdmin.vorname,
              nachname: sozialdienst.sozialdienstAdmin.nachname,
              email: sozialdienst.sozialdienstAdmin.email,
            },
          });
          SharedUiFormAddressComponent.patchForm(
            this.form.controls.adresse,
            sozialdienst.adresse,
          );
        }
      },
      { allowSignalWrites: true },
    );
  }

  handleSubmit() {
    if (this.idSig()) {
      this.update();
    } else {
      this.create();
    }
  }

  update() {
    const sozialdienst = this.store.sozialdienst().data;

    if (this.form.invalid || !sozialdienst) return;

    const values = convertTempFormToRealValues(this.form);

    const addressFormValues = SharedUiFormAddressComponent.getRealValues(
      this.form.controls.adresse,
    );

    const sozialdienstAdminFormValues = convertTempFormToRealValues(
      this.form.controls.sozialdienstAdmin,
    );

    const updateRequest: Sozialdienst = {
      ...values,
      id: sozialdienst.id,
      iban: 'CH' + values.iban,
      adresse: {
        ...sozialdienst.adresse,
        ...addressFormValues,
      },
      sozialdienstAdmin: {
        ...sozialdienstAdminFormValues,
        id: sozialdienst.sozialdienstAdmin.id,
      },
    };

    this.store.updateSozialdienst$({
      sozialdienst: updateRequest,
    });
  }

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

  replaceSozialdienstAdmin() {
    const sozialdienstId = this.idSig();

    if (!sozialdienstId) return;

    ReplaceSozialdienstAdminDialogComponent.open(this.dialog)
      .afterClosed()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((result) => {
        if (result) {
          this.store.replaceSozialdienstAdmin$({
            sozialdienstId,
            newUser: result,
          });
        }
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
