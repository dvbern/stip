import {
  Directive,
  ElementRef,
  Input,
  Renderer2,
  inject,
  signal,
} from '@angular/core';
import { FormGroupDirective } from '@angular/forms';

@Directive({
  selector: '[dvSharedUiFormReadonly]',
  standalone: true,
})
export class SharedUiFormReadonlyDirective {
  formGroup = inject(FormGroupDirective);
  elementRef = inject(ElementRef);
  renderer = inject(Renderer2);
  isReadonly = signal<boolean>(false);

  @Input() set dvSharedUiFormReadonly(readonly: boolean | undefined) {
    if (readonly) {
      this.isReadonly.set(true);
      this.formGroup.form.disable({ emitEvent: false });
      this.renderer.addClass(this.elementRef.nativeElement, 'readonly');
    }
  }
}
