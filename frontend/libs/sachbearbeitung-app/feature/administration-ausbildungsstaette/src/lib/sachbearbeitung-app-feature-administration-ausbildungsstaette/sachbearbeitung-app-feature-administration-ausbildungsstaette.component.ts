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
  ViewChild,
  inject,
} from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import {
  MatPaginator,
  MatPaginatorModule,
  PageEvent,
} from '@angular/material/paginator';
import { MatSelectModule } from '@angular/material/select';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import { TranslateModule } from '@ngx-translate/core';

import { AdminAusbildungsstaetteStore } from '@dv/sachbearbeitung-app/data-access/ausbildungsstaette';
import { AusbildungsstaetteTableData } from '@dv/sachbearbeitung-app/model/administration';
import {
  Ausbildungsgang,
  Ausbildungsort,
  Bildungsart,
} from '@dv/shared/model/gesuch';
import { SharedUiFormFieldDirective } from '@dv/shared/ui/form';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';

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

  @ViewChild('paginator', { static: false }) paginator!: MatPaginator;
  @ViewChild(MatSort, { static: false }) sort!: MatSort;

  ngAfterViewInit() {
    this.store.setPaginator(this.paginator);
    this.store.setSort(this.sort);
  }

  pageChange(event: PageEvent) {
    console.log(event);
  }

  filterChange(filter: string) {
    this.store.setFilter(filter);
  }

  trackByIndex(index: number) {
    return index;
  }

  constructor() {
    // this.gangForm.valueChanges.pipe(takeUntilDestroyed()).subscribe(() => {
    //   if (this.editedAusbildungsgang) {
    //     this.ausbildungsstaetteHasChanges = true;
    //   }
    // });
    // this.staetteForm.valueChanges.pipe(takeUntilDestroyed()).subscribe(() => {
    //   if (this.editedAusbildungsstaette) {
    //     this.ausbildungsstaetteHasChanges = true;
    //   }
    // });
  }

  // Ausbildungsstaette ==========================================================

  displayedColumns: string[] = [
    'nameDe',
    'nameFr',
    'ausbildungsgaengeCount',
    'actions',
  ];

  expandedRow: string | null = null;

  editedAusbildungsstaette: AusbildungsstaetteTableData | null = null;

  // ausbildungsstaetteHasChanges = false;

  staetteForm = this.fb.group({
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

    if (this.expandedRow === staette.id) {
      this.expandedRow = null;
    } else {
      this.expandedRow = staette.id;
    }
  }

  editAusbildungsstaette(ausbildungsstaette: AusbildungsstaetteTableData) {
    this.editedAusbildungsstaette = ausbildungsstaette;
    this.staetteForm.patchValue(ausbildungsstaette, { emitEvent: false });
  }

  cancelEditAusbildungsstaette() {
    if (this.editedAusbildungsstaette?.id === 'new') {
      this.store.removeNewAusbildungsstaetteRow();
    }

    this.editedAusbildungsstaette = null;
    this.staetteForm.reset();
    // this.ausbildungsstaetteHasChanges = false;
  }

  saveAusbildungsstaette() {
    this.cancelEditAusbildungsstaette();
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

  gangForm = this.fb.group({
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
      ausbildungsort: '' as Ausbildungsort,
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

  cancelEditAusbildungsgang(staette: AusbildungsstaetteTableData) {
    if (this.editedAusbildungsgang?.id === 'new') {
      this.store.removeNewAusbildungsgangRow(staette);
    }

    this.editedAusbildungsgang = null;

    this.gangForm.reset();
    // this.ausbildungsstaetteHasChanges = false;
  }

  saveAusbildungsgang() {
    console.log('saveAusbildungsgang');
  }
}
