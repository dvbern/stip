import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatOption } from '@angular/material/autocomplete';
import { MatFormField } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { MatSelect } from '@angular/material/select';
import {
  MatCell,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderRow,
  MatHeaderRowDef,
  MatRow,
  MatRowDef,
  MatTable,
} from '@angular/material/table';
import { TranslateModule } from '@ngx-translate/core';

import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';

export interface PeriodicElement {
  status: string;
  datum: number;
  user: number;
  kommentar: string;
}

const ELEMENT_DATA: PeriodicElement[] = [
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
];

@Component({
  selector: 'lib-shared-feature-gesuch-form-protokoll',
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
    MatTable,
    FormsModule,
    MatCell,
    MatColumnDef,
    MatFormField,
    MatHeaderCell,
    MatHeaderRow,
    MatHeaderRowDef,
    MatInput,
    MatOption,
    MatRow,
    MatRowDef,
    MatSelect,
    ReactiveFormsModule,
    TypeSafeMatCellDefDirective,
  ],
  templateUrl: './shared-feature-gesuch-form-protokoll.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SharedFeatureGesuchFormProtokollComponent {
  displayedColumns = ['datum', 'status', 'user', 'kommentar'];
  dataSource = ELEMENT_DATA;
}
