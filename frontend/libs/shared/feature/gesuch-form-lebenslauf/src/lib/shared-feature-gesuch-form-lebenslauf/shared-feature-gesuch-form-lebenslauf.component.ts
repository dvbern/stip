import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  OnInit,
  Signal,
  computed,
  inject,
} from '@angular/core';
import { TranslocoPipe } from '@jsverse/transloco';
import { Store } from '@ngrx/store';
import { addYears, max, setMonth, startOfMonth, subMonths } from 'date-fns';

import { AusbildungsstaetteStore } from '@dv/shared/data-access/ausbildungsstaette';
import { selectLanguage } from '@dv/shared/data-access/language';
import { SharedEventGesuchFormLebenslauf } from '@dv/shared/event/gesuch-form-lebenslauf';
import { LebenslaufItemUpdate } from '@dv/shared/model/gesuch';
import { LEBENSLAUF } from '@dv/shared/model/gesuch-form';
import { SharedModelLebenslauf } from '@dv/shared/model/lebenslauf';
import { SharedUiInfoContainerComponent } from '@dv/shared/ui/info-container';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiRdIsPendingPipe } from '@dv/shared/ui/remote-data-pipe';
import { SharedUiStepFormButtonsComponent } from '@dv/shared/ui/step-form-buttons';
import {
  dateFromMonthYearString,
  printDateAsMonthYear,
} from '@dv/shared/util/validator-date';

import { selectSharedFeatureGesuchFormLebenslaufVew } from './shared-feature-gesuch-form-lebenslauf.selector';
import { SharedFeatureGesuchFormLebenslaufEditorComponent } from '../shared-feature-gesuch-form-lebenslauf-editor/shared-feature-gesuch-form-lebenslauf-editor.component';
import { TimelineAddCommand } from '../shared-feature-gesuch-form-lebenslauf-visual/two-column-timeline';
import { TwoColumnTimelineComponent } from '../shared-feature-gesuch-form-lebenslauf-visual/two-column-timeline.component';

const AUSBILDUNGS_MONTH = 8;
const MIN_EDUCATION_AGE = 16;

