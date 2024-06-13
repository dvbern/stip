import {
  Directive,
  TemplateRef,
  ViewContainerRef,
  inject,
} from '@angular/core';

import { SharedModelCompiletimeConfig } from '@dv/shared/model/config';

@Directive({
  selector: '[dvIfGesuchsteller]',
  standalone: true,
})
export class SharedUiIfGesuchstellerDirective {
  viewContainerRef = inject(ViewContainerRef);
  templateRef = inject(TemplateRef);
  appType = inject(SharedModelCompiletimeConfig).appType;

  constructor() {
    if (this.appType === 'gesuch-app') {
      this.viewContainerRef.createEmbeddedView(this.templateRef);
    } else {
      this.viewContainerRef.clear();
    }
  }
}
