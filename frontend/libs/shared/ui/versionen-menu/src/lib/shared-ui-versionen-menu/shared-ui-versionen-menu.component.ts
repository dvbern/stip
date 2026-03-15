import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { MatMenuModule } from '@angular/material/menu';
import { RouterModule } from '@angular/router';
import { TranslocoDirective } from '@jsverse/transloco';

import { VerfuegtGesuch } from '@dv/shared/model/gesuch';

@Component({
  selector: 'dv-shared-ui-versionen-menu',
  imports: [CommonModule, RouterModule, TranslocoDirective, MatMenuModule],
  templateUrl: './shared-ui-versionen-menu.component.html',
  styleUrl: './shared-ui-versionen-menu.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiVersionenMenuComponent {
  versionenSig = input.required<VerfuegtGesuch[]>();
  gesuchIdSig = input.required<string | undefined>();
  trancheIdSig = input.required<string | undefined>();
  firstTrancheIdSig = input.required<string | undefined>();
}
