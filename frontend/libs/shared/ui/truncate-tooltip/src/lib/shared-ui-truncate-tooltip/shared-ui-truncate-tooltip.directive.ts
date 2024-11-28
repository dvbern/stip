import { Directive, ElementRef, Input, inject } from '@angular/core';
import { MatTooltip } from '@angular/material/tooltip';

@Directive({
  selector: '[dvTruncateTooltip]',
  standalone: true,
})
export class SharedUiTruncateTooltipDirective extends MatTooltip {
  @Input() set dvTruncateTooltip(value: string | undefined) {
    this.message = value;
  }

  private elementRef = inject<ElementRef<HTMLElement>>(ElementRef);

  override show(delay?: number, origin?: { x: number; y: number }): void {
    const element = this.elementRef.nativeElement;
    const isEllipsing = element.offsetWidth !== element.scrollWidth;
    if (isEllipsing) {
      super.show(delay, origin);
    }
  }
}
