import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  OnDestroy,
  effect,
  inject,
  input,
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

import { LandStore } from '@dv/shared/data-access/land';
import { selectLanguage } from '@dv/shared/data-access/language';
import { SozialdienstStore } from '@dv/shared/data-access/sozialdienst';
import { MASK_IBAN, PATTERN_EMAIL } from '@dv/shared/model/gesuch';
import { BFSCODE_SCHWEIZ } from '@dv/shared/model/ui-constants';
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
  private landStore = inject(LandStore);

  // eslint-disable-next-line @angular-eslint/no-input-rename
  idSig = input.required<string | undefined>({ alias: 'id' });
  store = inject(SozialdienstStore);

  languageSig = this.globalStore.selectSignal(selectLanguage);

  MASK_IBAN = MASK_IBAN;

  form = this.formBuilder.group({
    name: [<string | null>null, [Validators.required]],
    zahlungsverbindung: this.formBuilder.group({
      adresse: SharedUiFormAddressComponent.buildAddressFormGroup(
        this.formBuilder,
      ),
      iban: [
        <string | undefined>undefined,
        [Validators.required, ibanValidator()],
      ],
      vorname: [<string | null>null, [Validators.required]],
      nachname: [<string | null>null, [Validators.required]],
    }),
    sozialdienstAdmin: this.formBuilder.group({
      email: [
        <string | null>null,
        [Validators.required, Validators.pattern(PATTERN_EMAIL)],
      ],
    }),
  });

  constructor() {
    effect(() => {
      const id = this.idSig();

      if (id) {
        this.store.loadSozialdienst$({ sozialdienstId: id });
        // disable email field since it cannot be updated
        this.form.controls.sozialdienstAdmin.controls.email.disable({
          emitEvent: false,
        });
      } else {
        // set country to CH, since all sozialdienst are in CH
        const laender = this.landStore.landListViewSig();
        const chLandId = laender?.find(
          (l) => l.laendercodeBfs === BFSCODE_SCHWEIZ,
        )?.id;

        if (chLandId) {
          this.form.controls.zahlungsverbindung.controls.adresse.controls.landId.setValue(
            chLandId,
          );
        }
      }
      this.form.controls.zahlungsverbindung.controls.adresse.controls.landId.disable(
        {
          emitEvent: false,
        },
      );
    });

    effect(() => {
      const sozialdienst = this.store.sozialdienst().data;
      if (sozialdienst) {
        this.form.patchValue({
          name: sozialdienst.name,
          zahlungsverbindung: {
            iban: sozialdienst.zahlungsverbindung.iban?.substring(2),
            vorname: sozialdienst.sozialdienstAdmin.vorname,
            nachname: sozialdienst.sozialdienstAdmin.nachname,
          },
          sozialdienstAdmin: {
            email: sozialdienst.sozialdienstAdmin.email,
          },
        });
        SharedUiFormAddressComponent.patchForm(
          this.form.controls.zahlungsverbindung.controls.adresse,
          sozialdienst.zahlungsverbindung.adresse,
        );
      }
    });
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

    const zahlungsverbindung = convertTempFormToRealValues(
      this.form.controls.zahlungsverbindung,
    );

    const adresse = SharedUiFormAddressComponent.getRealValues(
      this.form.controls.zahlungsverbindung.controls.adresse,
    );

    const sozialdienstAdmin = convertTempFormToRealValues(
      this.form.controls.sozialdienstAdmin,
    );

    const updateRequest = {
      ...values,
      id: sozialdienst.id,
      name: values.name,
      zahlungsverbindung: {
        ...zahlungsverbindung,
        iban: 'CH' + zahlungsverbindung.iban,
        adresse: {
          ...sozialdienst.zahlungsverbindung.adresse,
          ...adresse,
        },
      },
      sozialdienstAdmin: {
        ...sozialdienstAdmin,
        id: sozialdienst.sozialdienstAdmin.id,
        vorname: zahlungsverbindung.vorname,
        nachname: zahlungsverbindung.nachname,
      },
    };

    this.store.updateSozialdienst$({
      sozialdienst: updateRequest,
    });
  }

  create() {
    if (this.form.invalid) return;

    const values = convertTempFormToRealValues(this.form);

    const zahlungsverbindung = convertTempFormToRealValues(
      this.form.controls.zahlungsverbindung,
    );

    const adresse = SharedUiFormAddressComponent.getRealValues(
      this.form.controls.zahlungsverbindung.controls.adresse,
    );

    const sozialdienstAdmin = convertTempFormToRealValues(
      this.form.controls.sozialdienstAdmin,
    );

    const createRequest = {
      name: values.name,
      zahlungsverbindung: {
        ...zahlungsverbindung,
        iban: 'CH' + zahlungsverbindung.iban,
        adresse,
      },
      sozialdienstAdmin: {
        ...sozialdienstAdmin,
        vorname: zahlungsverbindung.vorname,
        nachname: zahlungsverbindung.nachname,
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
