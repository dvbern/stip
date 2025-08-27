import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  OnInit,
  computed,
  effect,
  inject,
  input,
  signal,
} from '@angular/core';
import { NonNullableFormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSelectModule } from '@angular/material/select';
import { MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslocoPipe, TranslocoService } from '@jsverse/transloco';

import { SachbearbeitungAppTranslationKey } from '@dv/sachbearbeitung-app/assets/i18n';
import { AdministrationAusbildungsstaetteStore } from '@dv/sachbearbeitung-app/data-access/administration-ausbildungsstaette';
import { StatusFilter } from '@dv/sachbearbeitung-app/model/ausbildungsstaette';
import { AusbildungsstaetteStore } from '@dv/shared/data-access/ausbildungsstaette';
import { StatusType } from '@dv/shared/model/ausbildung';
import {
  Ausbildungskategorie,
  Ausbildungsstaette,
  AusbildungsstaetteSortColumn,
  SortOrder,
} from '@dv/shared/model/gesuch';
import { SortAndPageInputs } from '@dv/shared/model/table';
import {
  assertUnreachable,
  getCorrectPropertyName,
  getCurrentLanguageSig,
  isDefined,
  type,
  uppercased,
} from '@dv/shared/model/type-util';
import { DEFAULT_PAGE_SIZE, PAGE_SIZES } from '@dv/shared/model/ui-constants';
import { SharedUiClearButtonComponent } from '@dv/shared/ui/clear-button';
import { SharedUiConfirmDialogComponent } from '@dv/shared/ui/confirm-dialog';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiPadTextPipeComponent } from '@dv/shared/ui/pad-text-pipe';
import {
  TypeSafeMatCellDefDirective,
  TypeSafeMatRowDefDirective,
} from '@dv/shared/ui/table-helper';
import { TranslatedPropertyPipe } from '@dv/shared/ui/translated-property-pipe';
import { SharedUiTruncateTooltipDirective } from '@dv/shared/ui/truncate-tooltip';
import { paginatorTranslationProvider } from '@dv/shared/util/paginator-translation';
import { isPending } from '@dv/shared/util/remote-data';
import {
  getSortAndPageInputs,
  limitPageToNumberOfEntriesEffect,
  makeEmptyStringPropertiesNull,
  paginateList,
  partiallyDebounceFormValueChangesSig,
  restrictNumberParam,
  sortList,
} from '@dv/shared/util/table';

import { CreateAusbildungsstaetteDialogComponent } from './create-ausbildungsstaette-dialog.component';
import { EditAusbildungsstaetteDialogComponent } from './edit-ausbildungsstaette-dialog.component';

type AusbildungsstaetteFilterFormKeys =
  | 'ausbildungsstaette'
  | 'chShisNummer'
  | 'ctNummer'
  | 'burNummer'
  | 'status';
type DisplayColumns =
  | 'AUSBILDUNGSSTAETTE'
  | 'CH_SHIS_NUMMER'
  | 'CT_NUMMER'
  | 'BUR_NUMMER'
  | 'AKTIV'
  | 'AKTIONEN';

