import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  inject,
  input,
  signal,
} from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { TranslatePipe } from '@ngx-translate/core';

import { Notification } from '@dv/shared/model/gesuch';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiNotificationDialogComponent } from '@dv/shared/ui/notification-dialog';
import { SharedUiTooltipDateComponent } from '@dv/shared/ui/tooltip-date';
import { paginatorTranslationProvider } from '@dv/shared/util/paginator-translation';

@Component({
  selector: 'dv-shared-ui-notifications',
  imports: [
    CommonModule,
    TranslatePipe,
    MatPaginatorModule,
    SharedUiIconChipComponent,
    SharedUiTooltipDateComponent,
  ],
  providers: [paginatorTranslationProvider()],
  templateUrl: './shared-ui-notifications.component.html',
  styleUrl: './shared-ui-notifications.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedUiNotificationsComponent {
  private dialog = inject(MatDialog);
  readonly pageSize = 5;
  // eslint-disable-next-line @angular-eslint/no-input-rename
  notificationsSig = input.required<Notification[]>({ alias: 'notifications' });
  newPageSig = signal<PageEvent | null>(null);

  notificationsViewSig = computed(() => {
    const notifications = this.notificationsSig();
    const page = this.newPageSig();

    if (!page) {
      return notifications.slice(0, this.pageSize);
    }
    return notifications.slice(
      page.pageIndex * page.pageSize,
      page.pageIndex * page.pageSize + page.pageSize,
    );
  });

  openNotification(notification: Notification) {
    SharedUiNotificationDialogComponent.open(this.dialog, notification);
  }
}
