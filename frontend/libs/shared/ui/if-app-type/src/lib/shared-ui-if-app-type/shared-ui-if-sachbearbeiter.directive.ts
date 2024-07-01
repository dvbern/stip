import {
  Directive,
  TemplateRef,
  ViewContainerRef,
  inject,
} from '@angular/core';

import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';

@Directive({
  selector: '[dvIfSachbearbeiter]',
  standalone: true,
})
export class SharedUiIfSachbearbeiterDirective {
  viewContainerRef = inject(ViewContainerRef);
  templateRef = inject(TemplateRef);
  config = inject(SharedModelCompileTimeConfig);

  constructor() {
    if (this.config.isSachbearbeitungApp) {
      this.viewContainerRef.createEmbeddedView(this.templateRef);
    } else {
      this.viewContainerRef.clear();
    }
  }
}
