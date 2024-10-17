import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Output,
  inject,
  input,
} from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { TranslateModule } from '@ngx-translate/core';
import { Observable, filter, switchMap } from 'rxjs';

import { SharedUiConfirmDialogComponent } from '@dv/shared/ui/confirm-dialog';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';
import { SharedUiInfoContainerComponent } from '@dv/shared/ui/info-container';
import { SharedUiLoadingComponent } from '@dv/shared/ui/loading';
import { SharedUiRdIsPendingPipe } from '@dv/shared/ui/remote-data-pipe';
import { RemoteData } from '@dv/shared/util/remote-data';

@Component({
  selector:
    'dv-sachbearbeitung-app-feature-administration-gesuchsperiode-publish',
  standalone: true,
  imports: [
    TranslateModule,
    SharedUiInfoContainerComponent,
    SharedUiIconChipComponent,
    SharedUiRdIsPendingPipe,
    SharedUiLoadingComponent,
  ],
  templateUrl: './publish.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PublishComponent {
  typeSig = input.required<'gesuchsperiode' | 'gesuchsjahr'>();
  blockedReasonSig = input<RemoteData<string> | null>();
  unsavedChangesSig = input.required<boolean>();
  @Output() publish: Observable<unknown>;
  askForPublish$ = new EventEmitter();

  private dialog = inject(MatDialog);

  constructor() {
    this.publish = this.askForPublish$.pipe(
      switchMap(() =>
        SharedUiConfirmDialogComponent.open(this.dialog, {
          title: `sachbearbeitung-app.admin.${this.typeSig()}.publizieren.title`,
          message:
            `sachbearbeitung-app.admin.${this.typeSig()}.publizieren.confirm.text` +
            (this.unsavedChangesSig() ? '.unsaved' : ''),
          confirmText: this.unsavedChangesSig()
            ? `sachbearbeitung-app.admin.save-and-publish`
            : undefined,
        }).afterClosed(),
      ),
      filter(Boolean),
    );
  }
}
