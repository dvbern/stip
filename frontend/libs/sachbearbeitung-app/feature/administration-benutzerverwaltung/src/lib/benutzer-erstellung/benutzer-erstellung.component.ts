import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  OnDestroy,
  effect,
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
import { MatListModule } from '@angular/material/list';
import { TranslateModule } from '@ngx-translate/core';

import { BenutzerverwaltungStore } from '@dv/sachbearbeitung-app/data-access/benutzerverwaltung';
import {
  SharedModelRole,
  SharedModelRoleList,
} from '@dv/shared/model/benutzer';
import {
  SharedUiFormMessageErrorDirective,
  SharedUiFormReadonlyDirective,
  SharedUiFormSaveComponent,
} from '@dv/shared/ui/form';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import {
  SharedUiRdIsPendingPipe,
  SharedUiRdIsPendingWithoutCachePipe,
} from '@dv/shared/ui/remote-data-pipe';
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
    SharedUiLoadingComponent,
    SharedUiRdIsPendingPipe,
    SharedUiRdIsPendingWithoutCachePipe,
  ],
  templateUrl: './benutzer-erstellung.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class BenutzerErstellungComponent implements OnDestroy {
  private formBuilder = inject(NonNullableFormBuilder);
  idSig = input.required<string | undefined>({ alias: 'id' });

  store = inject(BenutzerverwaltungStore);
  form = this.formBuilder.group({
    name: [<string | null>null, [Validators.required]],
    vorname: [<string | null>null, [Validators.required]],
    email: [<string | null>null, [Validators.required]],
    roles: [<SharedModelRoleList>[], [Validators.required]],
  });
  isReadonly = signal(false);

  constructor() {
    this.store.loadAvailableRoles$();

    effect(
      () => {
        const id = this.idSig();

        if (id) {
          this.store.loadBenutzer$(id);
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

  handleSubmit() {
    if (this.idSig()) {
      this.update();
    } else {
      this.save();
    }
  }

  update() {
    if (this.form.invalid) {
      return;
    }

    return;

    // const values = convertTempFormToRealValues(this.form, [
    //   'email',
    //   'name',
    //   'vorname',
    // ]);
    // this.store.updateBenutzer$({
    //   ...values,
    //   id: this.idSig(),
    //   onAfterSave: () => {
    //     this.isReadonly.set(true);
    //   },
    // });
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
      onAfterSave: () => {
        this.isReadonly.set(true);
      },
    });
  }

  compareListFn(a: SharedModelRole, b: SharedModelRole) {
    return a.id === b.id;
  }

  ngOnDestroy() {
    this.store.resetBenutzer();
  }
}
