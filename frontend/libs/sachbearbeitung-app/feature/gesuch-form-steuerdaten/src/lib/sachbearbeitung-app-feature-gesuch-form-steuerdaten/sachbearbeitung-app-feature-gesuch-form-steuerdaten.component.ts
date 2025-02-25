import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-gesuch-form-steuerdaten',
  standalone: true,
  imports: [CommonModule],
  templateUrl:
    './sachbearbeitung-app-feature-gesuch-form-steuerdaten.component.html',
  styleUrl:
    './sachbearbeitung-app-feature-gesuch-form-steuerdaten.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureGesuchFormSteuerdatenComponent {}
