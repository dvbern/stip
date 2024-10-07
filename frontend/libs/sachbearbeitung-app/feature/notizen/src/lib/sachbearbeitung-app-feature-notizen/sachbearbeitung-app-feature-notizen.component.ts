import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { MatCell } from '@angular/material/table';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'lib-sachbearbeitung-app-feature-notizen',
  standalone: true,
  imports: [CommonModule, MatCell, TranslateModule],
  templateUrl: './sachbearbeitung-app-feature-notizen.component.html',
  styleUrl: './sachbearbeitung-app-feature-notizen.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureNotizenComponent {}
