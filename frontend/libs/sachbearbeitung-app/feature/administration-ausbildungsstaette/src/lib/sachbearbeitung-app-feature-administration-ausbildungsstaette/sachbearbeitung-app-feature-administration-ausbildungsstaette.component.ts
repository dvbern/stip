import {
  animate,
  state,
  style,
  transition,
  trigger,
} from '@angular/animations';
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
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSelectModule } from '@angular/material/select';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import { TranslateModule } from '@ngx-translate/core';
import { merge } from 'rxjs';

import { AdminAusbildungsstaetteStore } from '@dv/sachbearbeitung-app/data-access/ausbildungsstaette';
import { AusbildungsstaetteTableData } from '@dv/sachbearbeitung-app/model/administration';
import { Ausbildungsgang, Bildungsart } from '@dv/shared/model/gesuch';
import {
  ConfirmDialogData,
  SharedUiConfirmDialogComponent,
} from '@dv/shared/ui/confirm-dialog';
import { SharedUiFormFieldDirective } from '@dv/shared/ui/form';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUtilFormService } from '@dv/shared/util/form';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-administration-ausbildungsstaette',
  standalone: true,
  imports: [
    CommonModule,
    SharedUiLoadingComponent,
    MatFormFieldModule,
    SharedUiFormFieldDirective,
    TranslateModule,
    MatInputModule,
    MatTableModule,
    MatButtonModule,
    MatSortModule,
    ReactiveFormsModule,
    MatPaginatorModule,
    MatSelectModule,
  ],
  templateUrl:
    './sachbearbeitung-app-feature-administration-ausbildungsstaette.component.html',
  styleUrl:
    './sachbearbeitung-app-feature-administration-ausbildungsstaette.component.scss',
  animations: [
    trigger('detailExpand', [
      state('collapsed,void', style({ height: '0px', minHeight: '0' })),
      state('expanded', style({ height: '*' })),
      transition(
        'expanded <=> collapsed',
        animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)'),
      ),
    ]),
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

  @ViewChild('paginator', { static: false }) paginator!: MatPaginator;
  @ViewChild(MatSort, { static: false }) sort!: MatSort;

  ngAfterViewInit() {
    this.store.setPaginator(this.paginator);
    this.store.setSort(this.sort);
  }

  trackByIndex(index: number) {
    return index;
  }

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
        // is this going to be reactive, since it is not a subscription and does not come from an input, observable or the template?
        this.endEdit();
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

  // Ausbildungsstaette ==========================================================

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
    nameDe: [''],
    nameFr: [''],
  });

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

    // this.endEdit();

    this.store.handleCreateUpdateAusbildungsstaette(update);
  }

  deleteAusbildungsstaette(staette: AusbildungsstaetteTableData) {
    const dialogRef = this.dialog.open<unknown, ConfirmDialogData, boolean>(
      SharedUiConfirmDialogComponent,
      {
        data: {
          title: 'sachbearbeitung-app.admin.ausbildungsstaette.deleteTitle',
          message: 'sachbearbeitung-app.admin.ausbildungsstaette.deleteMessage',
          confirmText: 'shared.ui.yes',
          cancelText: 'shared.ui.no',
          translationObject: staette,
        },
      },
    );

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

  bildungsartValues = Object.values(Bildungsart);

  displayedChildColumns: string[] = [
    'bezeichnungDe',
    'bezeichnungFr',
    'ausbildungsrichtung',
    'ausbildungsort',
    'actions',
  ];

  editedAusbildungsgang: Ausbildungsgang | null = null;

  gangForm = this.fb.nonNullable.group({
    bezeichnungDe: [''],
    bezeichnungFr: [''],
    ausbildungsrichtung: [''],
    ausbildungsort: [''],
  });

  addAusbildungsgang(staette: AusbildungsstaetteTableData) {
    const newRow = {
      id: 'new',
      bezeichnungDe: '',
      bezeichnungFr: '',
      ausbildungsrichtung: Bildungsart.LEHREN_ANLEHREN,
      ausbildungsort: '',
    };

    this.editedAusbildungsgang = newRow;

    if (!staette.id) {
      return;
    }

    this.store.addAusbildungsgangRow(staette.id, newRow);
  }

  editAusbildungsgang(ausbildungsgang: Ausbildungsgang) {
    this.editedAusbildungsgang = ausbildungsgang;
    this.gangForm.patchValue(ausbildungsgang, { emitEvent: false });
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

    const update = {
      ...this.editedAusbildungsgang,
      ...this.gangForm.value,
      ausbildungsrichtung: this.gangForm.value
        .ausbildungsrichtung as Bildungsart,
    };

    this.endEdit();

    this.store.handleCreateUpdateAusbildungsgang(staette, update);
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
          this.store.deleteAusbildungsgang(staette, gang);
        }
      });
  }
}
