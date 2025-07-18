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
import { TranslatePipe, TranslateService } from '@ngx-translate/core';

import { SachbearbeitungAppTranslationKey } from '@dv/sachbearbeitung-app/assets/i18n';
import { AdministrationAusbildungsstaetteStore } from '@dv/sachbearbeitung-app/data-access/administration-ausbildungsstaette';
import { StatusFilter } from '@dv/sachbearbeitung-app/model/ausbildungsstaette';
import { StatusType } from '@dv/shared/model/ausbildung';
import {
  Abschluss,
  AbschlussSortColumn,
  Ausbildungskategorie,
  Bildungsrichtung,
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
  isDefined,
  type,
  uppercased,
} from '@dv/shared/model/type-util';
import { DEFAULT_PAGE_SIZE, PAGE_SIZES } from '@dv/shared/model/ui-constants';
import { SharedUiClearButtonComponent } from '@dv/shared/ui/clear-button';
import { SharedUiConfirmDialogComponent } from '@dv/shared/ui/confirm-dialog';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import {
  TypeSafeMatCellDefDirective,
  TypeSafeMatRowDefDirective,
} from '@dv/shared/ui/table-helper';
import { TranslatedPropertyPipe } from '@dv/shared/ui/translated-property-pipe';
import { paginatorTranslationProvider } from '@dv/shared/util/paginator-translation';
import { isPending } from '@dv/shared/util/remote-data';

import { CreateAbschlussDialogComponent } from './create-abschluss-dialog.component';
import { DataInfoDialogComponent } from '../data-info-dialog/data-info-dialog.component';

type AbschlussFilterFormKeys =
  | 'abschluss'
  | 'ausbildungskategorie'
  | 'bildungsrichtung'
  | 'status';
type DisplayColumns =
  | 'ABSCHLUSS'
  | 'AUSBILDUNGSKATEGORIE'
  | 'BILDUNGSRICHTUNG'
  | 'AKTIV'
  | 'AKTIONEN';

