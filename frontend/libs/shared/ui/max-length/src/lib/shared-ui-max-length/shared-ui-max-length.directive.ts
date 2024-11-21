import {
  Directive,
  ElementRef,
  Renderer2,
  effect,
  inject,
  input,
} from '@angular/core';

type MaxLength = 'small' | 'medium' | 'large';

const maxLength = {
  small: 20,
  medium: 255,
  large: 2000,
} satisfies Record<MaxLength, number>;

@Directive({
  selector: 'input[dvMaxLength], textarea[dvMaxLength]',
  standalone: true,
})
export class SharedUiMaxLengthDirective {
  dvMaxLengthSig = input<MaxLength | ''>('', { alias: 'dvMaxLength' });

  private elementRef =
    inject<ElementRef<HTMLInputElement | HTMLTextAreaElement>>(ElementRef);
  private renderer = inject(Renderer2);

  constructor() {
    effect(() => {
      this.renderer.setAttribute(
        this.elementRef.nativeElement,
        'maxlength',
        maxLength[this.dvMaxLengthSig() || 'medium'].toString(),
      );
    });
  }
}