@Component({
  selector: 'dv-ausbildungsstaette',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    TranslocoPipe,
    MatTableModule,
    MatSortModule,
    MatFormFieldModule,
    MatSelectModule,
    MatInputModule,
    MatPaginatorModule,
    MatTooltipModule,
    SharedUiClearButtonComponent,
    SharedUiLoadingComponent,
    SharedUiTruncateTooltipDirective,
    SharedUiPadTextPipeComponent,
    TypeSafeMatCellDefDirective,
    TypeSafeMatRowDefDirective,
    TranslatedPropertyPipe,
  ],
  providers: [paginatorTranslationProvider()],
  templateUrl: './ausbildungsstaette.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AusbildungsstaetteComponent
  implements SortAndPageInputs<DisplayColumns>, OnInit
{
  private formBuilder = inject(NonNullableFormBuilder);
  private ausbildungsstaetteStore = inject(AusbildungsstaetteStore);
  private administrationAusbildungsstaetteStore = inject(
    AdministrationAusbildungsstaetteStore,
  );
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private dialog = inject(MatDialog);
  translate = inject(TranslocoService);

  // Due to lack of space, the following inputs are not suffixed with 'Sig'
  ausbildungsstaette = input<string | undefined>(undefined);
  chShisNummer = input<string | undefined>(undefined);
  ctNummer = input<string | undefined>(undefined);
  burNummer = input<string | undefined>(undefined);
  status = input<StatusType>(undefined);
  sortColumn = input<DisplayColumns | undefined>(undefined);
  sortOrder = input<SortOrder | undefined>(undefined);
  page = input(<number | undefined>undefined, {
    transform: restrictNumberParam({ min: 0, max: 999 }),
  });
  pageSize = input(<number | undefined>undefined, {
    transform: restrictNumberParam({
      min: PAGE_SIZES[0],
      max: PAGE_SIZES[PAGE_SIZES.length - 1],
    }),
  });

  addAusbildungsstaetteIsLoadingSig = computed(() => {
    const abschluesseLoading = isPending(
      this.administrationAusbildungsstaetteStore.abschluesse(),
    );
    const ausbildungsstaettenLoading = isPending(
      this.ausbildungsstaetteStore.ausbildungsstaetten(),
    );
    return abschluesseLoading || ausbildungsstaettenLoading;
  });

  filterForm = this.formBuilder.group({
    ausbildungsstaette: [<string | undefined>undefined],
    chShisNummer: [<string | undefined>undefined],
    ctNummer: [<string | undefined>undefined],
    burNummer: [<string | undefined>undefined],
    status: [type<StatusType>('ACTIVE')],
  } satisfies Record<AusbildungsstaetteFilterFormKeys, unknown>);

  displayedColumns = [
    'AUSBILDUNGSSTAETTE',
    'CH_SHIS_NUMMER',
    'CT_NUMMER',
    'BUR_NUMMER',
    'AKTIV',
    'AKTIONEN',
  ] satisfies DisplayColumns[];
  viewSig = computed(() => {
    const ausbildungsstaetten =
      this.administrationAusbildungsstaetteStore.ausbildungsstaettenViewSig();

    return {
      loading: isPending(
        this.administrationAusbildungsstaetteStore.ausbildungsstaetten(),
      ),
      ausbildungsstaetten: ausbildungsstaetten?.entries ?? [],
      totalEntries: ausbildungsstaetten?.totalEntries ?? 0,
    };
  });
  filterFormChangedSig = partiallyDebounceFormValueChangesSig(this.filterForm, [
    'status',
  ]);
  ausbildungsstaettenDataSourceSig = computed(() => {
    const abschluesse = this.viewSig().ausbildungsstaetten;
    const datasource = new MatTableDataSource(abschluesse);

    return datasource;
  });
  pageSizes = PAGE_SIZES;
  defaultPageSize = DEFAULT_PAGE_SIZE;
  sortList = sortList(this.router, this.route);
  paginateList = paginateList(this.router, this.route);
  currentLangSig = getCurrentLanguageSig(this.translate);
  ausbildungskategorieValues = Object.values(Ausbildungskategorie);
  statusValues = Object.values(StatusFilter);
  totalEntriesSig = computed(() => this.viewSig().totalEntries);

  private reloadAusbildungsstaettenSig = signal<unknown>(null);

  constructor() {
    this.ausbildungsstaetteStore.loadAusbildungsstaetten$();

    limitPageToNumberOfEntriesEffect(
      this,
      this.totalEntriesSig,
      this.router,
      this.route,
    );

    // Handle string filter form control changes
    effect(() => {
      this.filterFormChangedSig();
      const formValue = this.filterForm.getRawValue();

      this.router.navigate(['.'], {
        relativeTo: this.route,
        queryParams: makeEmptyStringPropertiesNull(formValue),
        queryParamsHandling: 'merge',
        replaceUrl: true,
      });
    });

    // when the route param inputs change, load the data
    effect(() => {
      const { sortColumn, sortOrder, page, pageSize } =
        getSortAndPageInputs(this);

      this.reloadAusbildungsstaettenSig();
      const active = this.status();
      this.administrationAusbildungsstaetteStore.loadAusbildungsstaetten$({
        filter: {
          [getCorrectPropertyName('name', this.currentLangSig())]:
            this.ausbildungsstaette(),
          burNo: this.burNummer(),
          chShis: this.chShisNummer(),
          ctNo: this.ctNummer(),

          sortColumn: useCorrectSortColumn(sortColumn, this.currentLangSig()),
          sortOrder,
          page,
          pageSize,
          ...(isDefined(active) ? { aktiv: active === 'ACTIVE' } : {}),
        },
      });
    });
  }

  archive(ausbildungsstaette: Ausbildungsstaette) {
    SharedUiConfirmDialogComponent.open<SachbearbeitungAppTranslationKey>(
      this.dialog,
      {
        title:
          'sachbearbeitung-app.feature.administration.ausbildungsstaette.ausbildungsstaette.archiveDialog.title',
        message:
          'sachbearbeitung-app.feature.administration.ausbildungsstaette.ausbildungsstaette.archiveDialog.message',
        translationObject: ausbildungsstaette,
      },
    )
      .afterClosed()
      .subscribe((confirmed) => {
        if (confirmed) {
          this.administrationAusbildungsstaetteStore.archiveEntity$({
            type: 'ausbildungsstaette',
            id: ausbildungsstaette.id,
            onSuccess: () => {
              this.reloadAusbildungsstaettenSig.set({});
            },
          });
        }
      });
  }

  createAusbildungsstaette() {
    CreateAusbildungsstaetteDialogComponent.open(this.dialog, {
      ausbildungsstaetten:
        this.ausbildungsstaetteStore.ausbildungsstaetteViewSig(),
      abschluesse: this.ausbildungsstaetteStore.abschluesseViewSig(),
    })
      .afterClosed()
      .subscribe((ausbildungsstaetteCreate) => {
        if (ausbildungsstaetteCreate) {
          this.administrationAusbildungsstaetteStore.createAusbildungsstaette$({
            values: { ausbildungsstaetteCreate },
            onSuccess: () => {
              this.reloadAusbildungsstaettenSig.set({});
            },
          });
        }
      });
  }

  editAusbildungsstaette(ausbildungsstaette: Ausbildungsstaette) {
    EditAusbildungsstaetteDialogComponent.open(this.dialog, {
      nameDe: ausbildungsstaette.nameDe,
      nameFr: ausbildungsstaette.nameFr,
    })
      .afterClosed()
      .subscribe((renameAusbildungsstaette) => {
        if (renameAusbildungsstaette) {
          this.administrationAusbildungsstaetteStore.editAusbildungsstaette$({
            values: {
              ausbildungsstaetteId: ausbildungsstaette.id,
              renameAusbildungsstaette,
            },
            onSuccess: () => {
              this.reloadAusbildungsstaettenSig.set({});
            },
          });
        }
      });
  }

  ngOnInit() {
    this.filterForm.reset({
      ausbildungsstaette: this.ausbildungsstaette(),
      chShisNummer: this.chShisNummer(),
      ctNummer: this.ctNummer(),
      burNummer: this.burNummer(),
      status: this.status(),
    });

    // Enable validation from the beginning
    this.filterForm.markAllAsTouched();
  }
}

function useCorrectSortColumn(
  sortColumn: DisplayColumns | undefined,
  currentLang: 'de' | 'fr',
): AusbildungsstaetteSortColumn | undefined {
  switch (sortColumn) {
    case 'AUSBILDUNGSSTAETTE':
      return `NAME_${uppercased(currentLang)}`;
    case 'BUR_NUMMER':
    case 'CH_SHIS_NUMMER':
    case 'CT_NUMMER': {
      const nameMap = {
        BUR_NUMMER: 'BUR_NO',
        CH_SHIS_NUMMER: 'CH_SHIS',
        CT_NUMMER: 'CT_NO',
      } satisfies Partial<Record<DisplayColumns, AusbildungsstaetteSortColumn>>;
      return nameMap[sortColumn];
    }
    case 'AKTIV':
      return sortColumn;
    case 'AKTIONEN':
    case undefined:
      return undefined;
    default:
      assertUnreachable(sortColumn);
  }
}
