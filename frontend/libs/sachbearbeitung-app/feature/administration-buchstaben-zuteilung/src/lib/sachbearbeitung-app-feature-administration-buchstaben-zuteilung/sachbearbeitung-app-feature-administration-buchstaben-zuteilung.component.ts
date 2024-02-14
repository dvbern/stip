import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector:
    'dv-sachbearbeitung-app-feature-administration-buchstaben-zuteilung',
  standalone: true,
  imports: [CommonModule],
  templateUrl:
    './sachbearbeitung-app-feature-administration-buchstaben-zuteilung.component.html',
  styleUrls: [
    './sachbearbeitung-app-feature-administration-buchstaben-zuteilung.component.scss',
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureAdministrationBuchstabenZuteilungComponent {}
