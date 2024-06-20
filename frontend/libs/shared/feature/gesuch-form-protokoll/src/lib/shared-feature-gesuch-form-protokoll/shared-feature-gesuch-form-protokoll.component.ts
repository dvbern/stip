import { LiveAnnouncer } from '@angular/cdk/a11y';
import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  ViewChild,
  computed,
  viewChild,
} from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { TranslateModule } from '@ngx-translate/core';

import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';

@Component({
  selector: 'lib-shared-feature-gesuch-form-protokoll',
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
    MatTableModule,
    FormsModule,
    ReactiveFormsModule,
    TypeSafeMatCellDefDirective,
    MatSortModule,
  ],
  templateUrl: './shared-feature-gesuch-form-protokoll.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchFormProtokollComponent {
  displayedColumns = ['datum', 'status', 'user', 'kommentar'];
  statusprotokoll = new MatTableDataSource([
    { datum: 1, status: 'Hydrogen', user: 1.0079, kommentar: 'H' },
    { datum: 2, status: 'Helium', user: 4.0026, kommentar: 'He' },
    { datum: 3, status: 'Lithium', user: 6.941, kommentar: 'Li' },
    { datum: 4, status: 'Beryllium', user: 9.0122, kommentar: 'Be' },
    { datum: 5, status: 'Boron', user: 10.811, kommentar: 'B' },
    { datum: 6, status: 'Carbon', user: 12.0107, kommentar: 'C' },
    { datum: 7, status: 'Nitrogen', user: 14.0067, kommentar: 'N' },
    { datum: 8, status: 'Oxygen', user: 15.9994, kommentar: 'O' },
    { datum: 9, status: 'Fluorine', user: 18.9984, kommentar: 'F' },
    { datum: 10, status: 'Neon', user: 20.1797, kommentar: 'Ne' },
  ]);

  statusprotokollSig = computed(() => {
    const datasource = new MatTableDataSource([
      { datum: 1, status: 'Hydrogen', user: 1.0079, kommentar: 'H' },
      { datum: 2, status: 'Helium', user: 4.0026, kommentar: 'He' },
      { datum: 3, status: 'Lithium', user: 6.941, kommentar: 'Li' },
      { datum: 4, status: 'Beryllium', user: 9.0122, kommentar: 'Be' },
      { datum: 5, status: 'Boron', user: 10.811, kommentar: 'B' },
      { datum: 6, status: 'Carbon', user: 12.0107, kommentar: 'C' },
      { datum: 7, status: 'Nitrogen', user: 14.0067, kommentar: 'N' },
      { datum: 8, status: 'Oxygen', user: 15.9994, kommentar: 'O' },
      { datum: 9, status: 'Fluorine', user: 18.9984, kommentar: 'F' },
      { datum: 10, status: 'Neon', user: 20.1797, kommentar: 'Ne' },
    ]);
    datasource.sort = this.sortSig() ?? null;
    return datasource;
  });

  constructor(private _liveAnnouncer: LiveAnnouncer) {}

  @ViewChild(MatSort) sort2: MatSort | undefined;
  sortSig = viewChild(MatSort);
}
