import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  Signal,
  computed,
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
import { MatChipsModule } from '@angular/material/chips';
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
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { ActivatedRoute, Router } from '@angular/router';
import { MaskitoDirective } from '@maskito/angular';
import {
  TranslateDirective,
  TranslatePipe,
  TranslateService,
} from '@ngx-translate/core';

import { GesuchsperiodeStore } from '@dv/sachbearbeitung-app/data-access/gesuchsperiode';
import { GesuchsperiodeWithDaten } from '@dv/shared/model/gesuch';
import {
  SharedUiFormFieldDirective,
  SharedUiFormMessageErrorDirective,
  SharedUiFormReadonlyDirective,
  SharedUiFormSaveComponent,
} from '@dv/shared/ui/form';
import { SharedUiHeaderSuffixDirective } from '@dv/shared/ui/header-suffix';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import {
  SharedUiRdIsPendingPipe,
  SharedUiRdIsPendingWithoutCachePipe,
} from '@dv/shared/ui/remote-data-pipe';
import { TranslatedPropertyPipe } from '@dv/shared/ui/translated-property-pipe';
import { provideDvDateAdapter } from '@dv/shared/util/date-adapter';
import {
  SharedUtilFormService,
  convertTempFormToRealValues,
} from '@dv/shared/util/form';
import { maskitoNumber, maskitoYear } from '@dv/shared/util/maskito-util';
import { isPending, pending, success } from '@dv/shared/util/remote-data';
import { observeUnsavedChanges } from '@dv/shared/util/unsaved-changes';

import { PublishComponent } from '../publish/publish.component';

