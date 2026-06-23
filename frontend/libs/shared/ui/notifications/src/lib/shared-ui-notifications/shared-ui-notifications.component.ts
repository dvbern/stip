import { A11yModule } from '@angular/cdk/a11y';
import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  input,
  output,
  signal,
} from '@angular/core';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { RouterModule } from '@angular/router';
import { TranslocoPipe } from '@jsverse/transloco';

import { SharedModelNachricht } from '@dv/shared/model/nachricht';
import { SharedUiTooltipDateComponent } from '@dv/shared/ui/tooltip-date';
import { paginatorTranslationProvider } from '@dv/shared/util/paginator-translation';

@Component({
  selector: 'dv-shared-ui-notifications',
  imports: [
    TranslocoPipe,
    MatPaginatorModule,
    SharedUiTooltipDateComponent,
    A11yModule,
    RouterModule,
    CommonModule,
  ],
  providers: [paginatorTranslationProvider()],
  templateUrl: './shared-ui-notifications.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiNotificationsComponent {
  @HostBinding('class') class = '';

  // todo: implement in KSTIP-3146
  // private dialog = inject(MatDialog);
  notificationsSig = input.required<SharedModelNachricht[]>({
    // eslint-disable-next-line @angular-eslint/no-input-rename
    alias: 'notifications',
  });
  selectedNotificationIdSig = input<string | undefined>(undefined, {
    // eslint-disable-next-line @angular-eslint/no-input-rename
    alias: 'selectedNotificationId',
  });
  isMobile = input<boolean>(false);
  notificationClick = output<SharedModelNachricht>();
  newPageSig = signal<PageEvent | null>(null);

  // todo: add scrolling to list in KSTIP-3146
  // todo: implement in KSTIP-3146
  // openNotification(notification: SharedModelNachricht) {
  //   if (this.isMobile()) {
  //     this.notificationClick.emit(notification);
  //   } else {
  //     SharedUiNotificationDialogComponent.open(this.dialog, notification);
  //   }
  // }
}
