import {
  Directive,
  TemplateRef,
  ViewContainerRef,
  inject,
} from '@angular/core';

import { SharedModelCompiletimeConfig } from '@dv/shared/model/config';

@Directive({
  selector: '[dvIfSachbearbeiter]',
  standalone: true,
})
export class SharedUiIfSachbearbeiterDirective {
  viewContainerRef = inject(ViewContainerRef);
  templateRef = inject(TemplateRef);
  appType = inject(SharedModelCompiletimeConfig).appType;

  constructor() {
    if (this.appType === 'sachbearbeitung-app') {
      this.viewContainerRef.createEmbeddedView(this.templateRef);
    } else {
      this.viewContainerRef.clear();
    }
  }
}
