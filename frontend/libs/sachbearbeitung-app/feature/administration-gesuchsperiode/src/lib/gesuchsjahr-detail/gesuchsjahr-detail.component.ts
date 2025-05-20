import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  Signal,
  effect,
  inject,
  input,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import {
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatError, MatFormFieldModule } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { ActivatedRoute, Router } from '@angular/router';
import { MaskitoDirective } from '@maskito/angular';
import { TranslatePipe } from '@ngx-translate/core';

import { GesuchsperiodeStore } from '@dv/sachbearbeitung-app/data-access/gesuchsperiode';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
  SharedUiFormReadonlyDirective,
  SharedUiFormSaveComponent,
} from '@dv/shared/ui/form';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import {
  SharedUiRdIsPendingPipe,
  SharedUiRdIsPendingWithoutCachePipe,
} from '@dv/shared/ui/remote-data-pipe';
import {
  SharedUtilFormService,
  convertTempFormToRealValues,
} from '@dv/shared/util/form';
import { maskitoYear } from '@dv/shared/util/maskito-util';
import { observeUnsavedChanges } from '@dv/shared/util/unsaved-changes';

import { PublishComponent } from '../publish/publish.component';

@Component({
  imports: [
    CommonModule,
    MaskitoDirective,
    MatError,
    MatFormFieldModule,
    MatInput,
    TranslatePipe,
    ReactiveFormsModule,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SharedUiFormReadonlyDirective,
    SharedUiFormSaveComponent,
    SharedUiLoadingComponent,
    SharedUiRdIsPendingPipe,
    SharedUiRdIsPendingWithoutCachePipe,
    SharedUiMaxLengthDirective,
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
  // eslint-disable-next-line @angular-eslint/no-input-rename
  idSig = input.required<string | undefined>({ alias: 'id' });
  unsavedChangesSig: Signal<boolean>;
  form = this.formBuilder.group({
    bezeichnungDe: [<string | null>null, [Validators.required]],
    bezeichnungFr: [<string | null>null, [Validators.required]],
    technischesJahr: [<string | null>null, [Validators.required]],
  });

  constructor() {
    this.unsavedChangesSig = toSignal(observeUnsavedChanges(this.form), {
      initialValue: false,
    });
    this.formUtils.registerFormForUnsavedCheck(this);
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

  handleSave(config?: { shouldPublishAfterSave: boolean }) {
    this.form.markAllAsTouched();
    this.formUtils.focusFirstInvalid(this.elementRef);
    if (!this.form.valid) {
      return;
    }
    const value = convertTempFormToRealValues(this.form);
    const isNew = !this.idSig();
    this.store.saveGesuchsjahr$({
      gesuchsjahrId: this.idSig(),
      gesuchsjahrDaten: { ...value, technischesJahr: +value.technischesJahr },
      onAfterSave: (gesuchsjahr) => {
        this.form.markAsPristine();
        if (isNew) {
          this.router.navigate(['..', gesuchsjahr.id], {
            relativeTo: this.route,
          });
        }
        if (config?.shouldPublishAfterSave) {
          this.store.publishGesuchsjahr$(gesuchsjahr.id);
        }
      },
    });
  }

  publish(id: string) {
    if (!this.unsavedChangesSig()) {
      this.store.publishGesuchsjahr$(id);
      return;
    }

    this.handleSave({ shouldPublishAfterSave: true });
  }
}
