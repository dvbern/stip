import {
  AfterViewInit,
  Directive,
  OnDestroy,
  TemplateRef,
  inject,
} from '@angular/core';

import { SharedUtilHeaderService } from '@dv/shared/util/header';

@Directive({
  selector: '[dvHeaderSuffix]',
  standalone: true,
})
export class SharedUiHeaderSuffixDirective implements AfterViewInit, OnDestroy {
  headerService = inject(SharedUtilHeaderService);
  templateRef = inject(TemplateRef<unknown>);

  ngAfterViewInit(): void {
    this.headerService.updateSuffix(this.templateRef);
  }

  ngOnDestroy(): void {
    this.headerService.removeSuffix();
  }
}
