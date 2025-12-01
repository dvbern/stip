import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  OnInit,
  inject,
  input,
  signal,
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
import { MatDialog } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatRadioModule } from '@angular/material/radio';
import { RouterLink } from '@angular/router';
import { TranslocoPipe } from '@jsverse/transloco';
import { MaskitoDirective } from '@maskito/angular';

import { DarlehenDokumentType } from '@dv/shared/model/gesuch';
import {
  SharedPatternDocumentUploadComponent,
  createDarlehenUploadOptionsFactory,
} from '@dv/shared/pattern/document-upload';
import { SharedUiConfirmDialogComponent } from '@dv/shared/ui/confirm-dialog';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import { SharedUiIfSachbearbeiterDirective } from '@dv/shared/ui/if-app-type';
import { SharedUiKommentarDialogComponent } from '@dv/shared/ui/kommentar-dialog';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiStepFormButtonsComponent } from '@dv/shared/ui/step-form-buttons';
import {
  SharedUtilFormService,
  convertTempFormToRealValues,
} from '@dv/shared/util/form';
import { maskitoNumber } from '@dv/shared/util/maskito-util';

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
    RouterLink,
    SharedUiLoadingComponent,
    SharedUiFormFieldDirective,
    SharedPatternDocumentUploadComponent,
    SharedUiFormMessageErrorDirective,
    SharedUiStepFormButtonsComponent,
    SharedUiIfSachbearbeiterDirective,
  ],
  templateUrl: './shared-feature-darlehen.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureDarlehenComponent implements OnInit {
  // private store = inject(Store);
  private formBuilder = inject(NonNullableFormBuilder);
  private formUtils = inject(SharedUtilFormService);
  private dialog = inject(MatDialog);
  private elementRef = inject(ElementRef);

  // If this input is set, the component is used in a dialog context
  gesuchIdSig = input.required<string | null>();

  maskitoNumber = maskitoNumber;

  // todo: replace dummy signal with real data from store
  dokumentDummySig = signal({
    darlehenId: 'dummy',
    allowTypes: 'pdf',
    permissions: {},
  });

  // todo: this function will need to be updated
  /* needs {
      darlehenId: string | undefined;
      allowTypes: string | undefined;
      permissions: PermissionMap;
    }
  */
  private createUploadOptionsSig = createDarlehenUploadOptionsFactory(
    this.dokumentDummySig,
  );

  // Todo: error is not shown yet, or not anymore
  private atLeastOneCheckboxChecked: ValidatorFn = (
    control: AbstractControl,
  ) => {
    const checked = Object.values(control.value).some((v) => v === true);
    return checked ? null : { atLeastOneCheckboxChecked: true };
  };

  form = this.formBuilder.group({
    betragDarlehenGewuenscht: [<string | null>null, [Validators.required]],
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
    return DarlehenDokumentType.BETREIBUNGS_AUSZUG;
  });
  grundNichtBerechtigtDocSig = this.createUploadOptionsSig(() => {
    const isGrundNichtBerechtigt = this.grundNichtBerechtigtChangedSig();
    return isGrundNichtBerechtigt
      ? DarlehenDokumentType.AUFSTELLUNG_KOSTEN_ELTERN
      : null;
  });
  grundHoheGebuehrenDocSig = this.createUploadOptionsSig(() => {
    const isGrundHoheGebuehren = this.grundHoheGebuehrenChangedSig();
    return isGrundHoheGebuehren
      ? DarlehenDokumentType.KOPIE_SCHULGELDRECHNUNG
      : null;
  });
  grundAnschaffungenFuerAusbildungDocSig = this.createUploadOptionsSig(() => {
    const isGrundAnschaffungenFuerAusbildung =
      this.grundAnschaffungenFuerAusbildungChangedSig();
    return isGrundAnschaffungenFuerAusbildung
      ? DarlehenDokumentType.BELEGE_ANSCHAFFUNGEN
      : null;
  });

  ngOnInit() {
    // this.store.dispatch(SharedEventGesuchFormDarlehen.init());
  }

  constructor() {}

  private buildUpdatedGesuchFromForm() {
    const formValues = convertTempFormToRealValues(this.form, [
      'betragDarlehenGewuenscht',
      'betragBezogenKanton',
      'schulden',
      'anzahlBetreibungen',
    ]);
  }

  darlehenCreate(): void {
    this.form.markAllAsTouched();
    this.formUtils.focusFirstInvalid(this.elementRef);

    if (this.form.invalid) {
      return;
    }

    SharedUiConfirmDialogComponent.open(this.dialog, {
      title: 'shared.form.darlehen.create.dialog.title',
      message: 'shared.form.darlehen.create.dialog.message',
      cancelText: 'shared.cancel',
      confirmText: 'shared.form.darlehen.create.dialog.confirm',
    })
      .afterClosed()
      .subscribe((result) => {
        if (result) {
          // this.createDarlehen();
        }
      });
  }

  darlehenFreigeben(): void {
    SharedUiConfirmDialogComponent.open(this.dialog, {
      title: 'shared.form.darlehen.freigeben.dialog.title',
      message: 'shared.form.darlehen.freigeben.dialog.message',
      cancelText: 'shared.cancel',
      confirmText: 'shared.form.darlehen.freigeben',
    })
      .afterClosed()
      .subscribe((result) => {
        if (result) {
          // this.freigebenDarlehen();
        }
      });
  }

  darlehenAkzeptieren(): void {
    SharedUiConfirmDialogComponent.open(this.dialog, {
      title: 'shared.form.darlehen.akzeptieren.dialog.title',
      message: 'shared.form.darlehen.akzeptieren.dialog.message',
      cancelText: 'shared.cancel',
      confirmText: 'shared.form.darlehen.akzeptieren',
    })
      .afterClosed()
      .subscribe((result) => {
        if (result) {
          // this.akzeptierenDarlehen();
        }
      });
  }

  darlehenAblehnen(): void {
    SharedUiConfirmDialogComponent.open(this.dialog, {
      title: 'shared.form.darlehen.ablehnen.dialog.title',
      message: 'shared.form.darlehen.ablehnen.dialog.message',
      cancelText: 'shared.cancel',
      confirmText: 'shared.form.darlehen.ablehnen',
    })
      .afterClosed()
      .subscribe((result) => {
        if (result) {
          // this.zurueckweisenDarlehen();
        }
      });
  }

  darlehenZurueckweisen(): void {
    SharedUiKommentarDialogComponent.open(this.dialog, {
      entityId: this.gesuchIdSig()!,
      titleKey: 'shared.form.darlehen.zurueckweisen.dialog.title',
      messageKey: 'shared.form.darlehen.zurueckweisen.dialog.message',
      placeholderKey: 'shared.form.darlehen.zurueckweisen.dialog.placeholder',
      confirmKey: 'shared.form.darlehen.zurueckweisen',
    })
      .afterClosed()
      .subscribe((result) => {
        if (result) {
          // this.zurueckweisenDarlehen();
        }
      });
  }
}
