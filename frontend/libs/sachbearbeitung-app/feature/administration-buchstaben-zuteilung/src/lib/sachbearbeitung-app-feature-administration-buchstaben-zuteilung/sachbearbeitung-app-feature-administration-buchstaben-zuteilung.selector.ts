import { computed, inject } from '@angular/core';

import { SachbearbeiterStore } from '@dv/sachbearbeitung-app/data-access/sachbearbeiter';
import { BuchstabenZuordnung } from '@dv/sachbearbeitung-app/model/administration';

export const selectSachbearbeitungAppBuchstabenZuteilungView = () => {
  const buchstabenZuweisungStore = inject(SachbearbeiterStore);
  return computed(() => {
    return {
      sachbearbeiter:
        buchstabenZuweisungStore.sachbearbeiter?.()?.map(
          (u): BuchstabenZuordnung => ({
            benutzerId: u.id,
            fullName: `${u.vorname} ${u.nachname}`,
            enabledDe:
              (u.sachbearbeiterZuordnungStammdaten?.buchstabenDe?.length ?? 0) >
              0,
            buchstabenDe: u.sachbearbeiterZuordnungStammdaten?.buchstabenDe,
            enabledFr:
              (u.sachbearbeiterZuordnungStammdaten?.buchstabenFr?.length ?? 0) >
              0,
            buchstabenFr: u.sachbearbeiterZuordnungStammdaten?.buchstabenFr,
          }),
        ) ?? [],
      loading: buchstabenZuweisungStore.loading?.(),
    };
  });
};
