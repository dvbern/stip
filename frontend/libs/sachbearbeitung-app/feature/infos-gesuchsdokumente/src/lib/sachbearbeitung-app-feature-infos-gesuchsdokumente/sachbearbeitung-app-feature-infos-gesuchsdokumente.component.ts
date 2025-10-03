import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  inject,
  input,
} from '@angular/core';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { RouterLink } from '@angular/router';
import { TranslocoPipe } from '@jsverse/transloco';

import { InfosGesuchsdokumenteStore } from '@dv/sachbearbeitung-app/data-access/infos-gesuchsdokumente';
import { Verfuegung } from '@dv/shared/model/gesuch';
import { SharedUiDownloadButtonDirective } from '@dv/shared/ui/download-button';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';
import { paginatorTranslationProvider } from '@dv/shared/util/paginator-translation';

@Component({
  imports: [
    CommonModule,
    TranslocoPipe,
    MatTableModule,
    TypeSafeMatCellDefDirective,
    SharedUiDownloadButtonDirective,
    RouterLink,
  ],
  providers: [paginatorTranslationProvider()],
  templateUrl:
    './sachbearbeitung-app-feature-infos-gesuchsdokumente.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureInfosAdminComponent {
  private infosAdminStore = inject(InfosGesuchsdokumenteStore);
  displayColumns = ['timestamp', 'verfuegung'];
  datenschutzbriefMassendruckJobIdSig = computed(() => {
    return this.infosAdminStore.adminDokumenteViewSig()
      ?.datenschutzbriefMassendruckJobId;
  });
  verfuegungenSig = computed(() => {
    const { verfuegungen } = this.infosAdminStore.adminDokumenteViewSig() ?? {};
    return new MatTableDataSource<Verfuegung>(verfuegungen ?? []);
  });
  // eslint-disable-next-line @angular-eslint/no-input-rename
  gesuchIdSig = input.required<string>({ alias: 'id' });

  constructor() {
    effect(() => {
      const gesuchId = this.gesuchIdSig();

      if (!gesuchId) {
        return;
      }

      this.infosAdminStore.loadAdminDokumente$({ gesuchId });
    });
  }
}
