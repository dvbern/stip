import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, inject } from '@angular/core';

import { BenutzerverwaltungStore } from '@dv/sachbearbeitung-app/data-access/benutzerverwaltung';

@Component({
  selector: 'lib-sachbearbeitung-app-feature-administration-benutzerverwaltung',
  standalone: true,
  imports: [CommonModule],
  templateUrl:
    './sachbearbeitung-app-feature-administration-benutzerverwaltung.component.html',
  styleUrl:
    './sachbearbeitung-app-feature-administration-benutzerverwaltung.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureAdministrationBenutzerverwaltungComponent {
  name?: string;
  vorname?: string;
  email?: string;
  roles = new Set<{ id: string; name: string }>();
  benutzerverwaltungStore = inject(BenutzerverwaltungStore);

  constructor() {
    this.benutzerverwaltungStore.loadAvailableRoles$();
  }

  toggle(role: { id: string; name: string }, checked: boolean) {
    if (checked) {
      this.roles.add(role);
    } else {
      this.roles.delete(role);
    }
  }

  save() {
    if (!this.name || !this.vorname || !this.email || this.roles.size === 0) {
      return;
    }
    this.benutzerverwaltungStore.registerUser$({
      name: this.name,
      vorname: this.vorname,
      email: this.email,
      roles: Array.from(this.roles),
    });
  }
}
