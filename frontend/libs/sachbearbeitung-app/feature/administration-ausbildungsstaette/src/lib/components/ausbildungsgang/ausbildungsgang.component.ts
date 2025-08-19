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
import {
  TranslatePipe,
  TranslateService,
  isDefined,
} from '@ngx-translate/core';

import { SachbearbeitungAppTranslationKey } from '@dv/sachbearbeitung-app/assets/i18n';
import { AdministrationAusbildungsstaetteStore } from '@dv/sachbearbeitung-app/data-access/administration-ausbildungsstaette';
import { StatusFilter } from '@dv/sachbearbeitung-app/model/ausbildungsstaette';
import { AusbildungsstaetteStore } from '@dv/shared/data-access/ausbildungsstaette';
import { StatusType } from '@dv/shared/model/ausbildung';
import {
  Ausbildungsgang,
  AusbildungsgangSortColumn,
  Ausbildungskategorie,
  SortOrder,
} from '@dv/shared/model/gesuch';
import { SortAndPageInputs } from '@dv/shared/model/table';
import {
  assertUnreachable,
  capitalized,
  getCorrectPropertyName,
  getCurrentLanguageSig,
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
  sortListByText,
} from '@dv/shared/util/table';

import { CreateAusbildungsgangDialogComponent } from './create-ausbildungsgang-dialog.component';
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
    MatTooltipModule,
    SharedUiClearButtonComponent,
    SharedUiLoadingComponent,
    SharedUiTruncateTooltipDirective,
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
  private administrationAusbildungsstaetteStore = inject(
    AdministrationAusbildungsstaetteStore,
  );
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private dialog = inject(MatDialog);
  translate = inject(TranslateService);

  // Due to lack of space, the following inputs are not suffixed with 'Sig'
  ausbildungsgang = input<string | undefined>(undefined);
  ausbildungsstaette = input<string | undefined>(undefined);
  abschluss = input<string | undefined>(undefined);
  ausbildungskategorie = input<Ausbildungskategorie | undefined>(undefined);
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

  addAusbildungsgangIsLoadingSig = computed(() => {
    const abschluesseLoading = isPending(
      this.administrationAusbildungsstaetteStore.abschluesse(),
    );
    const ausbildungsstaettenLoading = isPending(
      this.ausbildungsstaetteStore.ausbildungsstaetten(),
    );
    return abschluesseLoading || ausbildungsstaettenLoading;
  });

  filterForm = this.formBuilder.group({
    ausbildungsgang: [<string | undefined>undefined],
    ausbildungsstaette: [<string | undefined>undefined],
    ausbildungskategorie: [<string | undefined>undefined],
    abschluss: [<string | undefined>undefined],
    status: [type<StatusType>('ACTIVE')],
  } satisfies Record<AusbildungsgangFilterFormKeys, unknown>);

  displayedColumns = [
    'AUSBILDUNGSSTAETTE',
    'ABSCHLUSS_AUSBILDUNGSKATEGORIE',
    'ABSCHLUSS',
    'AKTIV',
    'AKTIONEN',
  ] satisfies DisplayColumns[];
  viewSig = computed(() => {
    const ausbildungsgaenge =
      this.administrationAusbildungsstaetteStore.ausbildungsgaengeViewSig();

    return {
      loading: isPending(
        this.administrationAusbildungsstaetteStore.ausbildungsgaenge(),
      ),
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

  private reloadAbschluesseSig = signal<unknown>(null);

  constructor() {
    this.ausbildungsstaetteStore.loadAusbildungsstaetten$();
    this.ausbildungsstaetteStore.loadAbschluesse$();
    this.administrationAusbildungsstaetteStore.loadAllAusbildungsgaenge$();

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
      this.administrationAusbildungsstaetteStore.loadAllAusbildungsgaenge$();
      this.administrationAusbildungsstaetteStore.loadAusbildungsgaenge$({
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

  archive(ausbildungsgang: Ausbildungsgang) {
    const getBezeichnung = (
      lang: 'De' | 'Fr',
      ausbildungsgang: Ausbildungsgang,
    ) => {
      const name = ausbildungsgang?.ausbildungsstaette?.[`name${lang}`];
      const bezeichnung = ausbildungsgang?.abschluss?.[`bezeichnung${lang}`];
      return `${name} - ${bezeichnung}`;
    };
    SharedUiConfirmDialogComponent.open<SachbearbeitungAppTranslationKey>(
      this.dialog,
      {
        title:
          'sachbearbeitung-app.feature.administration.ausbildungsstaette.ausbildungsgang.archiveDialog.title',
        message:
          'sachbearbeitung-app.feature.administration.ausbildungsstaette.ausbildungsgang.archiveDialog.message',
        translationObject: {
          bezeichnungDe: getBezeichnung('De', ausbildungsgang),
          bezeichnungFr: getBezeichnung('Fr', ausbildungsgang),
        },
      },
    )
      .afterClosed()
      .subscribe((confirmed) => {
        if (confirmed) {
          this.administrationAusbildungsstaetteStore.archiveEntity$({
            type: 'ausbildungsgang',
            id: ausbildungsgang.id,
            onSuccess: () => {
              this.reloadAbschluesseSig.set({});
            },
          });
        }
      });
  }

  createAusbildungsgang() {
    CreateAusbildungsgangDialogComponent.open(this.dialog, {
      existingAusbildungsgaenge:
        this.administrationAusbildungsstaetteStore.allAusbildungsgaenge()
          .data ?? [],
      ausbildungsstaetten:
        this.ausbildungsstaetteStore.ausbildungsstaetteViewSig(),
      abschluesse: sortListByText(
        this.ausbildungsstaetteStore.abschluesseViewSig(),
        (item) => item[`bezeichnung${capitalized(this.currentLangSig())}`],
      ),
      language: this.currentLangSig(),
    })
      .afterClosed()
      .subscribe((ausbildungsgangCreate) => {
        if (ausbildungsgangCreate) {
          this.administrationAusbildungsstaetteStore.createAusbildungsgang$({
            values: { ausbildungsgangCreate },
            onSuccess: () => {
              this.reloadAbschluesseSig.set({});
            },
          });
        }
      });
  }

  showInfo(ausbildungsgang: Ausbildungsgang) {
    DataInfoDialogComponent.open(
      this.dialog,
      'sachbearbeitung-app.feature.administration.ausbildungsstaette.ausbildungsgang.infoDialog.title',
      ({ info, translatedInfo, spacer }) => [
        info(
          'sachbearbeitung-app.feature.administration.ausbildungsstaette.ausbildungsstaetteBezeichnungDe',
          ausbildungsgang.ausbildungsstaette.nameDe,
        ),
        info(
          'sachbearbeitung-app.feature.administration.ausbildungsstaette.ausbildungsstaetteBezeichnungFr',
          ausbildungsgang.ausbildungsstaette.nameFr,
        ),
        ...(ausbildungsgang.ausbildungsstaette.chShis
          ? [
              info(
                'sachbearbeitung-app.feature.administration.ausbildungsstaette.chShisNummer',
                ausbildungsgang.ausbildungsstaette.chShis,
              ),
            ]
          : []),
        ...(ausbildungsgang.ausbildungsstaette.ctNo
          ? [
              info(
                'sachbearbeitung-app.feature.administration.ausbildungsstaette.ctNummer',
                ausbildungsgang.ausbildungsstaette.ctNo,
              ),
            ]
          : []),
        ...(ausbildungsgang.ausbildungsstaette.burNo
          ? [
              info(
                'sachbearbeitung-app.feature.administration.ausbildungsstaette.burNummer',
                ausbildungsgang.ausbildungsstaette.burNo,
              ),
            ]
          : []),
        spacer(),
        info(
          'sachbearbeitung-app.feature.administration.ausbildungsstaette.abschlussBezeichnungDe',
          ausbildungsgang.abschluss.bezeichnungDe,
        ),
        info(
          'sachbearbeitung-app.feature.administration.ausbildungsstaette.abschlussBezeichnungFr',
          ausbildungsgang.abschluss.bezeichnungFr,
        ),
        translatedInfo(
          'sachbearbeitung-app.feature.administration.ausbildungsstaette.ausbildungskategorie',
          `shared.ausbildungskategorie.${ausbildungsgang.abschluss.ausbildungskategorie}`,
        ),
        translatedInfo(
          'sachbearbeitung-app.feature.administration.ausbildungsstaette.bildungskategorie',
          `sachbearbeitung-app.feature.administration.ausbildungsstaette.bildungskategorie.${ausbildungsgang.abschluss.bildungskategorie}`,
        ),
        translatedInfo(
          'sachbearbeitung-app.feature.administration.ausbildungsstaette.bildungsrichtung',
          `sachbearbeitung-app.feature.administration.ausbildungsstaette.bildungsrichtung.${ausbildungsgang.abschluss.bildungsrichtung}`,
        ),
        translatedInfo(
          'sachbearbeitung-app.feature.administration.ausbildungsstaette.berufsbefaehigenderAbschluss',
          `sachbearbeitung-app.feature.administration.ausbildungsstaette.boolean.${ausbildungsgang.abschluss.berufsbefaehigenderAbschluss}`,
        ),
        translatedInfo(
          'sachbearbeitung-app.feature.administration.ausbildungsstaette.ferien',
          `sachbearbeitung-app.feature.administration.ausbildungsstaette.ferien.${ausbildungsgang.abschluss.ferien}`,
        ),
        info(
          'sachbearbeitung-app.feature.administration.ausbildungsstaette.bfsKategorie',
          ausbildungsgang.abschluss.bfsKategorie,
        ),
        ...(ausbildungsgang.abschluss.zusatzfrage
          ? [
              translatedInfo(
                'sachbearbeitung-app.feature.administration.ausbildungsstaette.zusatzfrage',
                `sachbearbeitung-app.feature.administration.ausbildungsstaette.zusatzfrage.${ausbildungsgang.abschluss.zusatzfrage}`,
              ),
            ]
          : []),
        spacer(),
        translatedInfo(
          'sachbearbeitung-app.feature.administration.ausbildungsstaette.status',
          `sachbearbeitung-app.feature.administration.ausbildungsstaette.status.${ausbildungsgang.aktiv}`,
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
