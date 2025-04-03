import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  DestroyRef,
  computed,
  effect,
  inject,
  viewChild,
} from '@angular/core';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { NonNullableFormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatFormField } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { RouterLink } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';
import { debounceTime, map } from 'rxjs';

import { BenutzerverwaltungStore } from '@dv/sachbearbeitung-app/data-access/benutzerverwaltung';
import { SharedModelBenutzer } from '@dv/shared/model/benutzer';
import { SharedUiBadgeComponent } from '@dv/shared/ui/badge';
import { SharedUiClearButtonComponent } from '@dv/shared/ui/clear-button';
import { SharedUiConfirmDialogComponent } from '@dv/shared/ui/confirm-dialog';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiMaxLengthDirective } from '@dv/shared/ui/max-length';
import {
  SharedUiRdIsPendingPipe,
  SharedUiRdIsPendingWithoutCachePipe,
} from '@dv/shared/ui/remote-data-pipe';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';
import { SharedUiTruncateTooltipDirective } from '@dv/shared/ui/truncate-tooltip';
import { paginatorTranslationProvider } from '@dv/shared/util/paginator-translation';

const INPUT_DELAY = 600;

@Component({
  standalone: true,
  imports: [
    CommonModule,
    TranslatePipe,
    MatTableModule,
    MatSortModule,
    MatPaginatorModule,
    MatTooltipModule,
    RouterLink,
    TypeSafeMatCellDefDirective,
    SharedUiBadgeComponent,
    SharedUiRdIsPendingPipe,
    SharedUiRdIsPendingWithoutCachePipe,
    SharedUiTruncateTooltipDirective,
    SharedUiLoadingComponent,
    MatFormField,
    MatInput,
    ReactiveFormsModule,
    SharedUiClearButtonComponent,
    SharedUiMaxLengthDirective,
  ],
  templateUrl: './benutzer-overview.component.html',
  styleUrls: ['./benutzer-overview.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [paginatorTranslationProvider()],
})
export class BenutzerOverviewComponent {
  private dialog = inject(MatDialog);
  private formBuilder = inject(NonNullableFormBuilder);
  store = inject(BenutzerverwaltungStore);
  destroyRef = inject(DestroyRef);

  displayedColumns = ['name', 'email', 'roles', 'actions'];
  showFullListForBenutzer: Record<string, boolean> = {};

  sortSig = viewChild(MatSort);
  paginatorSig = viewChild(MatPaginator);

  filterForm = this.formBuilder.group({
    name: [<string | null>null],
    roles: [<string | null>null],
  });

  private filterFormChangedSig = toSignal(
    this.filterForm.valueChanges.pipe(
      debounceTime(INPUT_DELAY),
      map(() => this.filterForm.getRawValue()),
    ),
  );
  benutzerListDataSourceSig = computed(() => {
    const benutzers = this.store.benutzers();
    console.log('Benutzerdaten aus API', benutzers);
    const datasource = new MatTableDataSource(benutzers.data);
    const sort = this.sortSig();
    const paginator = this.paginatorSig();

    datasource.filterPredicate = (data, filter) => {
      const { name, roles } = JSON.parse(filter);
      if (name && !data.name.toLowerCase().includes(name.toLowerCase())) {
        return false;
      }
      if (roles) {
        const roleNames = [...data.roles.compact, ...data.roles.full]
          .map((role) => role.name.toLowerCase())
          .join(',');
        console.log(roleNames, 'Roles');
        if (!roleNames.includes(roles.toLowerCase())) {
          return false;
        }
      }
      return true;
    };

    if (sort) {
      datasource.sort = sort;
    }
    if (paginator) {
      datasource.paginator = paginator;
    }
    return datasource;
  });

  constructor() {
    this.store.loadAllSbAppBenutzers$();
    effect(
      () => {
        const filterValues = this.filterFormChangedSig();
        this.benutzerListDataSourceSig().filter = JSON.stringify(filterValues);
      },
      { allowSignalWrites: true },
    );
  }

  expandRolesForBenutzer(benutzerId: string) {
    this.showFullListForBenutzer[benutzerId] = true;
  }

  deleteBenutzer(benutzer: SharedModelBenutzer) {
    SharedUiConfirmDialogComponent.open(this.dialog, {
      title:
        'sachbearbeitung-app.admin.benutzerverwaltung.confirmDelete.benutzer.title',
      message:
        'sachbearbeitung-app.admin.benutzerverwaltung.confirmDelete.benutzer.text',
      translationObject: benutzer,
    })
      .afterClosed()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((confirmed) => {
        if (confirmed) {
          this.store.deleteBenutzer$({
            benutzerId: benutzer.id,
          });
        }
      });
  }
}
