import { CommonModule } from '@angular/common';
import {
  ChangeDetectionStrategy,
  Component,
  effect,
  inject,
  input,
} from '@angular/core';
import { NonNullableFormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { RouterLink } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';

import { NotizStore } from '@dv/sachbearbeitung-app/data-access/notiz';
import { SharedDataAccessGesuchEvents } from '@dv/shared/data-access/gesuch';
import { SharedUiFormSaveComponent } from '@dv/shared/ui/form';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import {
  SharedUiRdIsPendingPipe,
  SharedUiRdIsPendingWithoutCachePipe,
} from '@dv/shared/ui/remote-data-pipe';

@Component({
  selector: 'dv-sachbearbeitung-app-feature-infos-notizen',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    MatFormFieldModule,
    ReactiveFormsModule,
    TranslateModule,
    SharedUiLoadingComponent,
    SharedUiRdIsPendingPipe,
    SharedUiRdIsPendingWithoutCachePipe,
    SharedUiFormSaveComponent,
  ],
  templateUrl:
    './sachbearbeitung-app-feature-infos-notizen-detail.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class SachbearbeitungAppFeatureInfosNotizenDetailComponent {
  private store = inject(Store);
  private formBuilder = inject(NonNullableFormBuilder);
  notizStore = inject(NotizStore);

  form = this.formBuilder.group({});
  notizIdSig = input({ alias: 'notizId' });

  constructor() {
    effect(
      () => {
        const notizId = this.notizIdSig();
        // TODO: weiss noch nicht wie wir die Gesuch/Tranche ID übergeben, muss ich noch klären
        // sprich, nicht neuladen auf /create oder /detail/:notizId Seite
        // this.store.dispatch(SharedDataAccessGesuchEvents.loadGesuch());

        // DELETE ME
        console.log('notizId', notizId);

        // this.notizStore.loadNotiz$({
        //   gesuchId: notizId,
        // });
      },
      { allowSignalWrites: true },
    );
  }

  handleSave() {
    if (!this.form.valid) {
      return;
    }
  }
}
