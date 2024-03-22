import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  OnInit,
  computed,
  inject,
} from '@angular/core';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { RouterLink } from '@angular/router';
import { TranslateModule, TranslateService } from '@ngx-translate/core';

import { GesuchsperiodeStore } from '@dv/sachbearbeitung-app/data-access/gesuchsperiode';
import { SharedUiFocusableListDirective } from '@dv/shared/ui/focusable-list';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';
import { TranslatedPropertyPipe } from '@dv/shared/ui/translated-property-pipe';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-gesuchsperiode',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    RouterLink,
    TranslateModule,
    TranslatedPropertyPipe,
    TypeSafeMatCellDefDirective,
    SharedUiFocusableListDirective,
    SharedUiLoadingComponent,
  ],
  templateUrl: './gesuchsperiode-overview.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GesuchsperiodeOverviewComponent implements OnInit {
  store = inject(GesuchsperiodeStore);
  translate = inject(TranslateService);

  displayedColumns: string[] = [
    'bezeichnung',
    'gesuchsperiode',
    'status',
    'gesuchsjahr',
    'actions',
  ];
  gesuchsperiodenDatasourceSig = computed(() => {
    const gesuchsperioden = this.store.gesuchperiodenListView();
    console.log('gesuchsperioden', gesuchsperioden);
    const datasource = new MatTableDataSource(gesuchsperioden);
    return datasource;
  });

  ngOnInit(): void {
    this.store.loadGesuchsperioden();
  }
}
