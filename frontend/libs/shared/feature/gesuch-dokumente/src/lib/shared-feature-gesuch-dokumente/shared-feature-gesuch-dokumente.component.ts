import { ChangeDetectionStrategy, Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'dv-shared-feature-gesuch-dokumente',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './shared-feature-gesuch-dokumente.component.html',
  styleUrl: './shared-feature-gesuch-dokumente.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchDokumenteComponent {}