@Component({
  selector: 'dv-abschluss',
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
    MatTooltipModule,
    SharedUiClearButtonComponent,
    SharedUiLoadingComponent,
    TypeSafeMatCellDefDirective,
    TypeSafeMatRowDefDirective,
    TranslatedPropertyPipe,
  ],
  providers: [paginatorTranslationProvider()],
  templateUrl: './abschluss.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AbschlussComponent
  implements SortAndPageInputs<DisplayColumns>, OnInit
{
  private formBuilder = inject(NonNullableFormBuilder);
  private administrationAusbildungsstaetteStore = inject(
    AdministrationAusbildungsstaetteStore,
  );
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private dialog = inject(MatDialog);
  translate = inject(TranslateService);

  // Due to lack of space, the following inputs are not suffixed with 'Sig'
  abschluss = input<string | undefined>(undefined);
  ausbildungskategorie = input<Ausbildungskategorie | undefined>(undefined);
  bildungsrichtung = input<Bildungsrichtung | undefined>(undefined);
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

  filterForm = this.formBuilder.group({
    abschluss: [<string | undefined>undefined],
    ausbildungskategorie: [<string | undefined>undefined],
    bildungsrichtung: [<string | undefined>undefined],
    status: [type<StatusType>('ACTIVE')],
  } satisfies Record<AbschlussFilterFormKeys, unknown>);
  displayedColumns = [
    'ABSCHLUSS',
    'AUSBILDUNGSKATEGORIE',
    'BILDUNGSRICHTUNG',
    'AKTIV',
    'AKTIONEN',
  ] satisfies DisplayColumns[];
  viewSig = computed(() => {
    const abschluesse =
      this.administrationAusbildungsstaetteStore.abschluesseViewSig();

    return {
      loading: isPending(
        this.administrationAusbildungsstaetteStore.abschluesse(),
      ),
      abschluesse: abschluesse?.entries ?? [],
      totalEntries: abschluesse?.totalEntries ?? 0,
    };
  });
  filterFormChangedSig = partiallyDebounceFormValueChangesSig(this.filterForm, [
    'ausbildungskategorie',
    'bildungsrichtung',
    'status',
  ]);
  abschluesseDataSourceSig = computed(() => {
    const abschluesse = this.viewSig().abschluesse;
    const datasource = new MatTableDataSource(abschluesse);

    return datasource;
  });
  pageSizes = PAGE_SIZES;
  defaultPageSize = DEFAULT_PAGE_SIZE;
  sortList = sortList(this.router, this.route);
  paginateList = paginateList(this.router, this.route);
  currentLangSig = getCurrentLanguageSig(this.translate);
  ausbildungskategorieValues = Object.values(Ausbildungskategorie);
  bildungsrichtungValues = Object.values(Bildungsrichtung);
  statusValues = Object.values(StatusFilter);
  totalEntriesSig = computed(() => this.viewSig().totalEntries);

  private reloadAbschluesseSig = signal<unknown | null>(null);

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

      this.reloadAbschluesseSig();
      const active = this.status();
      this.administrationAusbildungsstaetteStore.loadAbschluesse$({
        filter: {
          [getCorrectPropertyName('bezeichnung', this.currentLangSig())]:
            this.abschluss(),
          ausbildungskategorie: this.ausbildungskategorie(),
          bildungsrichtung: this.bildungsrichtung(),
          sortColumn: useCorrectSortColumn(sortColumn, this.currentLangSig()),
          sortOrder,
          page,
          pageSize,
          ...(isDefined(active) ? { aktiv: active !== 'INACTIVE' } : {}),
        },
      });
    });
  }

  archive(abschluss: Abschluss) {
    SharedUiConfirmDialogComponent.open<SachbearbeitungAppTranslationKey>(
      this.dialog,
      {
        title:
          'sachbearbeitung-app.feature.administration.ausbildungsstaette.abschluss.archiveDialog.title',
        message:
          'sachbearbeitung-app.feature.administration.ausbildungsstaette.abschluss.archiveDialog.message',
        translationObject: abschluss,
      },
    )
      .afterClosed()
      .subscribe((confirmed) => {
        if (confirmed) {
          this.administrationAusbildungsstaetteStore.archiveEntity$({
            type: 'abschluss',
            id: abschluss.id,
            onSuccess: () => {
              this.reloadAbschluesseSig.set({});
            },
          });
        }
      });
  }

  createAbschluss() {
    CreateAbschlussDialogComponent.open(this.dialog)
      .afterClosed()
      .subscribe((brueckenangebotCreate) => {
        if (brueckenangebotCreate) {
          this.administrationAusbildungsstaetteStore.createAbschluss$({
            values: { brueckenangebotCreate },
            onSuccess: () => {
              this.reloadAbschluesseSig.set({});
            },
          });
        }
      });
  }

  showInfo(abschluss: Abschluss) {
    DataInfoDialogComponent.open(
      this.dialog,
      'sachbearbeitung-app.feature.administration.ausbildungsstaette.abschluss.infoDialog.title',
      abschluss,
      ({ info, translatedInfo, spacer }) => [
        info(
          'sachbearbeitung-app.feature.administration.ausbildungsstaette.bezeichnungDe',
          'bezeichnungDe',
        ),
        info(
          'sachbearbeitung-app.feature.administration.ausbildungsstaette.bezeichnungFr',
          'bezeichnungFr',
        ),
        spacer(),
        translatedInfo(
          'sachbearbeitung-app.feature.administration.ausbildungsstaette.ausbildungskategorie',
          `sachbearbeitung-app.feature.administration.ausbildungsstaette.ausbildungskategorie.${abschluss.ausbildungskategorie}`,
        ),
        translatedInfo(
          'sachbearbeitung-app.feature.administration.ausbildungsstaette.bildungskategorie',
          `sachbearbeitung-app.feature.administration.ausbildungsstaette.bildungskategorie.${abschluss.bildungskategorie}`,
        ),
        translatedInfo(
          'sachbearbeitung-app.feature.administration.ausbildungsstaette.bildungsrichtung',
          `sachbearbeitung-app.feature.administration.ausbildungsstaette.bildungsrichtung.${abschluss.bildungsrichtung}`,
        ),
        info(
          'sachbearbeitung-app.feature.administration.ausbildungsstaette.bfsKategorie',
          'bfsKategorie',
        ),
        translatedInfo(
          'sachbearbeitung-app.feature.administration.ausbildungsstaette.berufsbefaehigenderAbschluss',
          `sachbearbeitung-app.feature.administration.ausbildungsstaette.boolean.${abschluss.berufsbefaehigenderAbschluss}`,
        ),
        translatedInfo(
          'sachbearbeitung-app.feature.administration.ausbildungsstaette.ferien',
          `sachbearbeitung-app.feature.administration.ausbildungsstaette.ferien.${abschluss.ferien}`,
        ),
        ...(abschluss.zusatzfrage
          ? [
              translatedInfo(
                'sachbearbeitung-app.feature.administration.ausbildungsstaette.zusatzfrage',
                `sachbearbeitung-app.feature.administration.ausbildungsstaette.zusatzfrage.${abschluss.zusatzfrage}`,
              ),
            ]
          : []),
        translatedInfo(
          'sachbearbeitung-app.feature.administration.ausbildungsstaette.status',
          `sachbearbeitung-app.feature.administration.ausbildungsstaette.status.${abschluss.aktiv}`,
        ),
      ],
    );
  }

  ngOnInit() {
    this.filterForm.reset({
      abschluss: this.abschluss(),
      ausbildungskategorie: this.ausbildungskategorie(),
      bildungsrichtung: this.bildungsrichtung(),
      status: this.status(),
    });

    // Enable validation from the beginning
    this.filterForm.markAllAsTouched();
  }
}

function useCorrectSortColumn(
  sortColumn: DisplayColumns | undefined,
  currentLang: 'de' | 'fr',
): AbschlussSortColumn | undefined {
  switch (sortColumn) {
    case 'ABSCHLUSS':
      return `BEZEICHNUNG_${uppercased(currentLang)}`;
    case 'AUSBILDUNGSKATEGORIE':
    case 'BILDUNGSRICHTUNG':
    case 'AKTIV':
      return sortColumn;
    case 'AKTIONEN':
    case undefined:
      return undefined;
    default:
      assertUnreachable(sortColumn);
  }
}
