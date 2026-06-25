import { Route } from '@angular/router';

import { SharedFeatureNotificationComponent } from './components/shared-feature-notification/shared-feature-notification.component';
import { SharedFeatureNotificationsComponent } from './shared-feature-notifications/shared-feature-notifications.component';

export const sharedFeatureNotificationsRoutes: Route[] = [
  {
    path: ':fallId',
    component: SharedFeatureNotificationsComponent,
    children: [
      {
        path: ':notificationId',
        component: SharedFeatureNotificationComponent,
      },
    ],
  },
];
