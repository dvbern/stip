// noinspection PointlessBooleanExpressionJS

import {
  animate,
  state,
  style,
  transition,
  trigger,
} from '@angular/animations';
import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  OnInit,
  WritableSignal,
  effect,
  inject,
  signal,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import {
  FormControl,
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatRadioModule } from '@angular/material/radio';
import { MatSelectModule } from '@angular/material/select';
import { MaskitoModule } from '@maskito/angular';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';
import { Subject } from 'rxjs';

import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import { SharedEventGesuchFormFamiliensituation } from '@dv/shared/event/gesuch-form-familiensituation';
import {
  DokumentTyp,
  ElternAbwesenheitsGrund,
  ElternUnbekanntheitsGrund,
  Elternschaftsteilung,
  GesuchFormularUpdate,
} from '@dv/shared/model/gesuch';
import { FAMILIENSITUATION } from '@dv/shared/model/gesuch-form';
import {
  SharedPatternDocumentUploadComponent,
  createUploadOptionsFactory,
} from '@dv/shared/pattern/document-upload';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import {
  SharedUiPercentageSplitterComponent,
  numberToPercentString,
  percentStringToNumber,
} from '@dv/shared/ui/percentage-splitter';
import { SharedUiProgressBarComponent } from '@dv/shared/ui/progress-bar';
import { GesuchAppUiStepFormButtonsComponent } from '@dv/shared/ui/step-form-buttons';
import { SharedUiStepperNavigationComponent } from '@dv/shared/ui/stepper-navigation';
import {
  SharedUtilFormService,
  convertTempFormToRealValues,
} from '@dv/shared/util/form';

import {
  FamiliensituationFormStep,
  FamiliensituationFormSteps,
} from './familiensituation-form-steps';

type FamSitStepMeta = {
  [P in keyof FamiliensituationFormSteps]?: FamSitAnimationState;
};
type FamSitAnimationState = 'in' | 'right' | 'left' | 'hidden';

const animationTime = 500;

