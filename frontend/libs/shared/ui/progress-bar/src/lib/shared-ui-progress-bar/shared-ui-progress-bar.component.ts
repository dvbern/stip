import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  Injector,
  OnInit,
  Renderer2,
  effect,
  inject,
  input,
  runInInjectionContext,
} from '@angular/core';

import { GesuchFormStepProgress } from '@dv/shared/model/gesuch-form';
import { isDefined } from '@dv/shared/model/type-util';

@Component({
  selector: 'dv-shared-ui-progress-bar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './shared-ui-progress-bar.component.html',
  styleUrls: ['./shared-ui-progress-bar.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiProgressBarComponent implements OnInit {
  private injector = inject(Injector);
  private renderer = inject(Renderer2);
  private elementRef = inject(ElementRef);
  currentSig = input.required<GesuchFormStepProgress>();

  ngOnInit() {
    runInInjectionContext(this.injector, () => {
      effect(() => {
        if (isDefined(this.currentSig().percentage)) {
          this.renderer.setProperty(
            this.elementRef.nativeElement,
            'style',
            `--progress: ${this.currentSig().percentage}`,
          );
        } else {
          this.renderer.setProperty(
            this.elementRef.nativeElement,
            'style',
            null,
          );
        }
      });
    });
  }
}
