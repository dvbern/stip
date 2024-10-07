import { SharedModelError } from '@dv/shared/model/error';

export type Notification = {
  id: number;
  type: NotificationType;
  message?: string;
  messageKey?: string;
  content?: SharedModelError;
};

export type CreateNotification = Omit<Notification, 'id'>;

export type NotificationType =
  | 'SEVERE'
  | 'ERROR'
  | 'ERROR_PERMANENT'
  | 'INFO'
  | 'WARNING'
  | 'SUCCESS';

export type SharedModelGlobalNotification = {
  autohide: boolean;
  type: NotificationType;
  list: Notification[];
};
