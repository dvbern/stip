import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  Output,
  computed,
  effect,
  inject,
  signal,
  viewChildren,
} from '@angular/core';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
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
import { Observable, merge } from 'rxjs';

import { selectSharedDataAccessConfigsView } from '@dv/shared/data-access/config';
import { DarlehenStore } from '@dv/shared/data-access/darlehen';
import { PermissionStore } from '@dv/shared/global/permission';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import {
  DarlehenDokumentType,
  DarlehenGrund,
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
  SharedUiFormReadonlyDirective,
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
import { observeUnsavedChanges } from '@dv/shared/util/unsaved-changes';

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
    SharedUiFormReadonlyDirective,
  ],
  templateUrl: './shared-feature-darlehen.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureDarlehenComponent {
  @Output() formIsUnsaved: Observable<boolean>;

  private formBuilder = inject(NonNullableFormBuilder);
  private formUtils = inject(SharedUtilFormService);
  private dialog = inject(MatDialog);
  private elementRef = inject(ElementRef);
  private route = inject(ActivatedRoute);
  private compileTimeConfig = inject(SharedModelCompileTimeConfig);
  private permissionStore = inject(PermissionStore);

  private store = inject(Store);
  private config = this.store.selectSignal(selectSharedDataAccessConfigsView);

  private documentUploadsSig = viewChildren(
    SharedPatternDocumentUploadComponent,
  );

  darlehenStore = inject(DarlehenStore);
  darlehenSig = this.darlehenStore.cachedDarlehen.data;

  maskitoNumber = maskitoNumber;

  darlehenPermissionsSig = computed(() => {
    return getDarlehenPermissions(
      // todo: can we have the status not being optional?
      this.darlehenSig()?.status,
      this.compileTimeConfig.appType,
      this.permissionStore.rolesMapSig(),
    ).permissions;
  });

  // todo: add delegation as well, as input, so the permissions can be calculated correctly
  private createUploadOptionsSig = createDarlehenUploadOptionsFactory({
    darlehenId: this.darlehenSig()?.id,
    allowTypes: this.config().deploymentConfig?.allowedMimeTypes?.join(','),
    permissions: this.darlehenPermissionsSig(),
  });

  // Todo: error is not shown yet, or not anymore
  private atLeastOneCheckboxChecked: ValidatorFn = (
    control: AbstractControl,
  ) => {
    const checked = Object.values(control.value).some((v) => v === true);
    return checked ? null : { atLeastOneCheckboxChecked: true };
  };

  documentsHaveChanged = signal(false);

  isAllDocumentsUploadedSig = computed<boolean>(() => {
    this.documentsHaveChanged();
    const dokumentUploads = this.documentUploadsSig();
    return dokumentUploads.every((upload) => {
      return upload.gesuchDokumentSig()?.gesuchDokument?.dokumente?.length;
    });
  });

  formSb = this.formBuilder.group({
    gewaehren: [<boolean | null>null, [Validators.required]],
    betrag: [<string | null>null, [Validators.required]],
    kommentar: [<string | null>null, [Validators.required]],
  });

  formGs = this.formBuilder.group(
    {
      betragGewuenscht: [<string | null>null, [Validators.required]],
      schulden: [<string | null>null, [Validators.required]],
      anzahlBetreibungen: [<number | null>null, [Validators.required]],
      gruende: this.formBuilder.group(
        {
          ANSCHAFFUNGEN_FUER_AUSBILDUNG: [<boolean | undefined>undefined],
          AUSBILDUNG_ZWOELF_JAHRE: [<boolean | undefined>undefined],
          HOHE_GEBUEHREN: [<boolean | undefined>undefined],
          NICHT_BERECHTIGT: [<boolean | undefined>undefined],
          ZWEITAUSBILDUNG: [<boolean | undefined>undefined],
        } satisfies Record<DarlehenGrund, [boolean | undefined]>,
        { validators: [this.atLeastOneCheckboxChecked] },
      ),
    },
    // todo: make this work, but this approach was not best
    // { validators: [this.allDocumentsValidator] },
  );

  gewaehrenChangedSig = toSignal(this.formSb.controls.gewaehren.valueChanges);
  showBetragFieldSig = computed(() => {
    const gewaehren = this.gewaehrenChangedSig();

    if (!gewaehren) {
      this.formSb.controls.betrag.disable();
    } else {
      this.formSb.controls.betrag.enable();
    }
    this.formSb.controls.betrag.updateValueAndValidity();

    return !!gewaehren;
  });

  anzahlBetreibungenChangedSig = toSignal(
    this.formGs.controls.anzahlBetreibungen.valueChanges,
  );

  grundNichtBerechtigtChangedSig = toSignal(
    this.formGs.controls.gruende.controls.NICHT_BERECHTIGT.valueChanges,
  );
  grundHoheGebuehrenChangedSig = toSignal(
    this.formGs.controls.gruende.controls.HOHE_GEBUEHREN.valueChanges,
  );
  grundAnschaffungenFuerAusbildungChangedSig = toSignal(
    this.formGs.controls.gruende.controls.ANSCHAFFUNGEN_FUER_AUSBILDUNG
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

  hasUnsavedChanges = false;
  gsFormSavedSig = signal(false);
  sbFormSavedSig = signal(false);

  constructor() {
    this.formIsUnsaved = merge(
      observeUnsavedChanges(this.formGs, toObservable(this.gsFormSavedSig)),
      observeUnsavedChanges(this.formSb, toObservable(this.sbFormSavedSig)),
    );
    this.formUtils.registerFormForUnsavedCheck(this);

    // patch form value
    effect(() => {
      const darlehen = this.darlehenSig();
      if (!darlehen) {
        return;
      }
      // todo: handle null values?
      this.formSb.patchValue({
        gewaehren: darlehen.gewaehren,
        betrag: darlehen.betrag?.toString(),
        kommentar: darlehen.kommentar,
      });

      this.formGs.patchValue({
        betragGewuenscht: darlehen.betragGewuenscht?.toString(),
        schulden: darlehen.schulden?.toString(),
        anzahlBetreibungen: darlehen.anzahlBetreibungen,
        gruende: Object.fromEntries(
          Object.values(DarlehenGrund).map((grund) => [
            grund,
            darlehen.gruende?.includes(grund) ?? false,
          ]),
        ),
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
      betragGewuenscht: fromFormatedNumber(realValues.betragGewuenscht),
      schulden: fromFormatedNumber(realValues.schulden),
      anzahlBetreibungen: realValues.anzahlBetreibungen,
      gruende: Object.entries(this.formGs.controls.gruende.value)
        .filter(([, value]) => value === true)
        .map(([key]) => key as DarlehenGrund),
    };

    return darlehen;
  }

  // Gesuchsteller Actions
  // darlehenUpdateGs(): void {
  //   this.formGs.markAllAsTouched();
  //   this.formUtils.focusFirstInvalid(this.elementRef);

  //   if (this.formGs.invalid) {
  //     return;
  //   }

  //   const darlehen = this.buildUpdatedGsFrom();

  //   this.darlehenStore.darlehenUpdateGs$({
  //     darlehenId: this.darlehenSig().id,
  //     darlehenUpdateGs: darlehen,
  //   });
  // }

  darlehenEingeben(): void {
    this.formGs.markAllAsTouched();
    this.formUtils.focusFirstInvalid(this.elementRef);
    const darlehen = this.darlehenSig();

    if (this.formGs.invalid || !darlehen || !this.isAllDocumentsUploadedSig()) {
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
          this.darlehenStore.darlehenUpdateAndEingeben$({
            darlehenId: darlehen.id,
            darlehenUpdateGs: this.buildUpdatedGsFrom(),
          });
          this.gsFormSavedSig.set(true);
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
      betrag: fromFormatedNumber(realValues.betrag),
      kommentar: realValues.kommentar,
      gewaehren: realValues.gewaehren,
    };
  }

  darlehenUpdateSb(): void {
    this.formSb.markAllAsTouched();
    this.formUtils.focusFirstInvalid(this.elementRef);
    const darlehen = this.darlehenSig();

    if (this.formSb.invalid || !darlehen) {
      return;
    }

    const updatedDarlehen = this.buildUpdatedSbFrom();

    // todo: add saved signal in after success, if behavior is like this.
    this.darlehenStore.darlehenUpdateSb$({
      darlehenId: darlehen.id,
      darlehenUpdateSb: updatedDarlehen,
    });
  }

  darlehenFreigeben(): void {
    const darlehen = this.darlehenSig();

    if (!darlehen) {
      return;
    }

    SharedUiConfirmDialogComponent.open(this.dialog, {
      title: 'shared.form.darlehen.freigeben.dialog.title',
      message: 'shared.form.darlehen.freigeben.dialog.message',
      cancelText: 'shared.cancel',
      confirmText: 'shared.form.darlehen.freigeben',
    })
      .afterClosed()
      .subscribe((result) => {
        if (result) {
          this.darlehenStore.darlehenFreigeben$({
            darlehenId: darlehen.id,
          });
        }
      });
  }

  darlehenZurueckweisen(): void {
    const darlehen = this.darlehenSig();

    if (!darlehen) {
      return;
    }

    SharedUiKommentarDialogComponent.open(this.dialog, {
      entityId: darlehen.id,
      titleKey: 'shared.form.darlehen.zurueckweisen.dialog.title',
      messageKey: 'shared.form.darlehen.zurueckweisen.dialog.message',
      placeholderKey: 'shared.form.darlehen.zurueckweisen.dialog.placeholder',
      confirmKey: 'shared.form.darlehen.zurueckweisen',
    })
      .afterClosed()
      .subscribe((result) => {
        if (result) {
          this.darlehenStore.darlehenZurueckweisen$({
            darlehenId: result.entityId,
            kommentar: { text: result.kommentar },
          });
        }
      });
  }

  // Freigabestelle Actions

  darlehenAkzeptieren(): void {
    const darlehen = this.darlehenSig();

    if (!darlehen) {
      return;
    }

    SharedUiConfirmDialogComponent.open(this.dialog, {
      title: 'shared.form.darlehen.akzeptieren.dialog.title',
      message: 'shared.form.darlehen.akzeptieren.dialog.message',
      cancelText: 'shared.cancel',
      confirmText: 'shared.form.darlehen.akzeptieren',
    })
      .afterClosed()
      .subscribe((result) => {
        if (result) {
          this.darlehenStore.darlehenAkzeptieren$({
            darlehenId: darlehen.id,
          });
        }
      });
  }

  darlehenAblehnen(): void {
    const darlehen = this.darlehenSig();

    if (!darlehen) {
      return;
    }

    SharedUiConfirmDialogComponent.open(this.dialog, {
      title: 'shared.form.darlehen.ablehnen.dialog.title',
      message: 'shared.form.darlehen.ablehnen.dialog.message',
      cancelText: 'shared.cancel',
      confirmText: 'shared.form.darlehen.ablehnen',
    })
      .afterClosed()
      .subscribe((result) => {
        if (result) {
          this.darlehenStore.darlehenAblehnen$({
            darlehenId: darlehen.id,
          });
        }
      });
  }
}
