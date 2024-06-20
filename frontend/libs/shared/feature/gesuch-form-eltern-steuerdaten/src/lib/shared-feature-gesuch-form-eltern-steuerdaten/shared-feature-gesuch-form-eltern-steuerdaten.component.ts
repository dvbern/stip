import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'lib-shared-feature-gesuch-form-eltern-steuerdaten',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './shared-feature-gesuch-form-eltern-steuerdaten.component.html',
  styleUrl: './shared-feature-gesuch-form-eltern-steuerdaten.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchFormElternSteuerdatenComponent {}
