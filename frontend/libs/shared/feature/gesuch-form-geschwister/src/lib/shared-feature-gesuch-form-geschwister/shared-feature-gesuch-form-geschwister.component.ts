import {
  ChangeDetectionStrategy,
  Component,
  OnInit,
  inject,
} from '@angular/core';
import { Store } from '@ngrx/store';
import { TranslatePipe } from '@ngx-translate/core';

import { selectLanguage } from '@dv/shared/data-access/language';
import { SharedEventGesuchFormGeschwister } from '@dv/shared/event/gesuch-form-geschwister';
import { GeschwisterUpdate } from '@dv/shared/model/gesuch';
import { GESCHWISTER } from '@dv/shared/model/gesuch-form';
import { SharedUiChangeIndicatorComponent } from '@dv/shared/ui/change-indicator';
import { SharedUiFormZuvorHintListPipe } from '@dv/shared/ui/form';
import { SharedUiInfoContainerComponent } from '@dv/shared/ui/info-container';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiStepFormButtonsComponent } from '@dv/shared/ui/step-form-buttons';
import { parseBackendLocalDateAndPrint } from '@dv/shared/util/validator-date';

import { selectSharedFeatureGesuchFormGeschwisterView } from './shared-feature-gesuch-form-geschwister.selector';
import { SharedFeatureGesuchFormGeschwisterEditorComponent } from '../shared-feature-gesuch-form-geschwister-editor/shared-feature-gesuch-form-geschwister-editor.component';

@Component({
  selector: 'dv-shared-feature-gesuch-form-geschwister',
  standalone: true,
  imports: [
    TranslatePipe,
    SharedFeatureGesuchFormGeschwisterEditorComponent,
    SharedUiInfoContainerComponent,
    SharedUiStepFormButtonsComponent,
    SharedUiChangeIndicatorComponent,
    SharedUiFormZuvorHintListPipe,
    SharedUiLoadingComponent,
  ],
  templateUrl: './shared-feature-gesuch-form-geschwister.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchFormGeschwisterComponent implements OnInit {
  private store = inject(Store);

  viewSig = this.store.selectSignal(
    selectSharedFeatureGesuchFormGeschwisterView,
  );

  hasUnsavedChanges = false;
  languageSig = this.store.selectSignal(selectLanguage);

  parseBackendLocalDateAndPrint = parseBackendLocalDateAndPrint;

  editedGeschwister?: Partial<GeschwisterUpdate>;
  editedGeschwisterIndex: number | undefined;

  ngOnInit(): void {
    this.store.dispatch(SharedEventGesuchFormGeschwister.init());
  }

  public handleAddGeschwister(): void {
    this.editedGeschwister = {};
    this.editedGeschwisterIndex = undefined;
  }

  public handleSelectGeschwister(ge: GeschwisterUpdate, index: number): void {
    this.editedGeschwister = ge;
    this.editedGeschwisterIndex = index;
  }

  handleEditorSave(geschwister: GeschwisterUpdate) {
    const { gesuchId, trancheId, gesuchFormular } =
      this.buildUpdatedGesuchWithUpdatedGeschwister(geschwister);
    if (gesuchId && trancheId) {
      this.store.dispatch(
        SharedEventGesuchFormGeschwister.saveSubformTriggered({
          gesuchId,
          trancheId,
          gesuchFormular,
          origin: GESCHWISTER,
        }),
      );
      this.editedGeschwister = undefined;
    }
  }

  public handleDeleteGeschwister(geschwister: GeschwisterUpdate) {
    const { gesuchId, trancheId, gesuchFormular } =
      this.buildUpdatedGesuchWithDeletedGeschwister(geschwister);
    if (gesuchId && trancheId) {
      this.store.dispatch(
        SharedEventGesuchFormGeschwister.saveSubformTriggered({
          gesuchId,
          trancheId,
          gesuchFormular,
          origin: GESCHWISTER,
        }),
      );
      this.editedGeschwister = undefined;
    }
  }

  handleContinue() {
    const { gesuch } = this.viewSig();
    if (gesuch?.id) {
      this.store.dispatch(
        SharedEventGesuchFormGeschwister.nextTriggered({
          id: gesuch.id,
          origin: GESCHWISTER,
        }),
      );
    }
  }

  handleEditorClose() {
    this.editedGeschwister = undefined;
  }

  private buildUpdatedGesuchWithDeletedGeschwister(
    geschwister: GeschwisterUpdate,
  ) {
    const { gesuch, gesuchFormular } = this.viewSig();
    const updatedGeschwisters = gesuchFormular?.geschwisters?.filter(
      (entry) => entry.id !== geschwister.id,
    );

    return {
      gesuchId: gesuch?.id,
      trancheId: gesuch?.gesuchTrancheToWorkWith.id,
      gesuchFormular: {
        ...gesuchFormular,
        geschwisters: updatedGeschwisters,
      },
    };
  }

  private buildUpdatedGesuchWithUpdatedGeschwister(
    geschwister: GeschwisterUpdate,
  ) {
    const { gesuch, gesuchFormular } = this.viewSig();
    // update existing geschwister if found
    const updatedGeschwisters =
      gesuchFormular?.geschwisters?.map((oldGeschwister) => {
        if (oldGeschwister.id === geschwister.id) {
          return geschwister;
        } else {
          return oldGeschwister;
        }
      }) ?? [];
    // add new geschwister if not found
    if (!geschwister.id) {
      updatedGeschwisters.push(geschwister);
    }
    return {
      gesuchId: gesuch?.id,
      trancheId: gesuch?.gesuchTrancheToWorkWith.id,
      gesuchFormular: {
        ...gesuchFormular,
        geschwisters: updatedGeschwisters,
      },
    };
  }

  trackByIndex(index: number) {
    return index;
  }

  public asGeschwister(geschwisterRaw: GeschwisterUpdate) {
    return geschwisterRaw;
  }
}
