import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  OnInit,
  inject,
  input,
  output,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import {
  AbstractControl,
  NonNullableFormBuilder,
  ReactiveFormsModule,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatRadioModule } from '@angular/material/radio';
import { TranslocoPipe } from '@jsverse/transloco';
import { MaskitoDirective } from '@maskito/angular';
import { Store } from '@ngrx/store';

import { EinreichenStore } from '@dv/shared/data-access/einreichen';
import { DokumentTyp } from '@dv/shared/model/gesuch';
import {
  SharedPatternDocumentUploadComponent,
  createUploadOptionsFactory,
} from '@dv/shared/pattern/document-upload';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiStepFormButtonsComponent } from '@dv/shared/ui/step-form-buttons';
import {
  SharedUtilFormService,
  convertTempFormToRealValues,
} from '@dv/shared/util/form';
import { maskitoNumber } from '@dv/shared/util/maskito-util';

import { selectSharedFeatureGesuchFormDarlehenView } from './shared-feature-darlehen.selector';

@Component({
  selector: 'dv-shared-feature-darlehen',
  imports: [
    CommonModule,
    TranslocoPipe,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatCheckboxModule,
    MatRadioModule,
    MaskitoDirective,
    SharedUiLoadingComponent,
    SharedUiFormFieldDirective,
    SharedPatternDocumentUploadComponent,
    SharedUiFormMessageErrorDirective,
    SharedUiStepFormButtonsComponent,
  ],
  templateUrl: './shared-feature-darlehen.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureDarlehenComponent implements OnInit {
  private store = inject(Store);
  private formBuilder = inject(NonNullableFormBuilder);
  private formUtils = inject(SharedUtilFormService);
  private einreichenStore = inject(EinreichenStore);
  private elementRef = inject(ElementRef);

  // If this input is set, the component is used in a dialog context
  gesuchIdSig = input.required<string | null>();

  darlehenCreated = output<void>();

  viewSig = this.store.selectSignal(selectSharedFeatureGesuchFormDarlehenView);
  maskitoNumber = maskitoNumber;

  /* needs {
      trancheId: string | undefined;
      allowTypes: string | undefined;
      permissions: PermissionMap;
    }
  */
  private createUploadOptionsSig = createUploadOptionsFactory(this.viewSig);

  private atLeastOneCheckboxChecked: ValidatorFn = (
    control: AbstractControl,
  ) => {
    const checked = Object.values(control.value).some((v) => v === true);
    return checked ? null : { atLeastOneCheckboxChecked: true };
  };

  form = this.formBuilder.group({
    betragDarlehen: [<string | null>null, [Validators.required]],
    betragBezogenKanton: [<string | null>null, [Validators.required]],
    schulden: [<string | null>null, [Validators.required]],
    anzahlBetreibungen: [<number | null>null, [Validators.required]],
    gruende: this.formBuilder.group(
      {
        grundNichtBerechtigt: [<boolean | undefined>undefined],
        grundAusbildungZwoelfJahre: [<boolean | undefined>undefined],
        grundHoheGebuehren: [<boolean | undefined>undefined],
        grundAnschaffungenFuerAusbildung: [<boolean | undefined>undefined],
        grundZweitausbildung: [<boolean | undefined>undefined],
      },
      { validators: [this.atLeastOneCheckboxChecked] },
    ),
  });

  grundNichtBerechtigtChangedSig = toSignal(
    this.form.controls.gruende.controls.grundNichtBerechtigt.valueChanges,
  );
  grundHoheGebuehrenChangedSig = toSignal(
    this.form.controls.gruende.controls.grundHoheGebuehren.valueChanges,
  );
  grundAnschaffungenFuerAusbildungChangedSig = toSignal(
    this.form.controls.gruende.controls.grundAnschaffungenFuerAusbildung
      .valueChanges,
  );

  //todo: Falls dialog, das Dokumentenhandling besprechen!
  anzahlBetreibungenDocSig = this.createUploadOptionsSig(() => {
    return DokumentTyp.DARLEHEN_BETREIBUNGSREGISTERAUSZUG;
  });
  grundNichtBerechtigtDocSig = this.createUploadOptionsSig(() => {
    const isGrundNichtBerechtigt = this.grundNichtBerechtigtChangedSig();
    return isGrundNichtBerechtigt
      ? DokumentTyp.DARLEHEN_AUFSTELLUNG_KOSTEN_ELTERN
      : null;
  });
  grundHoheGebuehrenDocSig = this.createUploadOptionsSig(() => {
    const isGrundHoheGebuehren = this.grundHoheGebuehrenChangedSig();
    return isGrundHoheGebuehren
      ? DokumentTyp.DARLEHEN_KOPIE_SCHULGELDRECHNUNG
      : null;
  });
  grundAnschaffungenFuerAusbildungDocSig = this.createUploadOptionsSig(() => {
    const isGrundAnschaffungenFuerAusbildung =
      this.grundAnschaffungenFuerAusbildungChangedSig();
    return isGrundAnschaffungenFuerAusbildung
      ? DokumentTyp.DARLEHEN_BELEGE_ANSCHAFFUNGEN
      : null;
  });

  ngOnInit() {
    // this.store.dispatch(SharedEventGesuchFormDarlehen.init());
  }

  constructor() {
    this.formUtils.observeInvalidFieldsAndMarkControls(
      this.einreichenStore.invalidFormularControlsSig,
      this.form,
    );
  }

  private buildUpdatedGesuchFromForm() {
    const formValues = convertTempFormToRealValues(this.form, [
      'betragDarlehen',
      'betragBezogenKanton',
      'schulden',
      'anzahlBetreibungen',
    ]);
  }

  handleSave(): void {
    this.form.markAllAsTouched();
    this.formUtils.focusFirstInvalid(this.elementRef);

    // const { gesuchId, trancheId, gesuchFormular } =
    //   this.buildUpdatedGesuchFromForm();

    if (this.form.valid) {
      // todo: dispatch save event
    }
  }
}
