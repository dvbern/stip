import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Input,
  computed,
  inject,
} from '@angular/core';
import { MatSidenavModule } from '@angular/material/sidenav';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslatePipe } from '@ngx-translate/core';

import {
  VERFUEGUNG_OPTIONS,
  VerfuegungOption,
  createBerechnungOption,
} from '@dv/sachbearbeitung-app/model/verfuegung';
import { SachbearbeitungAppPatternGesuchHeaderComponent } from '@dv/sachbearbeitung-app/pattern/gesuch-header';
import { BerechnungStore } from '@dv/shared/data-access/berechnung';
import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import { SharedPatternAppHeaderComponent } from '@dv/shared/pattern/app-header';
import { SharedPatternMobileSidenavComponent } from '@dv/shared/pattern/mobile-sidenav';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';

@Component({
  selector: 'dv-sachbearbeitung-app-pattern-verfuegung-layout',
  standalone: true,
  imports: [
    CommonModule,
    TranslatePipe,
    RouterLink,
    RouterLinkActive,
    MatSidenavModule,
    SharedPatternMobileSidenavComponent,
    SharedPatternAppHeaderComponent,
    SharedUiIconChipComponent,
    SachbearbeitungAppPatternGesuchHeaderComponent,
  ],
  styleUrl: './sachbearbeitung-app-pattern-verfuegung-layout.component.scss',
  templateUrl: './sachbearbeitung-app-pattern-verfuegung-layout.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [BerechnungStore],
})
export class SachbearbeitungAppPatternVerfuegungLayoutComponent {
  @Input() option?: VerfuegungOption;
  navClicked$ = new EventEmitter();
  route = inject(Router);
  store = inject(Store);
  gesuchViewSig = this.store.selectSignal(selectSharedDataAccessGesuchsView);
  verfuegungOptions = VERFUEGUNG_OPTIONS;

  berechnungStore = inject(BerechnungStore);

  berechnungenSig = computed(() => {
    const gesuchId = this.gesuchViewSig().gesuchId;
    // berechnungenOptions will be fetched dynamically in the future
    const berechnungenOptions: VerfuegungOption[] = [];

    const berechnung = this.berechnungStore.berechnungZusammenfassungViewSig();
    berechnung.berechnungsresultate.forEach((berechnungen, berechnungIndex) => {
      berechnungen.forEach((berechnung) => {
        berechnungenOptions.push(
          createBerechnungOption(berechnungIndex, berechnung.type),
        );
      });
    });

    return berechnungenOptions.map((option) => ({
      ...option,
      fullRoute: ['/', 'verfuegung', gesuchId, ...option.route.split('/')],
    }));
  });
}
