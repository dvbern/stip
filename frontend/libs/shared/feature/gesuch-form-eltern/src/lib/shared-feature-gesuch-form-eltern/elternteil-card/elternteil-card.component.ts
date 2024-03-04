import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  Output,
} from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';

import { ElternTyp, ElternUpdate } from '@dv/shared/model/gesuch';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';

@Component({
  selector: 'dv-shared-feature-gesuch-form-eltern-card',
  standalone: true,
  imports: [TranslateModule, SharedUiIconChipComponent],
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
  translationkey!: string;
  @Input({ required: true })
  readonly!: boolean;
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
