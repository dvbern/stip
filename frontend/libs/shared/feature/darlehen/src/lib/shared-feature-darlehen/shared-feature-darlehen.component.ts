import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  computed,
  effect,
  inject,
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
import { ActivatedRoute, RouterLink } from '@angular/router';
import { TranslocoPipe } from '@jsverse/transloco';
import { MaskitoDirective } from '@maskito/angular';
import { Store } from '@ngrx/store';

import { selectSharedDataAccessConfigsView } from '@dv/shared/data-access/config';
import { DarlehenStore } from '@dv/shared/data-access/darlehen';
import { PermissionStore } from '@dv/shared/global/permission';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import {
  DarlehenDokumentType,
  DarlehenUpdateGs,
  DarlehenUpdateSb,
} from '@dv/shared/model/gesuch';
import { getDarlehenPermissions } from '@dv/shared/model/permission-state';
import {
  SharedPatternDocumentUploadComponent,
  createDarlehenUploadOptionsFactory,
} from '@dv/shared/pattern/document-upload';
import { SharedUiConfirmDialogComponent } from '@dv/shared/ui/confirm-dialog';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
} from '@dv/shared/ui/form';
import {
  SharedUiIfGesuchstellerDirective,
  SharedUiIfSachbearbeiterDirective,
} from '@dv/shared/ui/if-app-type';
import { SharedUiKommentarDialogComponent } from '@dv/shared/ui/kommentar-dialog';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import { SharedUiStepFormButtonsComponent } from '@dv/shared/ui/step-form-buttons';
import {
  SharedUtilFormService,
  convertTempFormToRealValues,
} from '@dv/shared/util/form';
import {
  fromFormatedNumber,
  maskitoNumber,
} from '@dv/shared/util/maskito-util';

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
    SharedUiIfGesuchstellerDirective,
    SharedUiMaxLengthDirective,
  ],
  templateUrl: './shared-feature-darlehen.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureDarlehenComponent {
  private formBuilder = inject(NonNullableFormBuilder);
  private formUtils = inject(SharedUtilFormService);
  private dialog = inject(MatDialog);
  private elementRef = inject(ElementRef);
  private route = inject(ActivatedRoute);
  private compileTimeConfig = inject(SharedModelCompileTimeConfig);
  private permissionStore = inject(PermissionStore);

  private store = inject(Store);
  private config = this.store.selectSignal(selectSharedDataAccessConfigsView);

  darlehenStore = inject(DarlehenStore);
  darlehenSig = computed(() => {
    const darlehen = this.darlehenStore.darlehenViewSig().darlehen;
    if (!darlehen) {
      throw new Error('Darlehen Component: No Darlehen present');
    }
    return darlehen;
  });

  maskitoNumber = maskitoNumber;

  // todo: add delegation as well
  private createUploadOptionsSig = createDarlehenUploadOptionsFactory({
    darlehenId: this.darlehenSig().id,
    allowTypes: this.config().deploymentConfig?.allowedMimeTypes?.join(','),
    permissions: getDarlehenPermissions(
      this.darlehenSig().status!,
      this.compileTimeConfig.appType,
      this.permissionStore.rolesMapSig(),
    ).permissions,
  });

  // Todo: error is not shown yet, or not anymore
  private atLeastOneCheckboxChecked: ValidatorFn = (
    control: AbstractControl,
  ) => {
    const checked = Object.values(control.value).some((v) => v === true);
    return checked ? null : { atLeastOneCheckboxChecked: true };
  };

  formSb = this.formBuilder.group({
    betrag: [<string | null>null, [Validators.required]],
    kommentar: [<string | null>null, [Validators.required]],
    gewaehren: [<boolean | null>null, [Validators.required]],
  });

  // todo: how to handle new field betragBezogenKanton?
  formGs = this.formBuilder.group({
    betragGewuenscht: [<string | null>null, [Validators.required]],
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
    this.formGs.controls.gruende.controls.grundNichtBerechtigt.valueChanges,
  );
  grundHoheGebuehrenChangedSig = toSignal(
    this.formGs.controls.gruende.controls.grundHoheGebuehren.valueChanges,
  );
  grundAnschaffungenFuerAusbildungChangedSig = toSignal(
    this.formGs.controls.gruende.controls.grundAnschaffungenFuerAusbildung
      .valueChanges,
  );

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

  constructor() {
    // patch form value
    effect(() => {
      const darlehen = this.darlehenSig();

      this.formGs.patchValue({
        betragGewuenscht: darlehen.betragGewuenscht?.toString(),
        schulden: darlehen.schulden?.toString(),
        anzahlBetreibungen: darlehen.anzahlBetreibungen,
        // todo: gruende
      });
    });
  }

  private buildUpdatedGsFrom() {
    const realValues = convertTempFormToRealValues(this.formGs, [
      'betragGewuenscht',
      'schulden',
      'anzahlBetreibungen',
    ]);

    const darlehen: DarlehenUpdateGs = {
      darlehenBetragGewuenscht: fromFormatedNumber(realValues.betragGewuenscht),
      schulden: fromFormatedNumber(realValues.schulden),
      anzahlBetreibungen: realValues.anzahlBetreibungen,
      grund: undefined,
    };

    return darlehen;
  }

  // Gesuchsteller Actions
  darlehenUpdateGs(): void {
    this.formGs.markAllAsTouched();
    this.formUtils.focusFirstInvalid(this.elementRef);

    if (this.formGs.invalid) {
      return;
    }

    const darlehen = this.buildUpdatedGsFrom();

    this.darlehenStore.darlehenUpdateGs$({
      darlehenId: this.darlehenSig().id,
      darlehenUpdateGs: darlehen,
    });
  }

  darlehenEingeben(): void {
    this.formGs.markAllAsTouched();
    this.formUtils.focusFirstInvalid(this.elementRef);

    if (this.formGs.invalid) {
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

  // Sachbearbeiter Actions
  private buildUpdatedSbFrom(): DarlehenUpdateSb {
    const realValues = convertTempFormToRealValues(this.formSb, [
      'betrag',
      'kommentar',
      'gewaehren',
    ]);
    return {
      darlehenBetrag: fromFormatedNumber(realValues.betrag),
      kommentar: realValues.kommentar,
      darlehenGewaehren: realValues.gewaehren,
    };
  }

  darlehenUpdateSb(): void {
    this.formSb.markAllAsTouched();
    this.formUtils.focusFirstInvalid(this.elementRef);

    if (this.formSb.invalid) {
      return;
    }

    const darlehen = this.buildUpdatedSbFrom();

    this.darlehenStore.darlehenUpdateSb$({
      darlehenId: this.darlehenSig().id,
      darlehenUpdateSb: darlehen,
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

  darlehenZurueckweisen(): void {
    SharedUiKommentarDialogComponent.open(this.dialog, {
      entityId: this.darlehenSig().id,
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

  // Freigabestellen Actions

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
}
