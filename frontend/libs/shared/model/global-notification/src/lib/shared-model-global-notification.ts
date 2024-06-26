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
  | 'INFO'
  | 'WARNING'
  | 'SUCCESS'
  | 'SUCCESS_PERMANENT';

export type SharedModelGlobalNotification = {
  autohide: boolean;
  type: NotificationType;
  list: Notification[];
};
