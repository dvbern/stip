import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  OnInit,
  computed,
  effect,
  inject,
} from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslatePipe } from '@ngx-translate/core';

import { AuszahlungStore } from '@dv/shared/data-access/auszahlung';
import { EinreichenStore } from '@dv/shared/data-access/einreichen';
import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import { selectLanguage } from '@dv/shared/data-access/language';
import {
  SharedDataAccessStammdatenApiEvents,
  selectSharedDataAccessStammdatensView,
} from '@dv/shared/data-access/stammdaten';
import { SharedEventGesuchFormAuszahlung } from '@dv/shared/event/gesuch-form-auszahlung';
import { SharedModelAuszahlung } from '@dv/shared/model/auszahlung';
import { AuszahlungUpdate } from '@dv/shared/model/gesuch';
import { AUSZAHLUNG } from '@dv/shared/model/gesuch-form';
import { isDefined } from '@dv/shared/model/type-util';
import { SharedUiAuszahlungComponent } from '@dv/shared/ui/auszahlung';
import { SharedUiIfGesuchstellerDirective } from '@dv/shared/ui/if-app-type';
import { isPending } from '@dv/shared/util/remote-data';

@Component({
  selector: 'dv-shared-feature-gesuch-form-auszahlung',
  imports: [
    RouterLink,
    TranslatePipe,
    SharedUiAuszahlungComponent,
    SharedUiIfGesuchstellerDirective,
  ],
  templateUrl: './shared-feature-gesuch-form-auszahlung.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchFormAuszahlungComponent implements OnInit {
  private store = inject(Store);
  private einreichenStore = inject(EinreichenStore);
  private auszahlungStore = inject(AuszahlungStore);
  private stammdatenViewSig = this.store.selectSignal(
    selectSharedDataAccessStammdatensView,
  );
  private languageSig = this.store.selectSignal(selectLanguage);
  private fallIdSig = computed(() => {
    const { gesuch } = this.gesuchsViewSig();
    return gesuch?.fallId;
  });

  router = inject(Router);
  gesuchsViewSig = this.store.selectSignal(selectSharedDataAccessGesuchsView);
  hasUnsavedChanges = false;
  auszahlungViewSig = computed<SharedModelAuszahlung>(() => {
    const laender = this.stammdatenViewSig()?.laender || [];
    const language = this.languageSig();
    const auszahlung = this.auszahlungStore.auszahlung();
    const invalidFormularControls =
      this.einreichenStore.invalidFormularControlsSig();

    return {
      auszahlung: auszahlung.data,
      isLoading: isPending(auszahlung),
      laender,
      language,
      invalidFormularControls,
      readonly: true,
    };
  });

  @HostBinding('class')
  hostClass = 'tw-pt-6';

  constructor() {
    effect(() => {
      const fallId = this.fallIdSig();

      if (!isDefined(fallId)) {
        return;
      }

      this.auszahlungStore.loadAuszahlung$({ fallId });
    });
  }

  ngOnInit(): void {
    this.store.dispatch(SharedDataAccessStammdatenApiEvents.init());
    this.store.dispatch(SharedEventGesuchFormAuszahlung.init());
  }

  handleSave(auszahlung: AuszahlungUpdate): void {
    const fallId = this.fallIdSig();

    if (isDefined(fallId)) {
      if (this.auszahlungStore.auszahlung().data?.auszahlung) {
        this.auszahlungStore.updateAuszahlung$({
          fallId,
          auszahlung,
        });
      } else {
        this.auszahlungStore.createAuszahlung$({
          fallId,
          auszahlung,
        });
      }
    }
  }

  handleContinue() {
    const { gesuchId, trancheId } = this.gesuchsViewSig();
    if (gesuchId && trancheId) {
      this.store.dispatch(
        SharedEventGesuchFormAuszahlung.nextTriggered({
          id: gesuchId,
          trancheId,
          origin: AUSZAHLUNG,
        }),
      );
    }
  }
}
