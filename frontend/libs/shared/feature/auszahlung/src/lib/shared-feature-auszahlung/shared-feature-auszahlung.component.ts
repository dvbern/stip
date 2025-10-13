/* eslint-disable @angular-eslint/no-input-rename */
import { Component, computed, effect, inject, input } from '@angular/core';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';

import { AuszahlungStore } from '@dv/shared/data-access/auszahlung';
import { EinreichenStore } from '@dv/shared/data-access/einreichen';
import { selectSharedDataAccessGesuchsView } from '@dv/shared/data-access/gesuch';
import { LandStore } from '@dv/shared/data-access/land';
import { selectLanguage } from '@dv/shared/data-access/language';
import { SharedEventGesuchFormAuszahlung } from '@dv/shared/event/gesuch-form-auszahlung';
import { PermissionStore } from '@dv/shared/global/permission';
import { SharedModelAuszahlung } from '@dv/shared/model/auszahlung';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import { AuszahlungUpdate } from '@dv/shared/model/gesuch';
import { AUSZAHLUNG } from '@dv/shared/model/gesuch-form';
import { isNotReadonly } from '@dv/shared/model/permission-state';
import { isDefined } from '@dv/shared/model/type-util';
import { isPending } from '@dv/shared/util/remote-data';
import { SharedUtilRouteHistoryService } from '@dv/shared/util/route-history';

@Component({
  selector: 'dv-shared-feature-auszahlung',
  imports: [],
  template: 'only for extension usage',
})
export abstract class SharedFeatureAuszahlungComponent {
  protected store = inject(Store);
  protected router = inject(Router);
  private einreichenStore = inject(EinreichenStore);
  private auszahlungStore = inject(AuszahlungStore);
  private permissionStore = inject(PermissionStore);
  private routeHistoryService = inject(SharedUtilRouteHistoryService);

  private config = inject(SharedModelCompileTimeConfig);

  hasUnsavedChanges = false;

  // QueryParam inputs
  fallIdQuerySig = input<string | undefined>(undefined, { alias: 'fallId' });

  fallIdSig = computed(() => {
    const fallIdQuery = this.fallIdQuerySig();

    if (isDefined(fallIdQuery)) {
      return fallIdQuery;
    }

    const { gesuch } = this.gesuchsViewSig();
    return gesuch?.fallId;
  });
  laenderStore = inject(LandStore);
  languageSig = this.store.selectSignal(selectLanguage);
  gesuchsViewSig = this.store.selectSignal(selectSharedDataAccessGesuchsView);
  auszahlungViewSig = computed<SharedModelAuszahlung>(() => {
    const laender = this.laenderStore.landListViewSig() ?? [];
    const language = this.languageSig();
    const auszahlung = this.auszahlungStore.auszahlung();
    const rolesMap = this.permissionStore.rolesMapSig();
    const invalidFormularControls =
      this.einreichenStore.invalidFormularControlsSig();

    return {
      auszahlung: auszahlung.data,
      isLoading: isPending(auszahlung),
      readonly: !isNotReadonly(
        this.config.appType,
        rolesMap,
        auszahlung.data?.isDelegated,
      ),
      laender,
      language,
      invalidFormularControls,
    };
  });

  constructor() {
    this.laenderStore.loadLaender$();

    effect(() => {
      const fallId = this.fallIdSig();

      if (isDefined(fallId)) {
        this.auszahlungStore.loadAuszahlung$({ fallId });
      }
    });
  }

  handleSave(auszahlung: AuszahlungUpdate, alternativeBackRoute: string): void {
    const fallId = this.fallIdSig();
    const onSuccess = () => {
      this.routeHistoryService.navigateToPreviousPage(alternativeBackRoute);
    };

    if (isDefined(fallId)) {
      if (this.auszahlungStore.auszahlung().data?.auszahlung) {
        this.auszahlungStore.updateAuszahlung$({
          fallId,
          auszahlung,
          onSuccess,
        });
      } else {
        this.auszahlungStore.createAuszahlung$({
          fallId,
          auszahlung,
          onSuccess,
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
