import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'dv-ausbildungsstaette',
  imports: [CommonModule],
  templateUrl: './ausbildungsstaette.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AusbildungsstaetteComponent {}
