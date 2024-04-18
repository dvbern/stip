import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  effect,
  inject,
  input,
} from '@angular/core';
import {
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import {
  MatDatepicker,
  MatDatepickerApply,
  MatDatepickerInput,
  MatDatepickerToggle,
} from '@angular/material/datepicker';
import {
  MatError,
  MatFormFieldModule,
  MatHint,
} from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { ActivatedRoute, Router } from '@angular/router';
import { MaskitoModule } from '@maskito/angular';
import { TranslateModule } from '@ngx-translate/core';

import { GesuchsperiodeStore } from '@dv/sachbearbeitung-app/data-access/gesuchsperiode';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
  SharedUiFormReadonlyDirective,
  SharedUiFormSaveComponent,
} from '@dv/shared/ui/form';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import {
  SharedUiRdIsPendingPipe,
  SharedUiRdIsPendingWithoutCachePipe,
} from '@dv/shared/ui/remote-data-pipe';
import {
  SharedUtilFormService,
  convertTempFormToRealValues,
} from '@dv/shared/util/form';
import { maskitoYear } from '@dv/shared/util/maskito-util';

import { PublishComponent } from '../publish/publish.component';

@Component({
  standalone: true,
  imports: [
    CommonModule,
    MaskitoModule,
    MatError,
    MatFormFieldModule,
    MatHint,
    MatInput,
    TranslateModule,
    ReactiveFormsModule,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SharedUiFormReadonlyDirective,
    SharedUiFormSaveComponent,
    SharedUiLoadingComponent,
    SharedUiRdIsPendingPipe,
    SharedUiRdIsPendingWithoutCachePipe,
    MatDatepicker,
    MatDatepickerToggle,
    MatDatepickerInput,
    MatDatepickerApply,
    PublishComponent,
  ],
  templateUrl: './gesuchsjahr-detail.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [],
})
export class GesuchsjahrDetailComponent {
  private formBuilder = inject(NonNullableFormBuilder);
  private formUtils = inject(SharedUtilFormService);
  private elementRef = inject(ElementRef);

  readonly = false;
  masktitoYear = maskitoYear;
  store = inject(GesuchsperiodeStore);
  router = inject(Router);
  route = inject(ActivatedRoute);
  idSig = input.required<string | undefined>({ alias: 'id' });
  form = this.formBuilder.group({
    bezeichnungDe: [<string | null>null, [Validators.required]],
    bezeichnungFr: [<string | null>null, [Validators.required]],
    technischesJahr: [<string | null>null, [Validators.required]],
  });

  constructor() {
    effect(() => {
      const gesuchsjahr = this.store.currentGesuchsjahrViewSig();
      if (!gesuchsjahr) return;

      this.form.patchValue({
        ...gesuchsjahr,
        technischesJahr: gesuchsjahr?.technischesJahr.toString(),
      });
    });
    effect(
      () => {
        const id = this.idSig();
        if (id) {
          this.store.loadGesuchsjahr$(id);
        }
      },
      { allowSignalWrites: true },
    );
  }

  handleSave() {
    this.form.markAllAsTouched();
    this.formUtils.focusFirstInvalid(this.elementRef);
    if (!this.form.valid) {
      return;
    }
    const value = convertTempFormToRealValues(this.form);
    this.store.saveGesuchsjahr$({
      gesuchsjahrId: this.idSig(),
      gesuchsjahrDaten: { ...value, technischesJahr: +value.technischesJahr },
      onAfterSave: (gesuchsjahr) => {
        this.router.navigate(['..', gesuchsjahr.id], {
          relativeTo: this.route,
        });
      },
    });
  }
}
