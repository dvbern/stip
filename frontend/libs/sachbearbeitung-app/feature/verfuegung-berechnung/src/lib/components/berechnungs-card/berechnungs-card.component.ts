import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  input,
} from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatExpansionModule } from '@angular/material/expansion';
import { TranslocoDirective } from '@jsverse/transloco';

import { BerechnungsStammdaten } from '@dv/shared/model/gesuch';
import { isDefined } from '@dv/shared/model/type-util';
import { BerechnungPersonalOrFam } from '@dv/shared/model/verfuegung';
import { SharedUiFormatChfPipe } from '@dv/shared/ui/format-chf-pipe';

import { BerechnungsExpansionPanelComponent } from './berechnungs-expansion-panel.component';
import { PositionComponent } from '../position/position.component';

type PersonDetail = {
  geburtsdatum: string;
  nachname: string;
  sozialversicherungsnummer: string;
  vorname: string;
};

@Component({
  selector: 'dv-berechnungs-card',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    CommonModule,
    MatCardModule,
    MatExpansionModule,
    SharedUiFormatChfPipe,
    BerechnungsExpansionPanelComponent,
    TranslocoDirective,
    PositionComponent,
  ],
  templateUrl: './berechnungs-card.component.html',
})
export class BerechnungsCardComponent {
  berechnungSig = input.required<BerechnungPersonalOrFam>();
  stammdatenSig = input.required<BerechnungsStammdaten>();

  nameDetailsSig = computed<PersonDetail[]>(() => {
    const berechnung = this.berechnungSig();
    const partnerDetails = getPartnerDetails(berechnung);

    return [berechnung, ...(partnerDetails ? [partnerDetails] : [])];
  });
}

const getPartnerDetails = (berechnung: BerechnungPersonalOrFam) => {
  if (berechnung.typ != 'familien') {
    return null;
  }
  const {
    geburtsdatumPartner,
    nachnamePartner,
    sozialversicherungsnummerPartner,
    vornamePartner,
  } = berechnung;

  if (
    !isDefined(geburtsdatumPartner) ||
    !isDefined(nachnamePartner) ||
    !isDefined(sozialversicherungsnummerPartner) ||
    !isDefined(vornamePartner)
  ) {
    return null;
  }
  return {
    geburtsdatum: geburtsdatumPartner,
    nachname: nachnamePartner,
    sozialversicherungsnummer: sozialversicherungsnummerPartner,
    vorname: vornamePartner,
  };
};
