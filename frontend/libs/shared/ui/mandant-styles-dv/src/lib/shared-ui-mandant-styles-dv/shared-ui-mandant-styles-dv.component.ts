import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  ViewEncapsulation,
} from '@angular/core';

@Component({
    selector: 'dv-shared-ui-mandant-styles-dv',
    imports: [CommonModule],
    template: '',
    styleUrl: './shared-ui-mandant-styles-dv.component.scss',
    changeDetection: ChangeDetectionStrategy.OnPush,
    encapsulation: ViewEncapsulation.None
})
export class SharedUiMandantStylesDvComponent {}
