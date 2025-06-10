import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  OnInit,
  computed,
  input,
  output,
} from '@angular/core';
import { TranslatePipe } from '@ngx-translate/core';
import { BehaviorSubject } from 'rxjs';

import { isDefined } from '@dv/shared/model/type-util';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';

type AllowedValues =
  | Date
  | [Date | undefined, Date | undefined]
  | string
  | number
  | undefined
  | null;

@Component({
  selector: 'dv-shared-ui-clear-button',
  imports: [CommonModule, SharedUiIconChipComponent, TranslatePipe],
  templateUrl: './shared-ui-clear-button.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiClearButtonComponent implements OnInit {
  valueSig = input.required<AllowedValues>();
  isVisibleSig = computed(() => {
    return hasValue(this.valueSig());
  });
  clear = output();

  private ngOnInit$ = new BehaviorSubject(false);

  ngOnInit() {
    this.ngOnInit$.next(true);
  }
}

const hasValue = (value: AllowedValues) => {
  if (!isDefined(value)) {
    return false;
  }
  if (value instanceof Date) {
    return value.getFullYear() > 1800;
  }
  if (typeof value === 'number') {
    return true;
  }
  if (Array.isArray(value)) {
    return value.filter(isDefined).length > 0;
  }
  return value.length > 0;
};
