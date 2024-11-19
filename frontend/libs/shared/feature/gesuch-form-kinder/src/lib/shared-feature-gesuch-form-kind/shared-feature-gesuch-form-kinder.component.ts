import {
  ChangeDetectionStrategy,
  Component,
  OnInit,
  computed,
  inject,
} from '@angular/core';
import { Store } from '@ngrx/store';
import { TranslatePipe } from '@ngx-translate/core';

import { selectLanguage } from '@dv/shared/data-access/language';
import { SharedEventGesuchFormKinder } from '@dv/shared/event/gesuch-form-kinder';
import { KindUpdate } from '@dv/shared/model/gesuch';
import { KINDER } from '@dv/shared/model/gesuch-form';
import { SharedUiChangeIndicatorComponent } from '@dv/shared/ui/change-indicator';
import { SharedUiFormZuvorHintListPipe } from '@dv/shared/ui/form';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiStepFormButtonsComponent } from '@dv/shared/ui/step-form-buttons';
import { parseBackendLocalDateAndPrint } from '@dv/shared/util/validator-date';

import { selectSharedFeatureGesuchFormKinderView } from './shared-feature-gesuch-form-kinder.selector';
import { SharedFeatureGesuchFormKinderEditorComponent } from '../shared-feature-gesuch-form-kind-editor/shared-feature-gesuch-form-kind-editor.component';

@Component({
  selector: 'dv-shared-feature-gesuch-form-kinder',
  standalone: true,
  imports: [
    TranslatePipe,
    SharedFeatureGesuchFormKinderEditorComponent,
    SharedUiStepFormButtonsComponent,
    SharedUiChangeIndicatorComponent,
    SharedUiFormZuvorHintListPipe,
    SharedUiLoadingComponent,
  ],
  templateUrl: './shared-feature-gesuch-form-kinder.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchFormKinderComponent implements OnInit {
  private store = inject(Store);

  viewSig = this.store.selectSignal(selectSharedFeatureGesuchFormKinderView);

  hasUnsavedChanges = false;
  languageSig = this.store.selectSignal(selectLanguage);

  parseBackendLocalDateAndPrint = parseBackendLocalDateAndPrint;

  sortedKinderSig = computed(() => {
    const originalList = this.viewSig().gesuchFormular?.kinds;
    return originalList
      ? [...originalList].sort((a, b) =>
          (a.vorname + ' ' + a.nachname).localeCompare(
            b.vorname + ' ' + b.nachname,
          ),
        )
      : undefined;
  });

  editedKind?: Partial<KindUpdate>;

  ngOnInit(): void {
    this.store.dispatch(SharedEventGesuchFormKinder.init());
  }

  public handleAddKinder(): void {
    this.editedKind = {};
  }

  public handleSelectKinder(ge: KindUpdate): void {
    this.editedKind = ge;
  }

  handleEditorSave(kind: KindUpdate) {
    const { gesuchId, trancheId, gesuchFormular } =
      this.buildUpdatedGesuchWithUpdatedKind(kind);
    if (gesuchId && trancheId) {
      this.store.dispatch(
        SharedEventGesuchFormKinder.saveSubformTriggered({
          gesuchId,
          trancheId,
          gesuchFormular,
          origin: KINDER,
        }),
      );
      this.editedKind = undefined;
    }
  }

  public handleDeleteKinder(kind: KindUpdate) {
    const { gesuchId, trancheId, gesuchFormular } =
      this.buildUpdatedGesuchWithDeletedKinder(kind);
    if (gesuchId && trancheId) {
      this.store.dispatch(
        SharedEventGesuchFormKinder.saveSubformTriggered({
          gesuchId,
          trancheId,
          gesuchFormular,
          origin: KINDER,
        }),
      );
      this.editedKind = undefined;
    }
  }

  handleContinue() {
    const { gesuch } = this.viewSig();
    if (gesuch?.id)
      this.store.dispatch(
        SharedEventGesuchFormKinder.nextTriggered({
          id: gesuch.id,
          origin: KINDER,
        }),
      );
  }

  handleEditorClose() {
    this.editedKind = undefined;
  }

  private buildUpdatedGesuchWithDeletedKinder(kind: KindUpdate) {
    const { gesuch, gesuchFormular } = this.viewSig();
    const updatedKinders = gesuchFormular?.kinds?.filter(
      (entry) => entry.id !== kind.id,
    );

    return {
      gesuchId: gesuch?.id,
      trancheId: gesuch?.gesuchTrancheToWorkWith.id,
      gesuchFormular: {
        ...gesuchFormular,
        kinds: updatedKinders,
      },
    };
  }

  private buildUpdatedGesuchWithUpdatedKind(kind: KindUpdate) {
    const { gesuch, gesuchFormular } = this.viewSig();
    // update existing kind if found
    const updatedKinders =
      gesuchFormular?.kinds?.map((oldKind) => {
        if (oldKind?.id === kind.id) {
          return kind;
        } else {
          return oldKind;
        }
      }) ?? [];
    // add new kind if not found
    if (!kind.id) {
      updatedKinders.push(kind);
    }
    return {
      gesuchId: gesuch?.id,
      trancheId: gesuch?.gesuchTrancheToWorkWith.id,
      gesuchFormular: {
        ...gesuchFormular,
        kinds: updatedKinders,
      },
    };
  }

  trackByIndex(index: number) {
    return index;
  }
}
