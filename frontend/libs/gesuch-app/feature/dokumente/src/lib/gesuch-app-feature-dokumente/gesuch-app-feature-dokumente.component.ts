import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'dv-gesuch-app-feature-dokumente',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './gesuch-app-feature-dokumente.component.html',
  styleUrl: './gesuch-app-feature-dokumente.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GesuchAppFeatureDokumenteComponent {}
