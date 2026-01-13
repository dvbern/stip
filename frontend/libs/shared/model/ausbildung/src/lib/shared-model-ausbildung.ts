import {
  AusbildungDashboardItem,
  AusbildungsStatus,
  FallDashboardItem,
} from '@dv/shared/model/gesuch';
import { SharedModelNachricht } from '@dv/shared/model/nachricht';
import { Modify } from '@dv/shared/model/type-util';

export type SharedModelGsGesuchView = Modify<
  Exclude<AusbildungDashboardItem['gesuchs'], undefined>[number],
  {
    fallId: string;
    isActive: boolean;
    isErstgesuch: boolean;
    canEdit: boolean;
    canDelete: boolean;
    canCreateAenderung: boolean;
    canDeleteAenderung: boolean;
    einreichefristAbgelaufen: boolean;
    reduzierterBeitrag: boolean;
    einreichefristDays: number | null;
    yearRange: string;
  }
>;

export type SharedModelGsAusbildungView = Modify<
  AusbildungDashboardItem,
  {
    ausbildungBegin: Date | null;
    ausbildungEnd: Date | null;
    status: AusbildungsStatus;
    canDelete: boolean;
    gesuchs: SharedModelGsGesuchView[];
    bezeichnungDe: string;
    bezeichnungFr: string;
  }
>;
export type SharedModelGsDashboardView = Omit<
  FallDashboardItem,
  'ausbildungDashboardItems' | 'notifications'
> & {
  hasActiveAusbildungen: boolean;
  canCreateAusbildung: boolean;
  activeAusbildungen: SharedModelGsAusbildungView[];
  inactiveAusbildungen: SharedModelGsAusbildungView[];
  notifications: SharedModelNachricht[];
};

export type StatusType = 'ACTIVE' | 'INACTIVE' | undefined;
