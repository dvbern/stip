import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  Input,
} from '@angular/core';

@Component({
  selector: 'dv-shared-ui-table-header-filter',
  imports: [],
  templateUrl: './shared-ui-table-header-filter.component.html',
  styleUrl: './shared-ui-table-header-filter.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiTableHeaderFilterComponent {
  @Input({ required: true }) text!: string;
  @HostBinding('class') class =
    'd-flex flex-column align-items-start position-relative pb-4';
}
