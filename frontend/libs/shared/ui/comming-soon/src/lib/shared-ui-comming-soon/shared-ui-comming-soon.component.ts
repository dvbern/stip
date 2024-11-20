import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { TranslatePipe } from '@ngx-translate/core';

@Component({
  selector: 'dv-shared-ui-comming-soon',
  standalone: true,
  imports: [CommonModule, TranslatePipe],
  templateUrl: './shared-ui-comming-soon.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiCommingSoonComponent {}
