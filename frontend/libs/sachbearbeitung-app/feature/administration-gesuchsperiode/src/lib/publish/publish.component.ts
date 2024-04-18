import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Output,
  inject,
  input,
} from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { NgbAlertModule } from '@ng-bootstrap/ng-bootstrap';
import { TranslateModule } from '@ngx-translate/core';
import { Observable, filter, switchMap } from 'rxjs';

import { SharedUiConfirmDialogComponent } from '@dv/shared/ui/confirm-dialog';
import { SharedUiIconChipComponent } from '@dv/shared/ui/icon-chip';

@Component({
  selector:
    'dv-sachbearbeitung-app-feature-administration-gesuchsperiode-publish',
  standalone: true,
  imports: [TranslateModule, NgbAlertModule, SharedUiIconChipComponent],
  templateUrl: './publish.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PublishComponent {
  typeSig = input.required<'gesuchsperiode' | 'gesuchsjahr'>();
  @Output() publish: Observable<unknown>;
  askForPublish$ = new EventEmitter();

  private dialog = inject(MatDialog);

  constructor() {
    this.publish = this.askForPublish$.pipe(
      switchMap(() =>
        SharedUiConfirmDialogComponent.open(this.dialog, {
          title: `sachbearbeitung-app.admin.${this.typeSig()}.publizieren.title`,
          message: `sachbearbeitung-app.admin.${this.typeSig()}.publizieren.confirm.text`,
        }).afterClosed(),
      ),
      filter(Boolean),
    );
  }
}
