import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  ElementRef,
  HostBinding,
  Renderer2,
  effect,
  inject,
  input,
} from '@angular/core';

@Component({
  selector: 'dv-shared-ui-info-container',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './shared-ui-info-container.component.html',
  styleUrl: './shared-ui-info-container.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiInfoContainerComponent {
  typeSig = input<'info' | 'light' | 'warning' | 'danger' | 'success'>('info', {
    alias: 'type',
  });
  @HostBinding('class') defaultClasses = 'rounded rounded-lg';

  private renderer = inject(Renderer2);
  private elementRef = inject(ElementRef);

  constructor() {
    effect(() => {
      const type = this.typeSig();

      this.renderer.addClass(this.elementRef.nativeElement, `alert-${type}`);
    });
  }
}
