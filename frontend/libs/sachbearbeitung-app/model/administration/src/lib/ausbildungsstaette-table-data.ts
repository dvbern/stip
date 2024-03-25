import { Ausbildungsstaette } from '@dv/shared/model/gesuch';

export interface AusbildungsstaetteTableData extends Ausbildungsstaette {
  ausbildungsgaengeCount: number;
}
