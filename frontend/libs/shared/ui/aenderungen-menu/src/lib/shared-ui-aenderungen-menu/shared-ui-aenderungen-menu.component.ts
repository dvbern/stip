import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { MatMenuModule } from '@angular/material/menu';
import { RouterModule } from '@angular/router';
import { TranslocoDirective } from '@jsverse/transloco';

import { GesuchAenderungs } from '@dv/shared/model/gesuch';

@Component({
  selector: 'dv-shared-ui-aenderungen-menu',
  imports: [CommonModule, MatMenuModule, TranslocoDirective, RouterModule],
  templateUrl: './shared-ui-aenderungen-menu.component.html',
  styleUrl: './shared-ui-aenderungen-menu.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiAenderungenMenuComponent {
  gesuchIdSig = input.required<string | undefined>();
  trancheIdSig = input.required<string | undefined>();
  revisionSig = input.required<number | undefined>();
  aenderungenSig = input.required<GesuchAenderungs | undefined>();
  isAenderungRouteSig = input.required<boolean | undefined>();
}
