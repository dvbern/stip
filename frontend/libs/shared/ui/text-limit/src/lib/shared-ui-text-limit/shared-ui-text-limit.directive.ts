import {
  Directive,
  ElementRef,
  Renderer2,
  effect,
  inject,
  input,
} from '@angular/core';

type MaxSize = 'small' | 'medium' | 'large';

const maxSize = {
  small: 20,
  medium: 255,
  large: 3000,
} satisfies Record<MaxSize, number>;

@Directive({
  selector: 'input[dvTextLimit], textarea[dvTextLimit]',
  standalone: true,
})
export class SharedUiTextLimitDirective {
  dvTextLimitSig = input<MaxSize | ''>('', { alias: 'dvTextLimit' });

  private elementRef =
    inject<ElementRef<HTMLInputElement | HTMLTextAreaElement>>(ElementRef);
  private renderer = inject(Renderer2);

  constructor() {
    effect(() => {
      this.renderer.setAttribute(
        this.elementRef.nativeElement,
        'maxlength',
        maxSize[this.dvTextLimitSig() || 'medium'].toString(),
      );
    });
  }
}