@Component({
  selector: 'dv-shared-feature-gesuch-form-familiensituation',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    TranslateModule,
    MaskitoModule,
    MatFormFieldModule,
    MatRadioModule,
    MatSelectModule,
    SharedUiProgressBarComponent,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SharedUiPercentageSplitterComponent,
    GesuchAppUiStepFormButtonsComponent,
    SharedUiStepperNavigationComponent,
    SharedUiLoadingComponent,
    SharedPatternDocumentUploadComponent,
  ],
  templateUrl: './shared-feature-gesuch-form-familiensituation.component.html',
  styleUrls: ['./shared-feature-gesuch-form-familiensituation.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  animations: [
    trigger('inOutPaneAnimation', [
      state('in', style({ position: 'relative', left: 0, right: 0 })),
      state('right', style({ left: '100%', display: 'none' })),
      state('left', style({ left: '-100%', display: 'none' })),
      state(
        'hidden',
        style({ position: 'absolute', left: '100%', display: 'none' }),
      ),
      transition('void => *', []),
      transition('* => *', [animate(`${animationTime}ms ease-in`)]),
    ]),
    trigger('hideDuringAnimation', [
      state('hide', style({ opacity: 0 })),
      state('show', style({ opacity: 1 })),
      transition('* => *', [animate(`150ms ease-in`)]),
      transition('void => *', []),
    ]),
  ],
})
export class SharedFeatureGesuchFormFamiliensituationComponent
  implements OnInit
{
  private elementRef = inject(ElementRef);
  private store = inject(Store);
  private formBuilder = inject(NonNullableFormBuilder);
  private formUtils = inject(SharedUtilFormService);

  readonly ELTERNSCHAFTSTEILUNG = Elternschaftsteilung;
  readonly ELTERN_ABWESENHEITS_GRUND = ElternAbwesenheitsGrund;
  readonly ELTERN_UNBEKANNTHEITS_GRUND = ElternUnbekanntheitsGrund;

  hiddenFieldsSetSig = signal(new Set());

  form = this.formBuilder.group({
    elternVerheiratetZusammen: [<boolean | null>null, [Validators.required]],
    gerichtlicheAlimentenregelung: [
      <boolean | undefined>undefined,
      [Validators.required],
    ],
    werZahltAlimente: this.formBuilder.control<
      Elternschaftsteilung | undefined
    >(undefined, { validators: Validators.required }),
    elternteilUnbekanntVerstorben: [
      <boolean | undefined>undefined,
      [Validators.required],
    ],
    mutterUnbekanntVerstorben: this.formBuilder.control<
      ElternAbwesenheitsGrund | undefined
    >(undefined, { validators: Validators.required }),
    vaterUnbekanntVerstorben: this.formBuilder.control<
      ElternAbwesenheitsGrund | undefined
    >(undefined, { validators: Validators.required }),
    mutterUnbekanntGrund: this.formBuilder.control<
      ElternUnbekanntheitsGrund | undefined
    >(undefined, { validators: Validators.required }),
    vaterUnbekanntGrund: this.formBuilder.control<
      ElternUnbekanntheitsGrund | undefined
    >(undefined, { validators: Validators.required }),
    vaterWiederverheiratet: [
      <boolean | undefined>undefined,
      [Validators.required],
    ],
    mutterWiederverheiratet: [
      <boolean | undefined>undefined,
      [Validators.required],
    ],
    sorgerecht: this.formBuilder.control<Elternschaftsteilung | undefined>(
      undefined,
      { validators: Validators.required },
    ),
    obhut: this.formBuilder.control<Elternschaftsteilung | undefined>(
      undefined,
      { validators: Validators.required },
    ),
    obhutMutter: ['', [Validators.required]],
    obhutVater: ['', [Validators.required]],
  });

  duringAnimation: 'show' | 'hide' = 'show';
  viewSig = this.store.selectSignal(selectSharedDataAccessGesuchsView);
  updateValidity$ = new Subject<unknown>();

  stateSig: WritableSignal<FamSitStepMeta> = signal({
    ELTERN_VERHEIRATET_ZUSAMMEN: 'in',
    ALIMENTENREGELUNG: 'right',
  });

  private currentFamiliensituationFormStep =
    FamiliensituationFormSteps.ELTERN_VERHEIRATET_ZUSAMMEN;

  private createUploadOptionsSig = createUploadOptionsFactory(this.viewSig);

  gerichtlicheAlimentenregelungSig = toSignal(
    this.form.controls.gerichtlicheAlimentenregelung.valueChanges,
  );

  mutterUnbekanntGrundSig = toSignal(
    this.form.controls.mutterUnbekanntGrund.valueChanges,
  );

  vaterUnbekanntGrundSig = toSignal(
    this.form.controls.vaterUnbekanntGrund.valueChanges,
  );

  trennungsvereinbarungDocumentSig = this.createUploadOptionsSig(() => {
    const gerichtlicheAlimentenregelung =
      this.gerichtlicheAlimentenregelungSig();

    return gerichtlicheAlimentenregelung
      ? DokumentTyp.FAMILIENSITUATION_TRENNUNGSKONVENTION
      : null;
  });

  vaterUnbekanntDocumentSig = this.createUploadOptionsSig(() => {
    const vaterUnbekanntGrund = this.vaterUnbekanntGrundSig();

    if (
      vaterUnbekanntGrund ===
      ElternUnbekanntheitsGrund.UNBEKANNTER_AUFENTHALTSORT
    ) {
      return DokumentTyp.FAMILIENSITUATION_AUFENTHALT_UNBEKANNT_VATER;
    }

    if (
      vaterUnbekanntGrund === ElternUnbekanntheitsGrund.FEHLENDE_ANERKENNUNG
    ) {
      return DokumentTyp.FAMILIENSITUATION_GEBURTSSCHEIN;
    }

    return null;
  });

  mutterUnbekanntDocumentSig = this.createUploadOptionsSig(() => {
    const mutterUnbekanntGrund = this.mutterUnbekanntGrundSig();

    return mutterUnbekanntGrund ===
      ElternUnbekanntheitsGrund.UNBEKANNTER_AUFENTHALTSORT
      ? DokumentTyp.FAMILIENSITUATION_AUFENTHALT_UNBEKANNT_MUTTER
      : null;
  });

  ngOnInit(): void {
    this.store.dispatch(SharedEventGesuchFormFamiliensituation.init());
  }

  constructor() {
    this.formUtils.registerFormForUnsavedCheck(this);
    Object.values(this.form.controls).forEach((control) => control.disable());
    const {
      elternVerheiratetZusammen,
      gerichtlicheAlimentenregelung,
      werZahltAlimente,
      elternteilUnbekanntVerstorben,
      vaterUnbekanntVerstorben,
      mutterUnbekanntVerstorben,
      obhut,
      vaterUnbekanntGrund,
      mutterUnbekanntGrund,
      mutterWiederverheiratet,
      obhutMutter,
      obhutVater,
      sorgerecht,
      vaterWiederverheiratet,
    } = this.form.controls;

    elternVerheiratetZusammen.enable();
    const elternVerheiratetZusammenSig = toSignal(
      elternVerheiratetZusammen.valueChanges,
    );

    const werZahltAlimenteSig = toSignal(werZahltAlimente.valueChanges);
    const elternteilUnbekanntVerstorbenSig = toSignal(
      elternteilUnbekanntVerstorben.valueChanges,
    );
    const vaterVerstorbenUnbekanntSig = toSignal(
      vaterUnbekanntVerstorben.valueChanges,
    );
    const mutterVerstorbenUnbekanntSig = toSignal(
      mutterUnbekanntVerstorben.valueChanges,
    );
    const obhutSig = toSignal(obhut.valueChanges);

    effect(
      () => {
        const { gesuchFormular } = this.viewSig();
        if (gesuchFormular !== undefined) {
          const initialFormFamSit = gesuchFormular?.familiensituation ?? {};
          this.form.patchValue({
            ...initialFormFamSit,

            obhutMutter: numberToPercentString(
              gesuchFormular?.familiensituation?.obhutMutter,
            ),
            obhutVater: numberToPercentString(
              gesuchFormular?.familiensituation?.obhutVater,
            ),
          });
        } else {
          this.form.reset();
        }
      },
      { allowSignalWrites: true },
    );

    // effect for gerichtlicheAlimentenregelung
    effect(
      () => {
        this.setDisabledStateAndHide(
          gerichtlicheAlimentenregelung,
          elternVerheiratetZusammenSig() !== false,
        );
        this.goNextStep();
      },
      { allowSignalWrites: true },
    );

    effect(
      () => {
        const gerichtlicheAlimentenregelung =
          this.gerichtlicheAlimentenregelungSig();

        this.setDisabledStateAndHide(
          werZahltAlimente,
          gerichtlicheAlimentenregelung !== true,
        );
        this.setDisabledStateAndHide(
          elternteilUnbekanntVerstorben,
          gerichtlicheAlimentenregelung !== false,
        );
        this.goNextStep();
      },
      { allowSignalWrites: true },
    );

    effect(
      () => {
        const elternteilUnbekanntVerstorben =
          elternteilUnbekanntVerstorbenSig();
        this.setDisabledStateAndHide(
          mutterUnbekanntVerstorben,
          elternteilUnbekanntVerstorben !== true,
        );
        this.setDisabledStateAndHide(
          vaterUnbekanntVerstorben,
          elternteilUnbekanntVerstorben !== true,
        );
        this.setDisabledStateAndHide(
          sorgerecht,
          elternteilUnbekanntVerstorben !== false,
        );
        this.setDisabledStateAndHide(
          obhut,
          elternteilUnbekanntVerstorben !== false,
        );
        this.goNextStep();
      },
      { allowSignalWrites: true },
    );

    effect(
      () => {
        const vaterUnbekanntVerstorben = vaterVerstorbenUnbekanntSig();
        this.setDisabledStateAndHide(
          vaterUnbekanntGrund,
          vaterUnbekanntVerstorben !== ElternAbwesenheitsGrund.UNBEKANNT,
        );
      },
      { allowSignalWrites: true },
    );

    effect(
      () => {
        const mutterUnbekanntVerstorben = mutterVerstorbenUnbekanntSig();
        this.setDisabledStateAndHide(
          mutterUnbekanntGrund,
          mutterUnbekanntVerstorben !== ElternAbwesenheitsGrund.UNBEKANNT,
        );
      },
      { allowSignalWrites: true },
    );

    effect(
      () => {
        const notGemeinsam = obhutSig() !== Elternschaftsteilung.GEMEINSAM;
        this.setDisabledStateAndHide(obhutVater, notGemeinsam);
        this.setDisabledStateAndHide(obhutMutter, notGemeinsam);
      },
      { allowSignalWrites: true },
    );

    effect(
      () => {
        const zahltMutterAlimente =
          werZahltAlimenteSig() === Elternschaftsteilung.MUTTER;
        const vaterWederVerstorbenNochUnbekannt =
          vaterVerstorbenUnbekanntSig() === ElternAbwesenheitsGrund.WEDER_NOCH;
        const mutterVerstorbenOderUnbekannt =
          mutterVerstorbenUnbekanntSig() ===
            ElternAbwesenheitsGrund.VERSTORBEN ||
          mutterVerstorbenUnbekanntSig() === ElternAbwesenheitsGrund.UNBEKANNT;
        const keinElternTeilUnbekanntVerstorben =
          elternteilUnbekanntVerstorbenSig() === false;

        const showVaterVerheiratedFrage =
          keinElternTeilUnbekanntVerstorben ||
          zahltMutterAlimente ||
          (mutterVerstorbenOderUnbekannt && vaterWederVerstorbenNochUnbekannt);

        this.setDisabledStateAndHide(
          vaterWiederverheiratet,
          !showVaterVerheiratedFrage,
        );
      },
      { allowSignalWrites: true },
    );

    effect(
      () => {
        const zahltVaterAlimente =
          werZahltAlimenteSig() === Elternschaftsteilung.VATER;
        const mutterWederVerstorbenNochUnbekannt =
          mutterVerstorbenUnbekanntSig() === ElternAbwesenheitsGrund.WEDER_NOCH;
        const vaterVerstorbenOderUnbekannt =
          vaterVerstorbenUnbekanntSig() ===
            ElternAbwesenheitsGrund.VERSTORBEN ||
          vaterVerstorbenUnbekanntSig() === ElternAbwesenheitsGrund.UNBEKANNT;
        const keinElternTeilUnbekanntVerstorben =
          elternteilUnbekanntVerstorbenSig() === false;

        const showMutterVerheiratedFrage =
          keinElternTeilUnbekanntVerstorben ||
          zahltVaterAlimente ||
          (vaterVerstorbenOderUnbekannt && mutterWederVerstorbenNochUnbekannt);

        this.setDisabledStateAndHide(
          mutterWiederverheiratet,
          !showMutterVerheiratedFrage,
        );
      },
      { allowSignalWrites: true },
    );

    effect(
      () => {
        const { readonly } = this.viewSig();
        if (readonly) {
          Object.values(this.form.controls).forEach((control) =>
            control.disable(),
          );
        }
      },
      { allowSignalWrites: true },
    );
  }

  hasPreviousStep(): boolean {
    const familiensituation =
      this.buildSharedModelAdresseFromForm().familiensituation;
    if (familiensituation === undefined) {
      return false;
    }
    return (
      this.currentFamiliensituationFormStep !==
      this.currentFamiliensituationFormStep.getPrevious(familiensituation)
    );
  }

  hasNextStep(): boolean {
    const familiensituation =
      this.buildSharedModelAdresseFromForm().familiensituation;
    if (familiensituation === undefined) {
      return false;
    }
    return (
      this.currentFamiliensituationFormStep !==
      this.currentFamiliensituationFormStep.getNext(familiensituation)
    );
  }

  goPreviousStep(): void {
    const familiensituation =
      this.buildSharedModelAdresseFromForm().familiensituation;
    if (familiensituation === undefined) {
      return;
    }
    const newCurrent =
      this.currentFamiliensituationFormStep.getPrevious(familiensituation);
    const newPrev = newCurrent.getPrevious(familiensituation);
    const newNext = this.currentFamiliensituationFormStep;

    this.updateSteps(newCurrent, newNext, newPrev);
  }

  goNextStep(): void {
    const familiensituation =
      this.buildSharedModelAdresseFromForm().familiensituation;
    if (familiensituation === undefined) {
      return;
    }
    const newCurrent =
      this.currentFamiliensituationFormStep.getNext(familiensituation);
    const newPrev = this.currentFamiliensituationFormStep;
    const newNext = newCurrent.getNext(familiensituation);
    this.updateSteps(newCurrent, newNext, newPrev);
  }

  handleSave(): void {
    this.form.markAllAsTouched();
    this.formUtils.focusFirstInvalid(this.elementRef);
    this.updateValidity$.next({});
    const { gesuch } = this.viewSig();
    if (this.form.valid && gesuch?.id) {
      const gesuchFormular = this.buildSharedModelAdresseFromForm();
      this.store.dispatch(
        SharedEventGesuchFormFamiliensituation.saveTriggered({
          gesuchId: gesuch.id,
          trancheId: gesuch.gesuchTrancheToWorkWith.id,
          gesuchFormular,
          origin: FAMILIENSITUATION,
        }),
      );
      this.form.markAsPristine();
    }
  }

  handleContinue() {
    const { gesuch } = this.viewSig();
    if (gesuch?.id) {
      this.store.dispatch(
        SharedEventGesuchFormFamiliensituation.nextTriggered({
          id: gesuch.id,
          trancheId: gesuch.gesuchTrancheToWorkWith.id,
          origin: FAMILIENSITUATION,
        }),
      );
    }
  }

  private buildSharedModelAdresseFromForm(): GesuchFormularUpdate {
    const { gesuchFormular } = this.viewSig();
    return {
      ...(gesuchFormular ?? {}),
      familiensituation: {
        ...gesuchFormular?.familiensituation,
        ...convertTempFormToRealValues(this.form, [
          'elternVerheiratetZusammen',
        ]),
        // nicht form.value, sonst werden keine Werte auf null gesetzt!
        obhutVater: percentStringToNumber(this.form.getRawValue().obhutVater),
        obhutMutter: percentStringToNumber(this.form.getRawValue().obhutMutter),
      },
    };
  }

  private updateSteps(
    newCurrent: FamiliensituationFormStep,
    newNext: FamiliensituationFormStep,
    newPrev: FamiliensituationFormStep,
  ): void {
    if (this.currentFamiliensituationFormStep === newCurrent) {
      return;
    }

    this.currentFamiliensituationFormStep = newCurrent;

    this.stateSig.set({
      [this.getKeyByValue(newCurrent)]: 'in',
      ...(newCurrent !== newNext && { [this.getKeyByValue(newNext)]: 'right' }),
      ...(newCurrent !== newPrev && { [this.getKeyByValue(newPrev)]: 'left' }),
    });
  }

  private getKeyByValue(
    value: FamiliensituationFormStep,
  ): keyof FamiliensituationFormSteps {
    const key = (
      Object.keys(
        FamiliensituationFormSteps,
      ) as (keyof FamiliensituationFormSteps)[]
    ).find((key) => FamiliensituationFormSteps[key] === value);

    if (key === undefined) {
      throw new Error();
    }

    return key;
  }

  private setDisabledStateAndHide(
    formControl: FormControl,
    disabled: boolean,
  ): void {
    this.formUtils.setDisabledState(formControl, disabled, true);
    this.hiddenFieldsSetSig.update((setToUpdate) => {
      if (disabled) {
        setToUpdate.add(formControl);
      } else {
        setToUpdate.delete(formControl);
      }
      return setToUpdate;
    });
  }
}
