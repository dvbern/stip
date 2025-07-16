import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  OnInit,
  computed,
  effect,
  inject,
  input,
} from '@angular/core';
import { NonNullableFormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSelectModule } from '@angular/material/select';
import { MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { ActivatedRoute, Router } from '@angular/router';
import {
  TranslatePipe,
  TranslateService,
  isDefined,
} from '@ngx-translate/core';

import { AusbildungsstaetteStore } from '@dv/sachbearbeitung-app/data-access/ausbildungsstaette';
import { StatusFilter } from '@dv/sachbearbeitung-app/model/ausbildungsstaette';
import {
  Ausbildungsgang,
  AusbildungsgangSortColumn,
  Ausbildungskategorie,
  SortOrder,
} from '@dv/shared/model/gesuch';
import {
  SortAndPageInputs,
  getSortAndPageInputs,
  limitPageToNumberOfEntriesEffect,
  makeEmptyStringPropertiesNull,
  paginateList,
  partiallyDebounceFormValueChangesSig,
  restrictNumberParam,
  sortList,
} from '@dv/shared/model/table';
import {
  assertUnreachable,
  getCorrectPropertyName,
  getCurrentLanguageSig,
  uppercased,
} from '@dv/shared/model/type-util';
import { DEFAULT_PAGE_SIZE, PAGE_SIZES } from '@dv/shared/model/ui-constants';
import { SharedUiClearButtonComponent } from '@dv/shared/ui/clear-button';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import {
  TypeSafeMatCellDefDirective,
  TypeSafeMatRowDefDirective,
} from '@dv/shared/ui/table-helper';
import { TranslatedPropertyPipe } from '@dv/shared/ui/translated-property-pipe';
import { paginatorTranslationProvider } from '@dv/shared/util/paginator-translation';
import { isPending } from '@dv/shared/util/remote-data';

import { DataInfoDialogComponent } from '../data-info-dialog/data-info-dialog.component';

type AusbildungsgangFilterFormKeys =
  | 'ausbildungsgang'
  | 'ausbildungsstaette'
  | 'abschluss'
  | 'ausbildungskategorie'
  | 'status';
type DisplayColumns =
  | 'ABSCHLUSS'
  | 'AUSBILDUNGSSTAETTE'
  | 'ABSCHLUSS_AUSBILDUNGSKATEGORIE'
  | 'AKTIV'
  | 'AKTIONEN';

@Component({
  selector: 'dv-ausbildungsgang',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    TranslatePipe,
    MatTableModule,
    MatSortModule,
    MatFormFieldModule,
    MatSelectModule,
    MatInputModule,
    MatPaginatorModule,
    SharedUiClearButtonComponent,
    SharedUiLoadingComponent,
    TypeSafeMatCellDefDirective,
    TypeSafeMatRowDefDirective,
    TranslatedPropertyPipe,
  ],
  providers: [paginatorTranslationProvider()],
  templateUrl: './ausbildungsgang.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AusbildungsgangComponent
  implements SortAndPageInputs<DisplayColumns>, OnInit
{
  private formBuilder = inject(NonNullableFormBuilder);
  private ausbildungsstaetteStore = inject(AusbildungsstaetteStore);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private dialog = inject(MatDialog);
  translate = inject(TranslateService);

  // Due to lack of space, the following inputs are not suffixed with 'Sig'
  ausbildungsgang = input<string | undefined>(undefined);
  ausbildungsstaette = input<string | undefined>(undefined);
  abschluss = input<string | undefined>(undefined);
  ausbildungskategorie = input<Ausbildungskategorie | undefined>(undefined);
  status = input<'ACTIVE' | 'INACTIVE' | undefined>(undefined);
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

  filterForm = this.formBuilder.group({
    ausbildungsgang: [<string | undefined>undefined],
    ausbildungsstaette: [<string | undefined>undefined],
    ausbildungskategorie: [<string | undefined>undefined],
    abschluss: [<string | undefined>undefined],
    status: [<string | undefined>undefined],
  } satisfies Record<AusbildungsgangFilterFormKeys, unknown>);

  displayedColumns = [
    'ABSCHLUSS',
    'AUSBILDUNGSSTAETTE',
    'ABSCHLUSS_AUSBILDUNGSKATEGORIE',
    'AKTIV',
    'AKTIONEN',
  ] satisfies DisplayColumns[];
  viewSig = computed(() => {
    const ausbildungsgaenge =
      this.ausbildungsstaetteStore.ausbildungsgaengeViewSig();

    return {
      loading: isPending(this.ausbildungsstaetteStore.ausbildungsgaenge()),
      ausbildungsgaenge: ausbildungsgaenge?.entries ?? [],
      totalEntries: ausbildungsgaenge?.totalEntries ?? 0,
    };
  });
  filterFormChangedSig = partiallyDebounceFormValueChangesSig(this.filterForm, [
    'ausbildungskategorie',
    'status',
  ]);
  ausbildungsgaengeDataSourceSig = computed(() => {
    const abschluesse = this.viewSig().ausbildungsgaenge;
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

  constructor() {
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

      const active = this.status();
      this.ausbildungsstaetteStore.loadAusbildungsgaenge$({
        filter: {
          [getCorrectPropertyName(
            'abschlussBezeichnung',
            this.currentLangSig(),
          )]: this.abschluss(),
          [getCorrectPropertyName(
            'ausbildungsstaetteName',
            this.currentLangSig(),
          )]: this.ausbildungsstaette(),
          ausbildungskategorie: this.ausbildungskategorie(),
          sortColumn: useCorrectSortColumn(sortColumn, this.currentLangSig()),
          sortOrder,
          page,
          pageSize,
          ...(isDefined(active) ? { aktiv: active === 'ACTIVE' } : {}),
        },
      });
    });
  }

  showInfo(ausbildungsgang: Ausbildungsgang) {
    DataInfoDialogComponent.open(
      this.dialog,
      'sachbearbeitung-app.feature.administration.ausbildungsstaette.abschluss.infoDialog.title',
      ausbildungsgang,
      ({ info, translatedInfo }) => [
        info(
          'sachbearbeitung-app.feature.administration.ausbildungsstaette.infoDialog.bezeichnungDe',
          'bezeichnungDe',
        ),
        info(
          'sachbearbeitung-app.feature.administration.ausbildungsstaette.infoDialog.bezeichnungFr',
          'bezeichnungFr',
        ),
        ...(ausbildungsgang.ausbildungskategorie
          ? [
              translatedInfo(
                'sachbearbeitung-app.feature.administration.ausbildungsstaette.infoDialog.ausbildungskategorie',
                `sachbearbeitung-app.feature.administration.ausbildungsstaette.infoDialog.ausbildungskategorie.${ausbildungsgang.ausbildungskategorie}`,
              ),
            ]
          : []),
        ...(ausbildungsgang.bildungsrichtung
          ? [
              translatedInfo(
                'sachbearbeitung-app.feature.administration.ausbildungsstaette.infoDialog.bildungsrichtung',
                `sachbearbeitung-app.feature.administration.ausbildungsstaette.infoDialog.bildungsrichtung.${ausbildungsgang.bildungsrichtung}`,
              ),
            ]
          : []),
        info(
          'sachbearbeitung-app.feature.administration.ausbildungsstaette.infoDialog.abschlussBezeichnungDe',
          'abschlussBezeichnungDe',
        ),
        info(
          'sachbearbeitung-app.feature.administration.ausbildungsstaette.infoDialog.abschlussBezeichnungFr',
          'abschlussBezeichnungFr',
        ),
        translatedInfo(
          'sachbearbeitung-app.feature.administration.ausbildungsstaette.infoDialog.status',
          `sachbearbeitung-app.feature.administration.ausbildungsstaette.infoDialog.status.${ausbildungsgang.aktiv}`,
        ),
      ],
    );
  }

  ngOnInit() {
    this.filterForm.reset({
      ausbildungsgang: this.ausbildungsgang(),
      ausbildungsstaette: this.ausbildungsstaette(),
      abschluss: this.abschluss(),
      ausbildungskategorie: this.ausbildungskategorie(),
      status: this.status(),
    });

    // Enable validation from the beginning
    this.filterForm.markAllAsTouched();
  }
}

function useCorrectSortColumn(
  sortColumn: DisplayColumns | undefined,
  currentLang: 'de' | 'fr',
): AusbildungsgangSortColumn | undefined {
  switch (sortColumn) {
    case 'ABSCHLUSS':
      return `ABSCHLUSS_BEZEICHNUNG_${uppercased(currentLang)}`;
    case 'AUSBILDUNGSSTAETTE':
      return `AUSBILDUNGSSTAETTE_NAME_${uppercased(currentLang)}`;
    case 'ABSCHLUSS_AUSBILDUNGSKATEGORIE':
    case 'AKTIV':
      return sortColumn;
    case 'AKTIONEN':
    case undefined:
      return undefined;
    default:
      assertUnreachable(sortColumn);
  }
}