@Component({
  standalone: true,
  imports: [
    CommonModule,
    MaskitoDirective,
    MatError,
    MatFormFieldModule,
    MatHint,
    MatInputModule,
    MatSelectModule,
    TranslateDirective,
    TranslatePipe,
    ReactiveFormsModule,
    SharedUiFormFieldDirective,
    SharedUiFormMessageErrorDirective,
    SharedUiFormSaveComponent,
    SharedUiFormReadonlyDirective,
    SharedUiLoadingComponent,
    SharedUiRdIsPendingPipe,
    SharedUiRdIsPendingWithoutCachePipe,
    SharedUiHeaderSuffixDirective,
    SharedUiMaxLengthDirective,
    PublishComponent,
    TranslatedPropertyPipe,
    MatDatepicker,
    MatDatepickerToggle,
    MatDatepickerInput,
    MatDatepickerApply,
    MatChipsModule,
  ],
  templateUrl: './gesuchsperiode-detail.component.html',
  providers: [provideDvDateAdapter()],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GesuchsperiodeDetailComponent {
  private formBuilder = inject(NonNullableFormBuilder);
  private formUtils = inject(SharedUtilFormService);
  private elementRef = inject(ElementRef);
  store = inject(GesuchsperiodeStore);
  router = inject(Router);
  route = inject(ActivatedRoute);
  translate = inject(TranslateService);
  maskitoYear = maskitoYear;
  maskitoNumber = maskitoNumber;
  idSig = input.required<string | undefined>({ alias: 'id' });
  unsavedChangesSig: Signal<boolean>;

  maxZweiterAuszahlungsterminMonat = 11;
  minZweiterAuszahlungsterminTag = 1;
  maxZweiterAuszahlungsterminTag = 28;
  form = this.formBuilder.group({
    bezeichnungDe: [<string | null>null, [Validators.required]],
    bezeichnungFr: [<string | null>null, [Validators.required]],
    fiskaljahr: [<string | null>null, [Validators.required]],
    gesuchsjahrId: [<string | null>null, [Validators.required]],
    gesuchsperiodeStart: [<string | null>null, [Validators.required]],
    gesuchsperiodeStopp: [<string | null>null, [Validators.required]],
    aufschaltterminStart: [<string | null>null, [Validators.required]],
    aufschaltterminStopp: [<string | null>null, [Validators.required]],
    einreichefristNormal: [<string | null>null, [Validators.required]],
    einreichefristReduziert: [<string | null>null, [Validators.required]],
    zweiterAuszahlungsterminMonat: [<number | null>null, [Validators.required]],
    zweiterAuszahlungsterminTag: [<number | null>null, [Validators.required]],
    ausbKosten_SekII: [<string | null>null, [Validators.required]],
    ausbKosten_Tertiaer: [<string | null>null, [Validators.required]],
    freibetrag_vermoegen: [<string | null>null, [Validators.required]],
    freibetrag_erwerbseinkommen: [<string | null>null, [Validators.required]],
    einkommensfreibetrag: [<string | null>null, [Validators.required]],
    elternbeteiligungssatz: [<string | null>null, [Validators.required]],
    vermogenSatzAngerechnet: [<string | null>null, [Validators.required]],
    integrationszulage: [<string | null>null, [Validators.required]],
    limite_EkFreibetrag_Integrationszulage: [
      <string | null>null,
      [Validators.required],
    ],
    stipLimite_Minimalstipendium: [<string | null>null, [Validators.required]],
    reduzierungDesGrundbedarfs: [<string | null>null, [Validators.required]],
    person_1: [<string | null>null, [Validators.required]],
    personen_2: [<string | null>null, [Validators.required]],
    personen_3: [<string | null>null, [Validators.required]],
    personen_4: [<string | null>null, [Validators.required]],
    personen_5: [<string | null>null, [Validators.required]],
    personen_6: [<string | null>null, [Validators.required]],
    personen_7: [<string | null>null, [Validators.required]],
    proWeiterePerson: [<string | null>null, [Validators.required]],
    kinder_00_18: [<string | null>null, [Validators.required]],
    jugendliche_erwachsene_19_25: [<string | null>null, [Validators.required]],
    erwachsene_26_99: [<string | null>null, [Validators.required]],
    wohnkosten_fam_1pers: [<string | null>null, [Validators.required]],
    wohnkosten_fam_2pers: [<string | null>null, [Validators.required]],
    wohnkosten_fam_3pers: [<string | null>null, [Validators.required]],
    wohnkosten_fam_4pers: [<string | null>null, [Validators.required]],
    wohnkosten_fam_5pluspers: [<string | null>null, [Validators.required]],
    wohnkosten_persoenlich_1pers: [<string | null>null, [Validators.required]],
    wohnkosten_persoenlich_2pers: [<string | null>null, [Validators.required]],
    wohnkosten_persoenlich_3pers: [<string | null>null, [Validators.required]],
    wohnkosten_persoenlich_4pers: [<string | null>null, [Validators.required]],
    wohnkosten_persoenlich_5pluspers: [
      <string | null>null,
      [Validators.required],
    ],
    preisProMahlzeit: [<string | null>null, [Validators.required]],
    maxSaeule3a: [<string | null>null, [Validators.required]],
    anzahlWochenLehre: [<string | null>null, [Validators.required]],
    anzahlWochenSchule: [<string | null>null, [Validators.required]],
    vermoegensanteilInProzent: [<string | null>null, [Validators.required]],
    limiteAlterAntragsstellerHalbierungElternbeitrag: [
      <number | null>null,
      [Validators.required],
    ],
  });

  gesuchsjahrChangedSig = toSignal(
    this.form.controls.gesuchsjahrId.valueChanges,
  );
  publishBlockedReasonSig = computed(() => {
    const gesuchsjahrId =
      this.gesuchsjahrChangedSig() ??
      this.store.currentGesuchsperiode.data()?.gesuchsjahrId;
    const gesuchsjahr = this.store.gesuchsjahre
      .data()
      ?.find((g) => g.id === gesuchsjahrId);

    if (
      isPending(this.store.currentGesuchsperiode()) ||
      isPending(this.store.gesuchsjahre())
    ) {
      return pending();
    }

    if (!gesuchsjahr) {
      return success(
        'sachbearbeitung-app.admin.gesuchsperiode.publishBlockedReason.noGesuchsjahr',
      );
    }

    if (gesuchsjahr.gueltigkeitStatus === 'ENTWURF') {
      return success(
        'sachbearbeitung-app.admin.gesuchsperiode.publishBlockedReason.gesuchsjahrEntwurf',
      );
    }
    return null;
  });

  private numberConverter = this.formUtils.createNumberConverter(this.form, [
    'fiskaljahr',
    'ausbKosten_SekII',
    'ausbKosten_Tertiaer',
    'freibetrag_vermoegen',
    'freibetrag_erwerbseinkommen',
    'elternbeteiligungssatz',
    'einkommensfreibetrag',
    'vermogenSatzAngerechnet',
    'integrationszulage',
    'limite_EkFreibetrag_Integrationszulage',
    'stipLimite_Minimalstipendium',
    'reduzierungDesGrundbedarfs',
    'person_1',
    'personen_2',
    'personen_3',
    'personen_4',
    'personen_5',
    'personen_6',
    'personen_7',
    'proWeiterePerson',
    'kinder_00_18',
    'jugendliche_erwachsene_19_25',
    'erwachsene_26_99',
    'wohnkosten_fam_1pers',
    'wohnkosten_fam_2pers',
    'wohnkosten_fam_3pers',
    'wohnkosten_fam_4pers',
    'wohnkosten_fam_5pluspers',
    'wohnkosten_persoenlich_1pers',
    'wohnkosten_persoenlich_2pers',
    'wohnkosten_persoenlich_3pers',
    'wohnkosten_persoenlich_4pers',
    'wohnkosten_persoenlich_5pluspers',
    'preisProMahlzeit',
    'maxSaeule3a',
    'anzahlWochenLehre',
    'anzahlWochenSchule',
    'vermoegensanteilInProzent',
  ]);

  constructor() {
    this.unsavedChangesSig = toSignal(observeUnsavedChanges(this.form), {
      initialValue: false,
    });
    this.formUtils.registerFormForUnsavedCheck(this);
    effect(
      () => {
        const id = this.idSig();
        this.store.loadAllGesuchsjahre$();
        if (id) {
          this.store.loadGesuchsperiode$(id);
        } else {
          this.store.loadLatestGesuchsperiode$();
        }
      },
      { allowSignalWrites: true },
    );
    effect(() => {
      const gesuchsJahre = this.store.gesuchsjahre.data();
      if (!gesuchsJahre) {
        this.form.controls.gesuchsjahrId.disable();
      } else {
        this.form.controls.gesuchsjahrId.enable();
      }
    });
    effect(() => {
      const gesuchsperiode = this.store.currentGesuchsperiodeViewSig();
      if (!gesuchsperiode) {
        const latestGesuchsperiode = this.store.latestGesuchsperiodeViewSig();
        if (latestGesuchsperiode) {
          const removeMetadaten = unsetGivenProperties<GesuchsperiodeWithDaten>(
            [
              'bezeichnungDe',
              'bezeichnungFr',
              'fiskaljahr',
              'gesuchsjahrId',
              'gesuchsperiodeStart',
              'gesuchsperiodeStopp',
              'aufschaltterminStart',
              'aufschaltterminStopp',
              'einreichefristNormal',
              'einreichefristReduziert',
              'zweiterAuszahlungsterminMonat',
              'zweiterAuszahlungsterminTag',
            ],
          );
          this.form.patchValue({
            ...latestGesuchsperiode,
            ...this.numberConverter.toString(latestGesuchsperiode),
            ...removeMetadaten,
          });
        }
        return;
      }
      this.form.patchValue({
        ...gesuchsperiode,
        ...this.numberConverter.toString(gesuchsperiode),
      });
    });
  }

  handleSave(config?: { shouldPublishAfterSave: boolean }) {
    this.form.markAllAsTouched();
    this.formUtils.focusFirstInvalid(this.elementRef);
    if (this.form.invalid) {
      return;
    }
    const formValues = convertTempFormToRealValues(this.form);
    const isNew = !this.idSig();
    this.store.saveGesuchsperiode$({
      gesuchsperiodeId: this.idSig(),
      gesuchsperiodenDaten: {
        ...formValues,
        ...this.numberConverter.toNumber(formValues),
      },
      onAfterSave: (gesuchsjahr) => {
        this.form.markAsPristine();
        if (isNew) {
          this.router.navigate(['..', gesuchsjahr.id], {
            relativeTo: this.route,
          });
        }
        if (config?.shouldPublishAfterSave) {
          this.store.publishGesuchsperiode$(gesuchsjahr.id);
        }
      },
    });
  }

  publish(id: string) {
    if (!this.unsavedChangesSig()) {
      this.store.publishGesuchsperiode$(id);
      return;
    }

    this.handleSave({ shouldPublishAfterSave: true });
  }
}

const unsetGivenProperties = <T>(keys: (keyof T)[]) => {
  return keys.reduce((acc, key) => {
    return { ...acc, [key]: null };
  }, {});
};
