/* eslint-disable @angular-eslint/no-input-rename */
import {
  Component,
  computed,
  effect,
  inject,
  input,
  signal,
} from '@angular/core';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';

import { AuszahlungStore } from '@dv/shared/data-access/auszahlung';
import { EinreichenStore } from '@dv/shared/data-access/einreichen';
import { selectLanguage } from '@dv/shared/data-access/language';
import {
  SharedDataAccessStammdatenApiEvents,
  selectSharedDataAccessStammdatensView,
} from '@dv/shared/data-access/stammdaten';
import { SharedModelAuszahlung } from '@dv/shared/model/auszahlung';
import { AuszahlungUpdate } from '@dv/shared/model/gesuch';
import { isDefined } from '@dv/shared/model/type-util';
import { isPending } from '@dv/shared/util/remote-data';

@Component({
  selector: 'dv-shared-feature-auszahlung',
  imports: [],
  template: 'only for extension usage',
})
export abstract class SharedFeatureAuszahlungComponent {
  private store = inject(Store);
  private router = inject(Router);
  private einreichenStore = inject(EinreichenStore);
  private auszahlungStore = inject(AuszahlungStore);
  private stammdatenViewSig = this.store.selectSignal(
    selectSharedDataAccessStammdatensView,
  );

  hasUnsavedChanges = false;
  fallIdSig = input.required<string>({ alias: 'fallId' });
  backlinkSig = input<string | null>(null, { alias: 'backlink' });
  optionalBacklinkSig = signal<string | null>(null);
  laenderSig = computed(() => {
    return this.stammdatenViewSig().laender;
  });
  languageSig = this.store.selectSignal(selectLanguage);
  auszahlungViewSig = computed<SharedModelAuszahlung>(() => {
    const laender = this.stammdatenViewSig()?.laender || [];
    const language = this.languageSig();
    const auszahlung = this.auszahlungStore.auszahlung();
    const invalidFormularControls =
      this.einreichenStore.invalidFormularControlsSig();
    const backlink = this.optionalBacklinkSig();

    return {
      auszahlung: auszahlung.data,
      isLoading: isPending(auszahlung),
      readonly: false,
      laender,
      language,
      backlink,
      invalidFormularControls,
    };
  });

  constructor() {
    this.store.dispatch(SharedDataAccessStammdatenApiEvents.init());

    effect(() => {
      const fallId = this.fallIdSig();

      this.auszahlungStore.loadAuszahlung$({ fallId });
    });

    effect(() => {
      const backlink = this.backlinkSig();

      if (isDefined(backlink)) {
        this.optionalBacklinkSig.set(backlink);
        this.router.navigate([], {
          queryParams: { backlink: undefined },
          replaceUrl: true,
        });
      }
    });
  }

  handleSave(auszahlung: AuszahlungUpdate): void {
    const fallId = this.fallIdSig();

    if (isDefined(fallId)) {
      if (this.auszahlungStore.auszahlung().data?.value) {
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
}
