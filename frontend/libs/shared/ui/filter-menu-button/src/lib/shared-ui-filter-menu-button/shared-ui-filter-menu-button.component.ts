import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  input,
  output,
} from '@angular/core';
import { MatMenuModule } from '@angular/material/menu';
import { TranslocoPipe } from '@jsverse/transloco';

@Component({
  selector: 'dv-shared-ui-filter-menu-button',
  imports: [TranslocoPipe, MatMenuModule, CommonModule],
  templateUrl: './shared-ui-filter-menu-button.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiFilterMenuButtonComponent<T> {
  filters = input.required<{ typ: T }[]>();
  activeFilter = input.required<T | undefined>();
  defaultFilter = input.required<T>();
  filterChange = output<T>();
  /**
   * Base translation key for the filters.
   * @example 'sachbearbeitung-app.cockpit.quick-filter.'
   * Make sure to include the trailing dot.
   */
  baseTranslationKey = input.required<string>();

  selectedFilterSig = computed(() => {
    return (
      this.filters().find((filter) => filter.typ === this.activeFilter())
        ?.typ ?? this.defaultFilter()
    );
  });

  isSelectedSig = computed(() => {
    return this.filters().some((filter) => filter.typ === this.activeFilter());
  });
}
