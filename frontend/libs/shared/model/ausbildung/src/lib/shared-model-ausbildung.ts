import {
  AusbildungDashboardItem,
  AusbildungsStatus,
  FallDashboardItem,
} from '@dv/shared/model/gesuch';
import { Modify } from '@dv/shared/model/type-util';

export type SharedModelGsGesuchView = Modify<
  Exclude<AusbildungDashboardItem['gesuchs'], undefined>[number],
  {
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
  'ausbildungDashboardItems'
> & {
  hasActiveAusbildungen: boolean;
  canCreateAusbildung: boolean;
  activeAusbildungen: SharedModelGsAusbildungView[];
  inactiveAusbildungen: SharedModelGsAusbildungView[];
};

export type StatusType = 'ACTIVE' | 'INACTIVE' | undefined;
