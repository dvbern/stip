import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  Output,
} from '@angular/core';
import { TranslocoDirective } from '@jsverse/transloco';

import { ElternTyp, ElternUpdate } from '@dv/shared/model/gesuch';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiTranslatedLandPipe } from '@dv/shared/ui/translated-land';

@Component({
  selector: 'dv-shared-feature-gesuch-form-eltern-card',
  imports: [
    CommonModule,
    TranslocoDirective,
    SharedUiIconChipComponent,
    SharedUiTranslatedLandPipe,
  ],
  templateUrl: './elternteil-card.component.html',
  styleUrls: ['./elternteil-card.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ElternteilCardComponent {
  @Input({ required: true })
  elternteil!: ElternUpdate | undefined;
  @Input({ required: true })
  elternTyp!: ElternTyp;
  @Input({ required: true })
  readonly!: boolean;
  @Input()
  hidden = false;
  @Output()
  editTriggered = new EventEmitter<ElternUpdate>();
  @Output()
  addTriggered = new EventEmitter<ElternTyp>();

  elternTypen = ElternTyp;

  handleClick() {
    if (this.elternteil) {
      this.editTriggered.emit(this.elternteil);
    } else {
      this.addTriggered.emit(this.elternTyp);
    }
  }
}
