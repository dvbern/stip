import {
  Directive,
  Input,
  Renderer2,
  TemplateRef,
  ViewContainerRef,
  inject,
} from '@angular/core';

/**
 * @description
 * Directive to show a the previous values of Formfields.
 * - Must be placed on a mat-hint component.
 * - Must be used in combination within a mat-form-field.
 * - If there is another hint, the hint will be replaced if there is a previous value.
 * Ohter hints must be placed after this hint element.
 * - The following hit must also have the attribute `align="end"` so material does not throw an error.
 *
 * @example
 * <mat-hint
 *  *dvZuvorHint="previousValue"
 *  data-testid="form-person-sozialversicherungsnummer-zuvor-hint"
 *  class="zuvor-hint"
 * >
 * </mat-hint>
 * <mat-hint align="end" translate>shared.form.einnahmenkosten.nettoerwerbseinkommen.info</mat-hint>
 */
@Directive({
  selector: '[dvZuvorHint]',
  standalone: true,
})
export class SharedUiZuvorHintDirective {
  private viewContainerRef = inject(ViewContainerRef);
  private renderer = inject(Renderer2);
  private templateRef = inject(TemplateRef);

  @Input() set dvZuvorHint(value: string | undefined) {
    if (value) {
      const ref = this.viewContainerRef.createEmbeddedView(this.templateRef);
      if (ref.rootNodes.length > 0) {
        this.renderer.setProperty(ref.rootNodes[0], 'innerText', value);
      }
    } else {
      this.viewContainerRef.clear();
    }
  }
}
