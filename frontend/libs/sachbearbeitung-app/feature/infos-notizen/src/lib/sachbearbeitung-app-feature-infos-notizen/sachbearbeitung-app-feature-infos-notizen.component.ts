import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  inject,
  input,
  viewChild,
} from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { MatMenuModule } from '@angular/material/menu';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';

import { NotizStore } from '@dv/sachbearbeitung-app/data-access/notiz';
import { SharedDataAccessGesuchEvents } from '@dv/shared/data-access/gesuch';
import { PermissionStore } from '@dv/shared/global/permission';
import {
  GesuchNotiz,
  GesuchNotizCreate,
  GesuchNotizTyp,
  JuristischeAbklaerungNotiz,
} from '@dv/shared/model/gesuch';
import { SharedUiConfirmDialogComponent } from '@dv/shared/ui/confirm-dialog';
import { SharedUiFocusableListDirective } from '@dv/shared/ui/focusable-list';
import { SharedUiFormSaveComponent } from '@dv/shared/ui/form';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import {
  SharedUiRdIsPendingPipe,
  SharedUiRdIsPendingWithoutCachePipe,
} from '@dv/shared/ui/remote-data-pipe';
import { TypeSafeMatCellDefDirective } from '@dv/shared/ui/table-helper';

import {
  NotizDialogData,
  SachbearbeitungAppFeatureInfosNotizenDetailDialogComponent,
} from '../sachbearbeitung-app-feature-infos-notizen-detail-dialog/sachbearbeitung-app-feature-infos-notizen-detail-dialog.component';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-infos-notizen',
  standalone: true,
  imports: [
    CommonModule,
    TranslateModule,
    MatFormFieldModule,
    MatTableModule,
    TypeSafeMatCellDefDirective,
    MatTooltipModule,
    SharedUiFocusableListDirective,
    SharedUiLoadingComponent,
    SharedUiRdIsPendingPipe,
    SharedUiRdIsPendingWithoutCachePipe,
    SharedUiFormSaveComponent,
    MatInput,
    ReactiveFormsModule,
    MatMenuModule,
  ],
  templateUrl: './sachbearbeitung-app-feature-infos-notizen.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureInfosNotizenComponent {
  private store = inject(Store);
  private dialog = inject(MatDialog);
  displayedColumns = [
    'notizTyp',
    'datum',
    'user',
    'betreff',
    'notiz',
    'actions',
  ];
  notizStore = inject(NotizStore);
  permissionStore = inject(PermissionStore);

  gesuchIdSig = input.required<string>({ alias: 'id' });
  sortSig = viewChild(MatSort);

  notizSig = computed(() => {
    const notiz = this.notizStore.notizenListViewSig();
    const datasource = new MatTableDataSource(notiz);
    datasource.sort = this.sortSig() ?? null;
    return datasource;
  });

  constructor() {
    this.store.dispatch(SharedDataAccessGesuchEvents.loadGesuch());

    effect(
      () => {
        const gesuchId = this.gesuchIdSig();

        this.notizStore.loadNotizen$({
          gesuchId,
        });
      },
      { allowSignalWrites: true },
    );
  }

  createNotiz(notizTyp: GesuchNotizTyp) {
    SachbearbeitungAppFeatureInfosNotizenDetailDialogComponent.open(
      this.dialog,
      {
        notizTyp,
      },
    ).subscribe((result) => {
      const gesuchId = this.gesuchIdSig();

      if (result) {
        const gesuchNotizCreate: GesuchNotizCreate = {
          betreff: result.betreff,
          text: result.text,
          notizTyp,
          gesuchId,
        };

        this.notizStore.createNotiz({
          gesuchNotizCreate,
        });
      }
    });
  }

  editNotiz(notiz: GesuchNotiz | JuristischeAbklaerungNotiz) {
    let dialogData: NotizDialogData | undefined;

    if (isJurNotiz(notiz)) {
      dialogData = {
        notizTyp: 'JURISTISCHE_NOTIZ',
        notiz,
      };
    } else {
      dialogData = {
        notizTyp: 'GESUCH_NOTIZ',
        notiz,
      };
    }

    // to be continued
    SachbearbeitungAppFeatureInfosNotizenDetailDialogComponent.open(
      this.dialog,
      dialogData,
    ).subscribe(() => {
      // const gesuchId = this.gesuchIdSig();
      // if (notizDaten) {
      //   this.notizStore.editNotiz({ gesuchId, notizDaten });
      // }
    });
  }

  deleteNotiz(notizId: string) {
    SharedUiConfirmDialogComponent.open(this.dialog, {
      title: 'sachbearbeitung-app.infos.notiz.delete.title',
      message: 'sachbearbeitung-app.infos.notiz.delete.text',
    })
      .afterClosed()
      .subscribe((confirmed) => {
        const gesuchId = this.gesuchIdSig();
        if (confirmed) {
          this.notizStore.deleteNotiz({ gesuchId, notizId });
        }
      });
  }
}

const isJurNotiz = (
  notiz: GesuchNotiz | JuristischeAbklaerungNotiz,
): notiz is JuristischeAbklaerungNotiz => {
  return notiz.notizTyp === 'JURISTISCHE_NOTIZ';
};
