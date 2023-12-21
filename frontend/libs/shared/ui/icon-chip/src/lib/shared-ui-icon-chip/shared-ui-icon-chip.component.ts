import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'dv-shared-ui-icon-chip',
  standalone: true,
  templateUrl: './shared-ui-icon-chip.component.html',
  styleUrls: ['./shared-ui-icon-chip.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiIconChipComponent {}
