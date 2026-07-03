import {
  Directive,
  TemplateRef,
  ViewContainerRef,
  inject,
} from '@angular/core';

import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';

@Directive({
  selector: '[dvIfSozialdienst]',
  standalone: true,
})
export class SharedUiIfSozialdienstDirective {
  viewContainerRef = inject(ViewContainerRef);
  templateRef = inject(TemplateRef);
  config = inject(SharedModelCompileTimeConfig);

  constructor() {
    // todo: implemented after merge of KSTIP-3676
    // if (this.config.isSozialdienstApp) {
    //   this.viewContainerRef.createEmbeddedView(this.templateRef);
    // } else {
    //   this.viewContainerRef.clear();
    // }
  }
}
