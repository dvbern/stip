import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  inject,
  viewChild,
} from '@angular/core';
import {
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { RouterLink } from '@angular/router';
import { MaskitoDirective } from '@maskito/angular';
import { Store } from '@ngrx/store';
import { TranslatePipe } from '@ngx-translate/core';

import {
  BuchhaltungEntryView,
  BuchhaltungStore,
} from '@dv/sachbearbeitung-app/data-access/buchhaltung';
import { selectRouteId } from '@dv/shared/data-access/gesuch';
import { SharedUiDownloadButtonDirective } from '@dv/shared/ui/download-button';
import { SharedUiFormatChfPipe } from '@dv/shared/ui/format-chf-pipe';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import {
  SharedUiRdIsPendingPipe,
  SharedUiRdIsPendingWithoutCachePipe,
} from '@dv/shared/ui/remote-data-pipe';
import {
  TypeSafeMatCellDefDirective,
  TypeSafeMatRowDefDirective,
} from '@dv/shared/ui/table-helper';
import { convertTempFormToRealValues } from '@dv/shared/util/form';
import {
  fromFormatedNumber,
  maskitoNumber,
} from '@dv/shared/util/maskito-util';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-infos-buchhaltung',
  standalone: true,
  imports: [
    CommonModule,
    TranslatePipe,
    ReactiveFormsModule,
    RouterLink,
    MaskitoDirective,
    MatFormFieldModule,
    MatInputModule,
    MatTableModule,
    MatPaginatorModule,
    SharedUiLoadingComponent,
    SharedUiRdIsPendingPipe,
    SharedUiFormatChfPipe,
    SharedUiRdIsPendingWithoutCachePipe,
    SharedUiDownloadButtonDirective,
    TypeSafeMatCellDefDirective,
    TypeSafeMatRowDefDirective,
  ],
  templateUrl: './sachbearbeitung-app-feature-infos-buchhaltung.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [BuchhaltungStore],
})
export class SachbearbeitungAppFeatureInfosBuchhaltungComponent {
  private formBuilder = inject(NonNullableFormBuilder);
  private store = inject(Store);
  private gesuchIdSig = this.store.selectSignal(selectRouteId);

  buchhaltungStore = inject(BuchhaltungStore);
  maskitoNumber = maskitoNumber;
  form = this.formBuilder.group({
    betrag: [<string | null>null, [Validators.required]],
  });
  sortSig = viewChild(MatSort);
  paginatorSig = viewChild(MatPaginator);
  displayedColumns = [
    'datum',
    'stipendienbetrag',
    'auszahlung',
    'rueckforderung',
    'saldo',
    'comment',
    'link',
  ];
  buchhaltungDataSourceSig = computed(() => {
    const buchhaltungEntries =
      this.buchhaltungStore.buchhaltungEntriesViewSig();

    const dataSource = new MatTableDataSource(buchhaltungEntries);
    const sort = this.sortSig();
    const paginator = this.paginatorSig();

    if (sort) {
      dataSource.sort = sort;
    }
    if (paginator) {
      dataSource.paginator = paginator;
    }

    return dataSource;
  });

  constructor() {
    effect(
      () => {
        const gesuchId = this.gesuchIdSig();

        if (!gesuchId) {
          return;
        }

        this.buchhaltungStore.loadBuchhaltung$({ gesuchId });
      },
      { allowSignalWrites: true },
    );
  }

  isStartOfNewGesuch(_: number, buchhaltungEntry: BuchhaltungEntryView) {
    return buchhaltungEntry.type === 'gesuchStart';
  }

  createBuchhaltungsKorrektur() {
    const gesuchId = this.gesuchIdSig();

    if (!gesuchId || this.form.invalid) {
      return;
    }

    const { betrag } = convertTempFormToRealValues(this.form, ['betrag']);

    this.buchhaltungStore.createBuchhaltungsKorrektur$({
      buchhaltungSaldokorrektur: {
        betrag: fromFormatedNumber(betrag),
        comment: 'Foobar',
        gesuchId,
      },
      gesuchId,
    });
  }
}
