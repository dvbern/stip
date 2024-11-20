import { CommonModule } from '@angular/common';
import {
  AfterViewInit,
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  ViewChild,
  effect,
  inject,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import {
  MatPaginator,
  MatPaginatorIntl,
  MatPaginatorModule,
} from '@angular/material/paginator';
import { MatSelectModule } from '@angular/material/select';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
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
import { SharedUiFormFieldDirective } from '@dv/shared/ui/form';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';
import { TranslatedPropertyPipe } from '@dv/shared/ui/translated-property-pipe';
import { SharedUtilFormService } from '@dv/shared/util/form';
import { SharedUtilPaginatorTranslation } from '@dv/shared/util/paginator-translation';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-administration-ausbildungsstaette',
  standalone: true,
  imports: [
    CommonModule,
    SharedUiLoadingComponent,
    MatFormFieldModule,
    SharedUiFormFieldDirective,
    TranslatePipe,
    MatInputModule,
    MatTableModule,
    MatButtonModule,
    MatSortModule,
    ReactiveFormsModule,
    MatPaginatorModule,
    MatSelectModule,
    TranslatedPropertyPipe,
    TypeSafeMatCellDefDirective,
  ],
  templateUrl:
    './sachbearbeitung-app-feature-administration-ausbildungsstaette.component.html',
  styleUrl:
    './sachbearbeitung-app-feature-administration-ausbildungsstaette.component.scss',
  animations: [detailExpand],
  providers: [
    AdminAusbildungsstaetteStore,
    { provide: MatPaginatorIntl, useClass: SharedUtilPaginatorTranslation },
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureAdministrationAusbildungsstaetteComponent
  implements AfterViewInit
{
  store = inject(AdminAusbildungsstaetteStore);
  fb = inject(FormBuilder);
  dialog = inject(MatDialog);
  destroyRef = inject(DestroyRef);
  formUtils = inject(SharedUtilFormService);
  translate = inject(TranslateService);

  @ViewChild('paginator', { static: false }) paginator!: MatPaginator;
  @ViewChild(MatSort, { static: false }) sort!: MatSort;

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

  constructor() {
    this.formUtils.registerFormForUnsavedCheck(this);
    merge(this.gangForm.valueChanges, this.form.valueChanges)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => {
        this.hasUnsavedChanges = true;
      });

    effect(() => {
      const response = this.store.response();

      if (response.type === 'success') {
        this.endEdit();
      }
    });
    this.store.loadAusbildungsstaetten({});
    this.store.loadBildungskategorien({});
  }

  ngAfterViewInit() {
    this.store.setPaginator(this.paginator);
    this.store.setSort(this.sort);
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
