import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  inject,
} from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { MatListModule } from '@angular/material/list';
import { Store } from '@ngrx/store';
import { debounceTime, map } from 'rxjs';

import { translatableShared } from '@dv/shared/assets/i18n';
import { selectSharedDataAccessGesuchCache } from '@dv/shared/data-access/gesuch';
import { selectLanguage } from '@dv/shared/data-access/language';
import { VersteckteElternStore } from '@dv/shared/data-access/versteckte-eltern';
import { SharedEventGesuchFormEltern } from '@dv/shared/event/gesuch-form-eltern';
import { GlobalNotificationStore } from '@dv/shared/global/notification';
import { PermissionStore } from '@dv/shared/global/permission';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import {
  ElternTyp,
  ElternUpdate,
  GesuchFormularUpdate,
} from '@dv/shared/model/gesuch';
import { ELTERN, isStepDisabled } from '@dv/shared/model/gesuch-form';
import { preparePermissions } from '@dv/shared/model/permission-state';
import { capitalized, lowercased } from '@dv/shared/model/type-util';
import { INPUT_DELAY } from '@dv/shared/model/ui-constants';
import { SharedUiAdvTranslocoDirective } from '@dv/shared/ui/adv-transloco-directive';
import { SharedUiChangeIndicatorComponent } from '@dv/shared/ui/change-indicator';
import { SharedUiIfSachbearbeiterDirective } from '@dv/shared/ui/if-app-type';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiStepFormButtonsComponent } from '@dv/shared/ui/step-form-buttons';

import { SharedFeatureGesuchFormElternEditorComponent } from '../shared-feature-gesuch-form-eltern-editor/shared-feature-gesuch-form-eltern-editor.component';
import { ElternteilCardComponent } from './elternteil-card/elternteil-card.component';
import { selectSharedFeatureGesuchFormElternView } from './shared-feature-gesuch-form-eltern.selector';

const allEltern = Object.values(ElternTyp);

