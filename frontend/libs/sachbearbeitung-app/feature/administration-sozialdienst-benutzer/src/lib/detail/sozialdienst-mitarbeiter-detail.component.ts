import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  standalone: true,
  imports: [RouterLink],
  templateUrl: './sozialdienst-mitarbeiter-detail.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SozialdienstMitarbeiterDetailComponent {}
