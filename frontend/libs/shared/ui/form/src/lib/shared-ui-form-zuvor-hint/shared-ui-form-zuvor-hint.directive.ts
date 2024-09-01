import {
  AfterViewInit,
  Directive,
  Input,
  Renderer2,
  TemplateRef,
  ViewContainerRef,
  inject,
} from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Directive({
  selector: '[dvZuvorHint]',
  standalone: true,
})
export class SharedUiZuvorHintDirective implements AfterViewInit {
  private viewContainerRef = inject(ViewContainerRef);
  private renderer = inject(Renderer2);
  private templateRef = inject(TemplateRef);

  translate = inject(TranslateService);
  private _value: string | undefined;

  private spacer: Node | undefined = undefined;
  private prevHint: Node | undefined = undefined;

  @Input() set dvZuvorHint(value: string | undefined) {
    this._value = value;

    if (value) {
      const translated = this.translate.instant('shared.form.zuvor.hint', {
        value: value,
      });
      const ref = this.viewContainerRef.createEmbeddedView(this.templateRef);
      if (ref.rootNodes.length > 0) {
        ref.rootNodes[0] as HTMLElement;
        this.renderer.setProperty(ref.rootNodes[0], 'innerText', translated);
      }
      this.hideExisitingHint();
    } else {
      this.viewContainerRef.clear();
      this.showExisitingHint();
    }
  }

  ngAfterViewInit() {
    if (this._value) {
      this.hideExisitingHint();
    }
  }

  hideExisitingHint() {
    const parentNodes: NodeList | undefined =
      this.viewContainerRef.element.nativeElement.parentNode?.childNodes;

    if (parentNodes) {
      this.prevHint = Array.from(parentNodes).find(
        (node) =>
          node.nodeType === Node.ELEMENT_NODE &&
          (node as Element).classList.contains('mat-mdc-form-field-hint') &&
          !(node as Element).classList.contains('zuvor-hint'),
      );

      this.spacer = Array.from(parentNodes).find(
        (node) =>
          node.nodeType === Node.ELEMENT_NODE &&
          (node as Element).classList.contains(
            'mat-mdc-form-field-hint-spacer',
          ),
      );
    }

    if (this.prevHint && this.spacer) {
      this.renderer.addClass(this.prevHint, 'd-none');
      this.renderer.addClass(this.spacer, 'd-none');
    }
  }

  showExisitingHint() {
    if (this.prevHint && this.spacer) {
      this.renderer.removeClass(this.prevHint, 'd-none');
      this.renderer.removeClass(this.spacer, 'd-none');
    }
  }
}
