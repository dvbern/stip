import { SachbearbeiterZuordnungStammdaten } from '@dv/shared/model/gesuch';

export type BuchstabenZuordnungUpdate = {
  benutzerId: string;
  sachbearbeiterZuordnungStammdaten: SachbearbeiterZuordnungStammdaten;
};
export type BuchstabenZuordnung = {
  benutzerId: string;
  fullName: string;
  enabledDe: boolean;
  buchstabenDe?: string;
  enabledFr: boolean;
  buchstabenFr?: string;
};
