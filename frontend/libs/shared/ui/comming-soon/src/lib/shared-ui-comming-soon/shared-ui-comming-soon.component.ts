import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'dv-shared-ui-comming-soon',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './shared-ui-comming-soon.component.html',
  styleUrl: './shared-ui-comming-soon.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiCommingSoonComponent {}
