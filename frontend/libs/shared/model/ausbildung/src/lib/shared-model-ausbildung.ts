import {
  AusbildungDashboardItem,
  AusbildungsStatus,
  FallDashboardItem,
} from '@dv/shared/model/gesuch';
import { Modify, ModifyList } from '@dv/shared/model/type-util';

export type SharedModelGsGesuchView = Modify<
  Exclude<AusbildungDashboardItem['gesuchs'], undefined>[number],
  {
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
    gesuchs: ModifyList<
      AusbildungDashboardItem['gesuchs'],
      {
        einreichefristAbgelaufen: boolean;
        reduzierterBeitrag: boolean;
        einreichefristDays: number | null;
        yearRange: string;
      }
    >;
  }
>;
export type SharedModelGsDashboardView = Omit<
  FallDashboardItem,
  'ausbildungDashboardItems'
> & {
  hasActiveAusbildungen: boolean;
  activeAusbildungen: SharedModelGsAusbildungView[];
  inactiveAusbildungen: SharedModelGsAusbildungView[];
};
