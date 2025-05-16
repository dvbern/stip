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
import { SharedModelRoleList } from '@dv/shared/model/benutzer';
import { PATTERN_EMAIL } from '@dv/shared/model/gesuch';
import { compareById } from '@dv/shared/model/type-util';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
  SharedUiFormSaveComponent,
} from '@dv/shared/ui/form';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import {
  SharedUiRdIsPendingPipe,
  SharedUiRdIsPendingWithoutCachePipe,
} from '@dv/shared/ui/remote-data-pipe';
import { convertTempFormToRealValues } from '@dv/shared/util/form';

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
        SharedUiRdIsPendingWithoutCachePipe,
    ],
    templateUrl: './benutzer-detail.component.html',
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class BenutzeDetailComponent implements OnDestroy {
  private formBuilder = inject(NonNullableFormBuilder);
  idSig = input.required<string | undefined>({ alias: 'id' });
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  store = inject(BenutzerverwaltungStore);
  form = this.formBuilder.group({
    name: [<string | null>null, [Validators.required]],
    vorname: [<string | null>null, [Validators.required]],
    email: [
      <string | null>null,
      [Validators.required, Validators.pattern(PATTERN_EMAIL)],
    ],
    roles: [<SharedModelRoleList>[], [Validators.required]],
  });

  constructor() {
    this.store.loadAvailableRoles$();

    effect(
      () => {
        const id = this.idSig();

        if (id) {
          this.store.loadBenutzerWithRoles$(id);

          // disable email field
          this.form.controls.email.disable({ emitEvent: false });
        }
      },
      { allowSignalWrites: true },
    );

    effect(
      () => {
        const benutzer = this.store.benutzer().data;
        if (benutzer) {
          this.form.patchValue({
            name: benutzer.lastName,
            vorname: benutzer.firstName,
            email: benutzer.email,
            roles: benutzer.roles,
          });
        }
      },
      { allowSignalWrites: true },
    );
  }

  compareById = compareById;

  handleSubmit() {
    if (this.idSig()) {
      this.update();
    } else {
      this.save();
    }
  }

  update() {
    const id = this.idSig();

    if (!id || this.form.invalid) {
      return;
    }

    const values = convertTempFormToRealValues(this.form, ['name', 'vorname']);
    this.store.updateBenutzer$({
      user: {
        lastName: values.name,
        firstName: values.vorname,
        username: this.store.benutzer().data?.username,
        id,
      },
      roles: values.roles,
    });
  }

  save() {
    if (this.form.invalid) {
      return;
    }
    const values = convertTempFormToRealValues(this.form, [
      'email',
      'name',
      'vorname',
    ]);
    this.store.registerUser$({
      ...values,
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

  ngOnDestroy() {
    this.store.resetBenutzer();
  }
}
