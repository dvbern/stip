import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  Output,
} from '@angular/core';
import { RouterLink } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';

import { GesuchTrancheSlim } from '@dv/shared/model/gesuch';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';

@Component({
  selector: 'dv-gesuch-app-ui-aenderungs-entry',
  standalone: true,
  imports: [CommonModule, TranslatePipe, RouterLink, SharedUiIconChipComponent],
  templateUrl: './gesuch-app-ui-aenderungs-entry.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GesuchAppUiAenderungsEntryComponent {
  @Input({ required: true }) tranche!: GesuchTrancheSlim;
  @Input({ required: true }) gesuchId!: string;
  @Output() deleteAenderung = new EventEmitter<string>();
}
