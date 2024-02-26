import { SachbearbeiterZuordnungStammdaten } from '@dv/shared/model/gesuch';

export type BuchstabenZuordnungUpdate = {
  sachbearbeiter: string;
  zuordnung: SachbearbeiterZuordnungStammdaten;
};
export type BuchstabenZuordnung = {
  benutzerId: string;
  fullName: string;
  enabledDe: boolean;
  buchstabenDe?: string;
  enabledFr: boolean;
  buchstabenFr?: string;
};
