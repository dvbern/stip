import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  Input,
  OnInit,
} from '@angular/core';

@Component({
  selector: 'dv-shared-ui-badge',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './shared-ui-badge.component.html',
  styleUrl: './shared-ui-badge.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiBadgeComponent implements OnInit {
  @Input() type: 'success' | 'warning' | 'danger' | 'info' = 'warning';

  @HostBinding('class') class = 'info';

  ngOnInit() {
    this.class = this.type;
  }
}
