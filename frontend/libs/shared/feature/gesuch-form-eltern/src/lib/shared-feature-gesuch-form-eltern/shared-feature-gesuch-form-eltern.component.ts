import {
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  inject,
} from '@angular/core';
import { Store } from '@ngrx/store';
import { TranslatePipe } from '@ngx-translate/core';

import { selectSharedDataAccessGesuchCache } from '@dv/shared/data-access/gesuch';
import { selectLanguage } from '@dv/shared/data-access/language';
import { SharedDataAccessStammdatenApiEvents } from '@dv/shared/data-access/stammdaten';
import { SharedEventGesuchFormEltern } from '@dv/shared/event/gesuch-form-eltern';
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
import { SharedUiChangeIndicatorComponent } from '@dv/shared/ui/change-indicator';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiStepFormButtonsComponent } from '@dv/shared/ui/step-form-buttons';

import { ElternteilCardComponent } from './elternteil-card/elternteil-card.component';
import { selectSharedFeatureGesuchFormElternView } from './shared-feature-gesuch-form-eltern.selector';
import { SharedFeatureGesuchFormElternEditorComponent } from '../shared-feature-gesuch-form-eltern-editor/shared-feature-gesuch-form-eltern-editor.component';

@Component({
  selector: 'dv-shared-feature-gesuch-form-eltern',
  imports: [
    TranslatePipe,
    SharedFeatureGesuchFormElternEditorComponent,
    ElternteilCardComponent,
    SharedUiStepFormButtonsComponent,
    SharedUiChangeIndicatorComponent,
    SharedUiLoadingComponent,
  ],
  templateUrl: './shared-feature-gesuch-form-eltern.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchFormElternComponent {
  private store = inject(Store);
  private permissionStore = inject(PermissionStore);
  private appType = inject(SharedModelCompileTimeConfig).appType;

  hasUnsavedChanges = false;
  laenderSig = computed(() => {
    return this.viewSig().laender;
  });
  languageSig = this.store.selectSignal(selectLanguage);

  viewSig = this.store.selectSignal(selectSharedFeatureGesuchFormElternView);
  cacheSig = this.store.selectSignal(selectSharedDataAccessGesuchCache);

  editedElternteil?: Omit<Partial<ElternUpdate>, 'elternTyp'> &
    Required<Pick<ElternUpdate, 'elternTyp'>>;

  constructor() {
    this.store.dispatch(SharedEventGesuchFormEltern.init());
    this.store.dispatch(SharedDataAccessStammdatenApiEvents.init());
    effect(
      () => {
        const { loading, gesuch, gesuchFormular } = this.viewSig();
        const rolesMap = this.permissionStore.rolesMapSig();
        const { trancheTyp } = this.cacheSig();
        const { permissions } = preparePermissions(
          trancheTyp,
          gesuch,
          this.appType,
          rolesMap,
        );
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
      },
      { allowSignalWrites: true },
    );
  }

  trackByIndex(index: number) {
    return index;
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
