import {
  Directive,
  TemplateRef,
  ViewContainerRef,
  effect,
  inject,
  input,
} from '@angular/core';

import {
  AppConfig,
  SharedModelCompileTimeConfig,
} from '@dv/shared/model/config';

@Directive({
  selector: '[dvIfTypeOneOf]',
  standalone: true,
})
export class SharedUiIfTypeOneOfDirective {
  viewContainerRef = inject(ViewContainerRef);
  templateRef = inject(TemplateRef);
  config = inject(SharedModelCompileTimeConfig);

  targetTypesSig = input.required<AppConfig['type'][]>({
    alias: 'dvIfTypeOneOf',
  });

  constructor() {
    effect(() => {
      if (this.targetTypesSig().includes(this.config.app.type)) {
        this.viewContainerRef.createEmbeddedView(this.templateRef);
      } else {
        this.viewContainerRef.clear();
      }
    });
  }
}
