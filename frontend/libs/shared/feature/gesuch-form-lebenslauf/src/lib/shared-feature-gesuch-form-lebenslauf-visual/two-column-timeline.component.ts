import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  EventEmitter,
  Output,
  effect,
  inject,
  input,
} from '@angular/core';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';

import { AusbildungsstaetteStore } from '@dv/shared/data-access/ausbildungsstaette';
import {
  Ausbildung,
  AusbildungsgangSlim,
  AusbildungsstaetteSlim,
  LebenslaufItemUpdate,
} from '@dv/shared/model/gesuch';
import {
  dateFromMonthYearString,
  printDateAsMonthYear,
} from '@dv/shared/util/validator-date';

import {
  TimelineAddCommand,
  TimelineBusyBlock,
  TimelineBusyBlockChild,
  TimelineGapBlock,
  TimelineRawItem,
  TwoColumnTimeline,
  asBusyBlock,
  asGapBlock,
  isTimelineBusyBlock,
  isTimelineGapBlock,
} from './two-column-timeline';

@Component({
  selector: 'dv-shared-feature-gesuch-form-lebenslauf-visual',
  imports: [CommonModule, TranslatePipe],
  templateUrl: './two-column-timeline.component.html',
  styleUrls: ['./two-column-timeline.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class TwoColumnTimelineComponent {
  cd = inject(ChangeDetectorRef);
  translate = inject(TranslateService);
  ausbildungsstaetteStore = inject(AusbildungsstaetteStore);

  @Output() addAusbildungTriggered = new EventEmitter<TimelineAddCommand>();
  @Output() addTaetigkeitTriggered = new EventEmitter<TimelineAddCommand>();
  @Output() editItemTriggered = new EventEmitter<string>();
  @Output() deleteItemTriggered = new EventEmitter<string>();

  startDate = input.required<Date | null>();
  lebenslaufItems = input.required<LebenslaufItemUpdate[]>();
  ausbildung = input.required<Ausbildung>();
  ausbildungsstaettes = input.required<AusbildungsstaetteSlim[]>();
  language = input.required<string>();

  constructor() {
    this.ausbildungsstaetteStore.loadAbschluesse$();

    effect(() => {
      const startDate = this.startDate();
      const lebenslaufItems = this.lebenslaufItems();
      const ausbildung = this.ausbildung();
      const ausbildungsstaettes = this.ausbildungsstaettes();
      this.language();

      this.setLebenslaufItems(
        startDate,
        lebenslaufItems,
        ausbildung,
        ausbildungsstaettes,
      );
    });
  }

  setLebenslaufItems(
    expectedSartDate: Date | null,
    lebenslaufItems: LebenslaufItemUpdate[],
    plannedAusbildung: Ausbildung | undefined,
    ausbildungsstaettes: AusbildungsstaetteSlim[],
  ) {
    const timelineRawItems = lebenslaufItems.map(
      (lebenslaufItem) =>
        ({
          col: lebenslaufItem.abschlussId ? 'LEFT' : 'RIGHT',
          von: dateFromMonthYearString(lebenslaufItem.von),
          bis: dateFromMonthYearString(lebenslaufItem.bis),
          id: lebenslaufItem.id,
          label: this.getLebenslaufItemLabel(lebenslaufItem),
          editable: true,
          ausbildungAbgeschlossen: lebenslaufItem.ausbildungAbgeschlossen,
        }) as TimelineRawItem,
    );

    // planned ausbildung
    const ausbildungsstaette = ausbildungsstaettes.find((staette) =>
      staette.ausbildungsgaenge?.some(
        (ausbildungsgang) =>
          plannedAusbildung?.ausbildungsgang?.id === ausbildungsgang.id,
      ),
    );
    const ausbildungsgang = ausbildungsstaette?.ausbildungsgaenge?.find(
      (each) => each.id === plannedAusbildung?.ausbildungsgang?.id,
    );

    timelineRawItems.push({
      id: 'planned-ausbildung',
      col: 'LEFT',
      von: dateFromMonthYearString(plannedAusbildung?.ausbildungBegin),
      bis: dateFromMonthYearString(plannedAusbildung?.ausbildungEnd),
      label: {
        title:
          (this.getTranslatedAusbildungstaetteName(ausbildungsstaette) ??
            plannedAusbildung?.alternativeAusbildungsstaette) +
          ': ' +
          (this.getTranslatedAusbildungsgangBezeichung(ausbildungsgang) ??
            plannedAusbildung?.alternativeAusbildungsgang),
        ...(plannedAusbildung && ausbildungsgang?.zusatzfrage
          ? {
              subTitle: {
                key: `shared.form.lebenslauf.item.name.zusatzfrage.${ausbildungsgang.zusatzfrage}`,
                value: plannedAusbildung.fachrichtungBerufsbezeichnung,
              },
            }
          : {}),
      },
      editable: false,
      ausbildungAbgeschlossen: false,
    } as TimelineRawItem);

    this.timeline.fillWith(expectedSartDate, timelineRawItems);
    this.cd.markForCheck();
  }

  private getLebenslaufItemLabel(
    lebenslaufItem: LebenslaufItemUpdate,
  ): TimelineRawItem['label'] {
    if (
      lebenslaufItem.taetigkeitsart !== undefined &&
      lebenslaufItem.taetigkeitsart !== null
    ) {
      return { title: lebenslaufItem.taetigkeitsBeschreibung ?? '' };
    }

    const abschluss = this.ausbildungsstaetteStore
      .abschluesseViewSig()
      .find((abschluss) => abschluss.id === lebenslaufItem.abschlussId);

    if (!abschluss) {
      console.warn(`Abschluss with id ${lebenslaufItem.abschlussId} not found`);
      return { title: '' };
    }

    return {
      title: `shared.ausbildungskategorie.${abschluss.ausbildungskategorie}`,
      ...(abschluss.zusatzfrage
        ? {
            subTitle: {
              key: `shared.form.lebenslauf.item.name.zusatzfrage.${abschluss.zusatzfrage}`,
              value: lebenslaufItem.fachrichtungBerufsbezeichnung,
            },
          }
        : {}),
    };
  }

  timeline = new TwoColumnTimeline();

  trackByIndex(index: number) {
    return index;
  }

  protected readonly isTimelineBusyBlock = isTimelineBusyBlock;
  protected readonly isTimelineGapBlock = isTimelineGapBlock;
  protected readonly asBusyBlock = asBusyBlock;
  protected readonly asGapBlock = asGapBlock;

  public handleAddAusbildung(timelineBusyBlock: TimelineGapBlock): void {
    this.addAusbildungTriggered.emit({
      von: timelineBusyBlock.von,
      bis: timelineBusyBlock.bis,
    });
  }

  public handleAddTaetigkeit(timelineGapBlock: TimelineGapBlock): void {
    this.addTaetigkeitTriggered.emit({
      von: timelineGapBlock.von,
      bis: timelineGapBlock.bis,
    });
  }

  public handleEditItem(
    item: TimelineBusyBlock | TimelineBusyBlockChild,
  ): void {
    this.editItemTriggered.emit(item.id);
  }

  private getTranslatedAusbildungstaetteName(
    staette: AusbildungsstaetteSlim | undefined,
  ): string | undefined {
    if (staette === undefined) {
      return undefined;
    }
    return this.language() === 'fr' ? staette.nameFr : staette.nameDe;
  }

  protected readonly printDateAsMonthYear = printDateAsMonthYear;

  private getTranslatedAusbildungsgangBezeichung(
    ausbildungsgang: AusbildungsgangSlim | undefined,
  ): string | undefined {
    if (ausbildungsgang === undefined) {
      return undefined;
    }
    return this.language() === 'fr'
      ? ausbildungsgang.bezeichnungFr
      : ausbildungsgang.bezeichnungDe;
  }
}
