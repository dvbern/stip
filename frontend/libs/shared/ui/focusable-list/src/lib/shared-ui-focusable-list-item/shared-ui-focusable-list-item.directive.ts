import { Highlightable } from '@angular/cdk/a11y';
import { Directive, ElementRef, inject, output } from '@angular/core';
import { RouterLink } from '@angular/router';

@Directive({
  selector: '[dvSharedUiFocusableListItem]',
  standalone: true,
})
export class SharedUiFocusableListItemDirective implements Highlightable {
  interacted = output();
  private routerLink = inject(RouterLink, { optional: true });
  private elementRef: ElementRef<HTMLElement> = inject(ElementRef);
  private _disabled?: boolean;
  public get disabled() {
    return this._disabled;
  }

  setActiveStyles(): void {
    this.elementRef.nativeElement.classList.add('active');
  }
  setInactiveStyles(): void {
    this.elementRef.nativeElement.classList.remove('active');
  }
  interact() {
    this.interacted.emit();
    this.routerLink?.onClick(1, false, false, false, false);
  }
}
