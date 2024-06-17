import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  inject,
  signal,
} from '@angular/core';
import {
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { TranslateModule } from '@ngx-translate/core';

import { BenutzerverwaltungStore } from '@dv/sachbearbeitung-app/data-access/benutzerverwaltung';
import { SharedModelRoleList } from '@dv/shared/model/benutzer';
import {
  SharedUiFormMessageErrorDirective,
  SharedUiFormReadonlyDirective,
  SharedUiFormSaveComponent,
} from '@dv/shared/ui/form';
import { SharedUiRdIsPendingPipe } from '@dv/shared/ui/remote-data-pipe';
import { convertTempFormToRealValues } from '@dv/shared/util/form';

@Component({
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatListModule,
    TranslateModule,
    SharedUiFormReadonlyDirective,
    SharedUiFormMessageErrorDirective,
    SharedUiFormSaveComponent,
    SharedUiRdIsPendingPipe,
  ],
  templateUrl: './benutzer-erstellung.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class BenutzerErstellungComponent {
  private formBuilder = inject(NonNullableFormBuilder);

  benutzerverwaltungStore = inject(BenutzerverwaltungStore);
  form = this.formBuilder.group({
    name: [<string | null>null, [Validators.required]],
    vorname: [<string | null>null, [Validators.required]],
    email: [<string | null>null, [Validators.required]],
    roles: [<SharedModelRoleList>[], [Validators.required]],
  });
  isReadonly = signal(false);

  constructor() {
    this.benutzerverwaltungStore.loadAvailableRoles$();
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
    this.benutzerverwaltungStore.registerUser$({
      ...values,
      onAfterSave: () => {
        this.isReadonly.set(true);
      },
    });
  }
}
