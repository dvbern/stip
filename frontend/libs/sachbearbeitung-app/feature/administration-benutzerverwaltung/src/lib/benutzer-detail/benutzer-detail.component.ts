import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  OnDestroy,
  effect,
  inject,
  input,
} from '@angular/core';
import {
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';

import { BenutzerverwaltungStore } from '@dv/sachbearbeitung-app/data-access/benutzerverwaltung';
import { BENUTZER_ROLES, BenutzerRole } from '@dv/shared/model/benutzer';
import { PATTERN_EMAIL } from '@dv/shared/model/gesuch';
import { compareById } from '@dv/shared/model/type-util';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
  SharedUiFormSaveComponent,
} from '@dv/shared/ui/form';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import { SharedUiRdIsPendingPipe } from '@dv/shared/ui/remote-data-pipe';
import { convertTempFormToRealValues } from '@dv/shared/util/form';
import { sharedUtilValidatorTelefonNummer } from '@dv/shared/util/validator-telefon-nummer';

@Component({
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatListModule,
    TranslatePipe,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SharedUiFormSaveComponent,
    SharedUiMaxLengthDirective,
    SharedUiLoadingComponent,
    SharedUiRdIsPendingPipe,
  ],
  templateUrl: './benutzer-detail.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class BenutzeDetailComponent implements OnDestroy {
  private formBuilder = inject(NonNullableFormBuilder);
  // eslint-disable-next-line @angular-eslint/no-input-rename
  idSig = input.required<string | undefined>({ alias: 'id' });
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  availableRoles = BENUTZER_ROLES;
  store = inject(BenutzerverwaltungStore);
  form = this.formBuilder.group({
    nachname: [<string | null>null, [Validators.required]],
    vorname: [<string | null>null, [Validators.required]],
    email: [
      <string | null>null,
      [Validators.required, Validators.pattern(PATTERN_EMAIL)],
    ],
    telefonnummer: [
      <string | null>null,
      [Validators.required, sharedUtilValidatorTelefonNummer()],
    ],
    funktionDe: [<string | null>null, [Validators.required]],
    funktionFr: [<string | null>null, [Validators.required]],
    sachbearbeiterRollen: [<BenutzerRole[]>[], [Validators.required]],
  });

  constructor() {
    effect(() => {
      const id = this.idSig();

      if (id) {
        this.store.loadBenutzerWithRoles$(id);

        // disable email field
        this.form.controls.email.disable({ emitEvent: false });
      }
    });

    effect(() => {
      const { roles, ...benutzer } = this.store.benutzer().data ?? {};
      if (benutzer) {
        this.form.patchValue({
          ...benutzer,
          sachbearbeiterRollen: roles?.raw,
        });
      }
    });
  }

  ngOnDestroy() {
    this.store.resetBenutzer();
  }

  compareById = compareById;

  handleSubmit() {
    if (this.idSig()) {
      this.update();
    } else {
      this.save();
    }
  }

  private update() {
    const id = this.idSig();

    if (!id || this.form.invalid) {
      return;
    }

    const values = convertTempFormToRealValues(this.form);
    this.store.updateBenutzer$({
      userId: id,
      user: {
        ...values,
        sachbearbeiterRollen: values.sachbearbeiterRollen,
      },
    });
  }

  private save() {
    if (this.form.invalid) {
      return;
    }
    const values = convertTempFormToRealValues(this.form);
    this.store.registerUser$({
      user: {
        ...values,
        sachbearbeiterRollen: values.sachbearbeiterRollen,
      },
      onAfterSave: (userId) => {
        this.router.navigate(['..', 'edit', userId], {
          relativeTo: this.route,
          replaceUrl: true,
        });
      },
    });
  }

  trimEmail() {
    const email = this.form.controls.email;
    email.setValue(email.value?.trim() ?? null);
  }
}