@Component({
  selector: 'dv-shared-feature-gesuch-form-lebenslauf',
  imports: [
    CommonModule,
    SharedFeatureGesuchFormLebenslaufEditorComponent,
    TranslocoPipe,
    TwoColumnTimelineComponent,
    SharedUiRdIsPendingPipe,
    SharedUiInfoContainerComponent,
    SharedUiStepFormButtonsComponent,
    SharedUiLoadingComponent,
  ],
  templateUrl: './shared-feature-gesuch-form-lebenslauf.component.html',
  styleUrls: ['./shared-feature-gesuch-form-lebenslauf.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchFormLebenslaufComponent implements OnInit {
  private store = inject(Store);
  ausbildungsstatteStore = inject(AusbildungsstaetteStore);
  languageSig = this.store.selectSignal(selectLanguage);
  hasUnsavedChanges = false;

  viewSig = this.store.selectSignal(selectSharedFeatureGesuchFormLebenslaufVew);

  minDatesSig: Signal<{ optional: Date; required: Date } | null> = computed(
    () => {
      const geburtsdatum =
        this.viewSig().gesuchFormular?.personInAusbildung?.geburtsdatum;
      if (geburtsdatum) {
        const geburtsdatumDate = Date.parse(geburtsdatum);
        const dates = [
          // either the birthdate or the start of the current school year of the birth year
          // if the birthdate is after the start of the school year
          max([
            geburtsdatumDate,
            setMonth(geburtsdatumDate, AUSBILDUNGS_MONTH - 1),
          ]),
          // the start of the current school year of the birth year + 16 years
          setMonth(
            addYears(Date.parse(geburtsdatum), MIN_EDUCATION_AGE),
            AUSBILDUNGS_MONTH - 1,
          ),
        ];
        const [optional, required] = dates.map((date) => startOfMonth(date));
        return { optional, required };
      }
      return null;
    },
  );

  maxDateSig: Signal<Date | null> = computed(() => {
    const ausbildungStart =
      this.viewSig().gesuchFormular?.ausbildung?.ausbildungBegin;
    if (ausbildungStart) {
      const start = dateFromMonthYearString(ausbildungStart);
      return start ? subMonths(start, 1) : null;
    }
    return null;
  });

  ausbildungenSig: Signal<LebenslaufItemUpdate[]> = computed(() => {
    return this.viewSig().lebenslaufItems.filter((l) => l.abschlussId);
  });

  editedItem?: SharedModelLebenslauf;

  ngOnInit(): void {
    this.store.dispatch(SharedEventGesuchFormLebenslauf.init());
    this.ausbildungsstatteStore.loadAusbildungsstaetten$();
  }

  public handleAddAusbildung(addCommand: TimelineAddCommand | undefined): void {
    this.editedItem = {
      type: 'AUSBILDUNG',
      von: addCommand ? printDateAsMonthYear(addCommand.von) : undefined,
      bis: addCommand ? printDateAsMonthYear(addCommand.bis) : undefined,
    };
  }

  public handleAddTaetigkeit(addCommand: TimelineAddCommand | undefined): void {
    this.editedItem = {
      type: 'TAETIGKEIT',
      von: addCommand ? printDateAsMonthYear(addCommand.von) : undefined,
      bis: addCommand ? printDateAsMonthYear(addCommand.bis) : undefined,
    };
  }

  public handleEditItem(ge: LebenslaufItemUpdate): void {
    this.editedItem = {
      type: ge.abschlussId ? 'AUSBILDUNG' : 'TAETIGKEIT',
      ...ge,
    };
  }

  handleEditorSave(item: LebenslaufItemUpdate) {
    const { gesuchId, trancheId, gesuchFormular } =
      this.buildUpdatedGesuchWithUpdatedItem(item);
    if (gesuchId && trancheId) {
      this.store.dispatch(
        SharedEventGesuchFormLebenslauf.saveSubformTriggered({
          gesuchId,
          trancheId,
          gesuchFormular,
          origin: LEBENSLAUF,
        }),
      );
      this.editedItem = undefined;
    }
  }

  public handleDeleteItem(itemId: string) {
    const { gesuchId, trancheId, gesuchFormular } =
      this.buildUpdatedGesuchWithDeletedItem(itemId);
    if (gesuchId && trancheId) {
      this.store.dispatch(
        SharedEventGesuchFormLebenslauf.saveSubformTriggered({
          gesuchId,
          trancheId,
          gesuchFormular,
          origin: LEBENSLAUF,
        }),
      );
      this.editedItem = undefined;
    }
  }

  handleContinue() {
    const { gesuch } = this.viewSig();
    if (gesuch?.id) {
      this.store.dispatch(
        SharedEventGesuchFormLebenslauf.nextTriggered({
          id: gesuch.id,
          origin: LEBENSLAUF,
        }),
      );
    }
  }

  handleEditorClose() {
    this.editedItem = undefined;
  }

  private buildUpdatedGesuchWithDeletedItem(itemId: string) {
    const { gesuch, gesuchFormular } = this.viewSig();
    const updatedItems = gesuchFormular?.lebenslaufItems?.filter(
      (item) => item.id !== itemId,
    );

    return {
      gesuchId: gesuch?.id,
      trancheId: gesuch?.gesuchTrancheToWorkWith.id,
      gesuchFormular: {
        ...gesuchFormular,
        lebenslaufItems: updatedItems,
      },
    };
  }

  private buildUpdatedGesuchWithUpdatedItem(item: LebenslaufItemUpdate) {
    const { gesuch, gesuchFormular } = this.viewSig();
    // update existing item if found
    const updatedItems =
      gesuchFormular?.lebenslaufItems?.map((oldItem) => {
        if (item.id === oldItem.id) {
          return item;
        } else {
          return oldItem;
        }
      }) ?? [];
    // add new item if not found
    if (!item.id) {
      updatedItems.push(item);
    }
    return {
      gesuchId: gesuch?.id,
      trancheId: gesuch?.gesuchTrancheToWorkWith.id,
      gesuchFormular: {
        ...gesuchFormular,
        lebenslaufItems: updatedItems,
      },
    };
  }

  trackByIndex(index: number) {
    return index;
  }

  public asItem(itemRaw: LebenslaufItemUpdate): LebenslaufItemUpdate {
    return itemRaw;
  }

  public handleEditItemId(id: string, items: LebenslaufItemUpdate[]): void {
    const item = items.find((each) => each.id === id);
    if (item) {
      this.handleEditItem(item);
    }
  }

  protected readonly printDateAsMonthYear = printDateAsMonthYear;
}
