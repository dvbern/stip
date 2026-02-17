import { SharedModelError } from '@dv/shared/model/error';

export type Notification<T extends string = string> = {
  id: number;
  type: NotificationType;
  message?: string;
  messageKey?: T;
  content?: SharedModelError;
};

export type CreateNotification<T extends string = string> = Omit<
  Notification<T>,
  'id'
>;

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
