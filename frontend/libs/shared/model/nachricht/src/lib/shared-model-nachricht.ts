import { translatableShared } from '@dv/shared/assets/i18n';
import { Notification } from '@dv/shared/model/gesuch';

export const getNotificationTranslationKey = (nachricht: Notification) =>
  translatableShared(`shared.nachrichten.type.${nachricht.notificationType}`);

export type NotificationTranslationKey = ReturnType<
  typeof getNotificationTranslationKey
>;

export type SharedModelNachricht = Notification & {
  translationKey: NotificationTranslationKey;
};
