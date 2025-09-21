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
export class SharedUiFilterMenuButtonComponent {
  filters = input<{ typ: string; icon: string; roles: string[] }[]>([]);
  activeFilter = input<string | undefined>('');
  defaultFilter = input.required<string>();
  filterChange = output<string>();

  selectedFilter = computed(() => {
    return (
      this.filters().find((filter) => filter.typ === this.activeFilter())
        ?.typ || this.defaultFilter()
    );
  });

  isSelected = computed(() => {
    return this.filters().some((filter) => filter.typ === this.activeFilter());
  });
}
