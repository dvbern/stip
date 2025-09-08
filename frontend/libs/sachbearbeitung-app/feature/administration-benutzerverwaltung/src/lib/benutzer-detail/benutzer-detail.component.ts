import {
  ChangeDetectionStrategy,
  Component,
  DOCUMENT,
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
import { TranslocoPipe } from '@jsverse/transloco';

import { BenutzerverwaltungStore } from '@dv/sachbearbeitung-app/data-access/benutzerverwaltung';
import { BENUTZER_ROLES, BenutzerRole } from '@dv/shared/model/benutzer';
import { PATTERN_EMAIL } from '@dv/shared/model/gesuch';
import { getCurrentUrl } from '@dv/shared/model/router';
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
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatListModule,
    TranslocoPipe,
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
  private document = inject(DOCUMENT);
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

  handleSubmit() {
    if (this.idSig()) {
      this.update();
    } else {
      this.create();
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
      user: values,
    });
  }

  private create() {
    if (this.form.invalid) {
      return;
    }
    const values = convertTempFormToRealValues(this.form);
    this.store.registerUser$({
      user: {
        ...values,
        redirectUri: getCurrentUrl(this.document),
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
