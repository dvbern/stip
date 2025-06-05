import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  computed,
  effect,
  inject,
  signal,
  viewChild,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSelectModule } from '@angular/material/select';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { merge } from 'rxjs';

import { AdminAusbildungsstaetteStore } from '@dv/sachbearbeitung-app/data-access/ausbildungsstaette';
import { AusbildungsstaetteTableData } from '@dv/sachbearbeitung-app/model/administration';
import { Ausbildungsgang, Bildungsstufe } from '@dv/shared/model/gesuch';
import { detailExpand } from '@dv/shared/ui/animations';
import {
  ConfirmDialogData,
  SharedUiConfirmDialogComponent,
} from '@dv/shared/ui/confirm-dialog';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';
import { TranslatedPropertyPipe } from '@dv/shared/ui/translated-property-pipe';
import { SharedUtilFormService } from '@dv/shared/util/form';
import { paginatorTranslationProvider } from '@dv/shared/util/paginator-translation';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-administration-ausbildungsstaette',
  imports: [
    CommonModule,
    SharedUiLoadingComponent,
    MatFormFieldModule,
    SharedUiMaxLengthDirective,
    TranslatePipe,
    MatInputModule,
    MatTableModule,
    MatButtonModule,
    MatSortModule,
    ReactiveFormsModule,
    MatPaginatorModule,
    MatSelectModule,
    MatTooltipModule,
    TranslatedPropertyPipe,
    TypeSafeMatCellDefDirective,
  ],
  templateUrl:
    './sachbearbeitung-app-feature-administration-ausbildungsstaette.component.html',
  styleUrl:
    './sachbearbeitung-app-feature-administration-ausbildungsstaette.component.scss',
  animations: [detailExpand],
  providers: [AdminAusbildungsstaetteStore, paginatorTranslationProvider()],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureAdministrationAusbildungsstaetteComponent {
  store = inject(AdminAusbildungsstaetteStore);
  fb = inject(FormBuilder);
  dialog = inject(MatDialog);
  destroyRef = inject(DestroyRef);
  formUtils = inject(SharedUtilFormService);
  translate = inject(TranslateService);

  paginatorSig = viewChild(MatPaginator);
  sortSig = viewChild(MatSort);
  filterSig = signal('');

  filterColumns = ['filter'];

  detailColumn = ['expandedDetail'];

  displayedColumns: string[] = [
    'nameDe',
    'nameFr',
    'ausbildungsgaengeCount',
    'actions',
  ];

  expandedRowId: string | null = null;

  editedAusbildungsstaette: AusbildungsstaetteTableData | null = null;

  hasUnsavedChanges = false;

  form = this.fb.nonNullable.group({
    nameDe: ['', Validators.required],
    nameFr: ['', Validators.required],
  });

  displayedChildColumns: string[] = [
    'bezeichnungDe',
    'bezeichnungFr',
    'bildungskategorie',
    'actions',
  ];

  editedAusbildungsgang: Ausbildungsgang | null = null;

  gangForm = this.fb.nonNullable.group({
    bezeichnungDe: ['', Validators.required],
    bezeichnungFr: ['', Validators.required],
    bildungskategorieId: ['', Validators.required],
  });

  private tableDataSig = computed(() => {
    const tableData = this.store.tableData();
    return new MatTableDataSource(tableData);
  });
  dataSourceSig = computed(() => {
    const sort = this.sortSig();
    const paginator = this.paginatorSig();
    const datasource = this.tableDataSig();

    if (sort) {
      datasource.sort = sort;
    }
    if (paginator) {
      datasource.paginator = paginator;
    }
    return datasource;
  });

  constructor() {
    this.formUtils.registerFormForUnsavedCheck(this);
    merge(this.gangForm.valueChanges, this.form.valueChanges)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => {
        this.hasUnsavedChanges = this.gangForm.dirty || this.form.dirty;
      });

    effect(() => {
      const response = this.store.response();

      if (response.type === 'success') {
        this.endEdit();
      }
    });

    effect(() => {
      const filter = this.filterSig();
      this.dataSourceSig().filter = filter.trim().toLowerCase();
    });

    this.store.loadAusbildungsstaetten({});
    this.store.loadBildungskategorien({});
  }

  setFilter(filter: string) {
    this.dataSourceSig().filter = filter.trim();
  }

  // Ausbildungsstaette ==========================================================

  addAusbildungsstaette() {
    const newRow = {
      id: 'new',
      nameDe: '',
      nameFr: '',
      ausbildungsgaengeCount: 0,
    };

    this.editedAusbildungsstaette = newRow;

    this.store.addAusbildungsstaetteRow(newRow);
    this.paginatorSig()?.firstPage();
  }

  expandRow(staette: AusbildungsstaetteTableData) {
    if (
      this.editedAusbildungsgang ||
      this.editedAusbildungsstaette ||
      !staette.id
    ) {
      return;
    }

    if (this.expandedRowId === staette.id) {
      this.expandedRowId = null;
    } else {
      this.expandedRowId = staette.id;
    }
  }

  editAusbildungsstaette(ausbildungsstaette: AusbildungsstaetteTableData) {
    this.editedAusbildungsstaette = ausbildungsstaette;
    this.form.patchValue(ausbildungsstaette, { emitEvent: false });
  }

  cancelEditAusbildungsstaette(staette: AusbildungsstaetteTableData) {
    if (this.editedAusbildungsstaette?.id === 'new') {
      this.store.removeNewAusbildungsstaetteRow(staette);
    }

    this.endEdit();
  }

  saveAusbildungsstaette() {
    if (!this.editedAusbildungsstaette) {
      return;
    }

    const update = {
      ...this.editedAusbildungsstaette,
      ...this.form.value,
    };

    this.store.handleCreateUpdateAusbildungsstaette(update);
  }

  deleteAusbildungsstaette(staette: AusbildungsstaetteTableData) {
    const dialogRef = SharedUiConfirmDialogComponent.open(this.dialog, {
      title: 'sachbearbeitung-app.admin.ausbildungsstaette.deleteTitle',
      message: 'sachbearbeitung-app.admin.ausbildungsstaette.deleteMessage',
      confirmText: 'shared.ui.yes',
      cancelText: 'shared.ui.no',
      translationObject: staette,
    });

    dialogRef
      .afterClosed()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((confirmed) => {
        if (confirmed) {
          this.store.deleteAusbildungsstaette(staette);
        }
      });
  }

  // Ausbildungsgang ==========================================================

  addAusbildungsgang(staette: AusbildungsstaetteTableData) {
    const newRow: Ausbildungsgang = {
      id: 'new',
      bezeichnungDe: '',
      bezeichnungFr: '',
      bildungskategorie: {
        id: '',
        bezeichnungDe: '',
        bezeichnungFr: '',
        bfs: 0,
        bildungsstufe: Bildungsstufe.SEKUNDAR_2,
      },
    };

    this.editedAusbildungsgang = newRow;

    if (!staette.id) {
      return;
    }

    this.store.addAusbildungsgangRow(staette.id, newRow);
  }

  editAusbildungsgang(ausbildungsgang: Ausbildungsgang) {
    this.editedAusbildungsgang = ausbildungsgang;
    this.gangForm.patchValue(
      {
        bezeichnungDe: ausbildungsgang.bezeichnungDe,
        bezeichnungFr: ausbildungsgang.bezeichnungFr,
        bildungskategorieId: ausbildungsgang.bildungskategorie.id,
      },
      { emitEvent: false },
    );
  }

  cancelEditAusbildungsgang(
    staette: AusbildungsstaetteTableData,
    gang: Ausbildungsgang,
  ) {
    if (this.editedAusbildungsgang?.id === 'new') {
      this.store.removeNewAusbildungsgangRow(staette, gang);
    }

    this.endEdit();
  }

  saveAusbildungsgang(staette: AusbildungsstaetteTableData) {
    if (!this.editedAusbildungsgang) {
      return;
    }

    const gang = {
      ...this.editedAusbildungsgang,
      ...this.gangForm.value,
    };

    this.store.handleCreateUpdateAusbildungsgang({ staette, gang });
  }

  deleteAusbildungsgang(
    staette: AusbildungsstaetteTableData,
    gang: Ausbildungsgang,
  ) {
    const dialogRef = this.dialog.open<unknown, ConfirmDialogData, boolean>(
      SharedUiConfirmDialogComponent,
      {
        data: {
          title: 'sachbearbeitung-app.admin.ausbildungsgaenge.deleteTitle',
          message: 'sachbearbeitung-app.admin.ausbildungsgaenge.deleteMessage',
          confirmText: 'shared.ui.yes',
          cancelText: 'shared.ui.no',
          translationObject: gang,
        },
      },
    );

    dialogRef
      .afterClosed()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((confirmed) => {
        if (confirmed) {
          this.store.deleteAusbildungsgang({ staette, gang });
        }
      });
  }

  private endEdit() {
    this.editedAusbildungsstaette = null;
    this.editedAusbildungsgang = null;
    this.form.reset();
    this.gangForm.reset();
    this.hasUnsavedChanges = false;
  }
}
