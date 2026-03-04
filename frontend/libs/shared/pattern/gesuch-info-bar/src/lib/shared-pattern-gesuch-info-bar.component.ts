import { CommonModule } from '@angular/common';
import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { TranslocoDirective } from '@jsverse/transloco';

import { GesuchHeaderSb, SharedModelGesuch } from '@dv/shared/model/gesuch';

@Component({
  selector: 'dv-shared-pattern-gesuch-info-bar',
  imports: [CommonModule, TranslocoDirective, RouterLink],
  templateUrl: './shared-pattern-gesuch-info-bar.component.html',
  styles: `
    :host {
      display: block;
    }
  `,
})
export class SharedPatternGesuchInfoBarComponent {
  headerSbSig = input.required<
    { isLoading: boolean } & Partial<GesuchHeaderSb>
  >();

  gesuchInfoSig = input.required<{ gesuch: SharedModelGesuch | null }>();
}
