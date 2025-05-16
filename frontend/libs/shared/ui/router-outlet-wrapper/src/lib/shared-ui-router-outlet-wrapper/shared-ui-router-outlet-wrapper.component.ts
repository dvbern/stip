import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  Output,
  ViewChild,
} from '@angular/core';
import { RouterModule, RouterOutlet } from '@angular/router';
import { delay, map } from 'rxjs';

@Component({
  selector: 'dv-shared-ui-router-outlet-wrapper',
  imports: [RouterModule],
  templateUrl: './shared-ui-router-outlet-wrapper.component.html',
  styleUrls: ['./shared-ui-router-outlet-wrapper.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiRouterOutletWrapperComponent {
  @Input({ required: true }) attribute!: string;
  @ViewChild('outlet', { read: RouterOutlet, static: true })
  public outlet!: RouterOutlet;
  public activated$ = new EventEmitter();
  @Output() public data = this.activated$.pipe(
    delay(0),
    map(() => this.outlet.activatedRoute.snapshot.data[this.attribute]),
  );
}