@Component({
  selector: 'dv-shared-feature-gesuch-form-eltern',
  imports: [
    ReactiveFormsModule,
    CommonModule,
    MatListModule,
    ElternteilCardComponent,
    SharedFeatureGesuchFormElternEditorComponent,
    SharedUiStepFormButtonsComponent,
    SharedUiChangeIndicatorComponent,
    SharedUiLoadingComponent,
    SharedUiAdvTranslocoDirective,
    SharedUiIfSachbearbeiterDirective,
  ],
  templateUrl: './shared-feature-gesuch-form-eltern.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchFormElternComponent {
  private store = inject(Store);
  private permissionStore = inject(PermissionStore);
  private appType = inject(SharedModelCompileTimeConfig).appType;
  private versteckteElternStore = inject(VersteckteElternStore);
  private globalNotificationStore = inject(GlobalNotificationStore);

  shouldHide = this.appType === 'gesuch-app';
  hasUnsavedChanges = false;
  languageSig = this.store.selectSignal(selectLanguage);

  viewSig = this.store.selectSignal(selectSharedFeatureGesuchFormElternView);
  sichtbareElternSig = computed(() => {
    const { gesuchFormular } = this.viewSig();
    const versteckteEltern = gesuchFormular?.versteckteEltern;
    const sichtbareEltern =
      this.sichtbareElternChangedSig()?.value ??
      allEltern.filter((e) => !versteckteEltern?.includes(e));
    return {
      MUTTER: !!sichtbareEltern?.includes('MUTTER'),
      VATER: !!sichtbareEltern?.includes('VATER'),
    };
  });
  cacheSig = this.store.selectSignal(selectSharedDataAccessGesuchCache);
  elternTyps = ElternTyp;
  sichtbareEltern = new FormControl(allEltern, {
    nonNullable: true,
  });
  editedElternteil?: Omit<Partial<ElternUpdate>, 'elternTyp'> &
    Required<Pick<ElternUpdate, 'elternTyp'>>;

  private sichtbareElternChangedSig = toSignal(
    this.sichtbareEltern.valueChanges.pipe(
      map((value) => ({ value })),
      debounceTime(INPUT_DELAY),
    ),
  );

  constructor() {
    this.store.dispatch(SharedEventGesuchFormEltern.init());

    effect(() => {
      const { loading, gesuch, gesuchFormular } = this.viewSig();
      const rolesMap = this.permissionStore.rolesMapSig();
      const { trancheTyp } = this.cacheSig();
      const { permissions } = preparePermissions(
        trancheTyp,
        gesuch,
        this.appType,
        rolesMap,
      );
      const versteckteEltern = gesuchFormular?.versteckteEltern;
      if (versteckteEltern) {
        this.sichtbareEltern.setValue(
          allEltern.filter((e) => !versteckteEltern.includes(e)),
          { emitEvent: false },
        );
      }
      if (
        !loading &&
        gesuch &&
        gesuchFormular &&
        isStepDisabled(ELTERN, gesuch, permissions)
      ) {
        this.store.dispatch(
          SharedEventGesuchFormEltern.nextTriggered({
            id: gesuch?.id,
            origin: ELTERN,
          }),
        );
      }
    });

    effect(() => {
      const gesuchTrancheId = this.viewSig().gesuch?.gesuchTrancheToWorkWith.id;
      const sichtbareEltern = this.sichtbareElternChangedSig()?.value;

      if (gesuchTrancheId && sichtbareEltern) {
        // Convert visible eltern to hidden eltern (inverse)
        const versteckteEltern = allEltern.filter(
          (e) => !sichtbareEltern.includes(e),
        );
        this.versteckteElternStore.saveVersteckteEltern$({
          gesuchTrancheId,
          versteckteEltern: versteckteEltern,
          onSuccess: () => {
            this.globalNotificationStore.createSuccessNotification({
              messageKey: translatableShared(
                'shared.form.eltern.sichtbar.success',
              ),
            });
          },
        });
      }
    });
  }

  trackByIndex(index: number) {
    return index;
  }

  changeElternTypVisibility(elternTyp: ElternTyp, sichtbar: boolean) {
    const currentSichtbar = this.sichtbareEltern.value;
    this.sichtbareEltern.patchValue(
      sichtbar
        ? [...currentSichtbar, elternTyp]
        : currentSichtbar.filter((e) => e !== elternTyp),
    );
  }

  handleEdit(elternteil: ElternUpdate) {
    this.editedElternteil = elternteil;
  }

  handleAddElternteil(elternTyp: ElternTyp) {
    const { gesuchFormular } = this.viewSig();
    this.editedElternteil = setupElternTeil(elternTyp, gesuchFormular);
  }

  handleEditorSave(elternteil: ElternUpdate) {
    const { gesuchId, trancheId, gesuchFormular } =
      this.buildUpdatedGesuchWithUpdatedElternteil(elternteil);
    if (gesuchId && trancheId) {
      this.store.dispatch(
        SharedEventGesuchFormEltern.saveSubformTriggered({
          gesuchId,
          trancheId,
          gesuchFormular,
          origin: ELTERN,
        }),
      );
      this.editedElternteil = undefined;
    }
  }

  public handleDeleteElternteil(id: string) {
    const { gesuchId, trancheId, gesuchFormular } =
      this.buildUpdatedGesuchWithDeletedElternteil(id);
    if (gesuchId && trancheId) {
      this.store.dispatch(
        SharedEventGesuchFormEltern.saveSubformTriggered({
          gesuchId,
          trancheId,
          gesuchFormular,
          origin: ELTERN,
        }),
      );
      this.editedElternteil = undefined;
    }
  }

  handleContinue() {
    const { gesuch } = this.viewSig();
    if (gesuch?.id) {
      this.store.dispatch(
        SharedEventGesuchFormEltern.nextTriggered({
          id: gesuch.id,
          origin: ELTERN,
        }),
      );
    }
  }

  handleEditorClose() {
    this.editedElternteil = undefined;
  }

  private buildUpdatedGesuchWithDeletedElternteil(id: string) {
    const { gesuch, gesuchFormular, expectMutter, expectVater } =
      this.viewSig();
    const updatedElterns = gesuchFormular?.elterns?.filter(
      (entry) =>
        entry.id !== id &&
        isElternTypeExpected(entry, { expectMutter, expectVater }),
    );

    return {
      gesuchId: gesuch?.id,
      trancheId: gesuch?.gesuchTrancheToWorkWith.id,
      gesuchFormular: {
        ...gesuchFormular,
        elterns: updatedElterns,
      },
    };
  }

  private buildUpdatedGesuchWithUpdatedElternteil(elternteil: ElternUpdate) {
    const { gesuch, gesuchFormular, expectMutter, expectVater } =
      this.viewSig();
    // update existing elternteil if found
    const updatedElterns =
      gesuchFormular?.elterns
        ?.map((oldEltern) => {
          if (oldEltern.id === elternteil.id) {
            return elternteil;
          } else {
            return oldEltern;
          }
        })
        .filter((entry) =>
          isElternTypeExpected(entry, { expectMutter, expectVater }),
        ) ?? [];
    // add new elternteil if not found
    if (!elternteil.id) {
      updatedElterns.push(elternteil);
    }
    return {
      gesuchId: gesuch?.id,
      trancheId: gesuch?.gesuchTrancheToWorkWith.id,
      gesuchFormular: {
        ...gesuchFormular,
        elterns: updatedElterns,
      },
    };
  }
}

export const setupElternTeil = (
  elternTyp: ElternTyp,
  gesuchFormular: GesuchFormularUpdate | null,
) => {
  const adresse = gesuchFormular?.personInAusbildung?.adresse;
  const lebtBeiEltern =
    gesuchFormular?.personInAusbildung?.wohnsitz === 'FAMILIE';
  return {
    ...(adresse && lebtBeiEltern
      ? { adresse: { ...adresse, id: undefined } }
      : {}),
    elternTyp,
  };
};

const isElternTypeExpected = (
  eltern: ElternUpdate,
  expected: { expectVater: boolean; expectMutter: boolean },
) => {
  return expected[`expect${capitalized(lowercased(eltern.elternTyp))}`];
};
