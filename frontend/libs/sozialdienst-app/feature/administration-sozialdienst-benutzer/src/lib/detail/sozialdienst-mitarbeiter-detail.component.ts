import { DOCUMENT } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  OnDestroy,
  OnInit,
  computed,
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
import { ActivatedRoute, Router } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';

import { SozialdienstStore } from '@dv/shared/data-access/sozialdienst';
import { PATTERN_EMAIL } from '@dv/shared/model/gesuch';
import { isDefined } from '@dv/shared/model/type-util';
import {
  SharedUiFormMessageErrorDirective,
  SharedUiFormSaveComponent,
} from '@dv/shared/ui/form';
import { SharedUiHeaderSuffixDirective } from '@dv/shared/ui/header-suffix';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import {
  SharedUiRdIsPendingPipe,
  SharedUiRdIsPendingWithoutCachePipe,
} from '@dv/shared/ui/remote-data-pipe';
import { convertTempFormToRealValues } from '@dv/shared/util/form';
import { getCurrentUrl } from '@dv/shared/util-fn/keycloak-helper';

@Component({
  imports: [
    ReactiveFormsModule,
    TranslatePipe,
    MatInputModule,
    MatFormFieldModule,
    SharedUiRdIsPendingPipe,
    SharedUiRdIsPendingWithoutCachePipe,
    SharedUiLoadingComponent,
    SharedUiFormMessageErrorDirective,
    SharedUiFormSaveComponent,
    SharedUiHeaderSuffixDirective,
  ],
  templateUrl: './sozialdienst-mitarbeiter-detail.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SozialdienstMitarbeiterDetailComponent
  implements OnInit, OnDestroy
{
  idSig = input<string | undefined>(undefined, { alias: 'id' });

  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private document = inject(DOCUMENT);
  private formBuilder = inject(NonNullableFormBuilder);

  store = inject(SozialdienstStore);
  form = this.formBuilder.group({
    nachname: [<string | null>null, [Validators.required]],
    vorname: [<string | null>null, [Validators.required]],
    email: [
      <string | null>null,
      [Validators.required, Validators.pattern(PATTERN_EMAIL)],
    ],
  });
  modeSig = computed<'edit' | 'create'>(() => {
    return isDefined(this.idSig()) ? 'edit' : 'create';
  });

  constructor() {
    effect(() => {
      const benutzer = this.store.sozialdienstBenutzer();

      if (!benutzer.data) {
        return;
      }

      this.form.controls.email.disable();
      this.form.patchValue(benutzer.data);
    });
  }

  ngOnInit() {
    const id = this.idSig();
    if (isDefined(id)) {
      this.store.loadSozialdienstBenutzer$({
        sozialdienstBenutzerId: id,
      });
    }
  }

  ngOnDestroy() {
    this.store.resetSozialdienstBenutzerCache();
  }

  trimEmail() {
    const email = this.form.controls.email;
    email.setValue(email.value?.trim() ?? null);
  }

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

    const values = convertTempFormToRealValues(this.form, [
      'nachname',
      'vorname',
    ]);
    this.store.updateSozialdienstBenutzer$({
      sozialdienstBenutzerUpdate: {
        nachname: values.nachname,
        vorname: values.vorname,
        id,
      },
    });
  }

  private save() {
    if (this.form.invalid) {
      return;
    }
    const values = convertTempFormToRealValues(this.form, [
      'email',
      'nachname',
      'vorname',
    ]);
    this.store.createSozialdienstBenutzer$({
      sozialdienstBenutzerCreate: {
        ...values,
        redirectUri: getCurrentUrl(this.document),
      },
      onAfterSave: (sozialdienstBenutzerId) => {
        this.router.navigate(['..', 'edit', sozialdienstBenutzerId], {
          relativeTo: this.route,
          replaceUrl: true,
        });
      },
    });
  }
}
