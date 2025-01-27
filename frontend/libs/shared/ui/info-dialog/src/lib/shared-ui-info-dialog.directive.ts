import { ScrollStrategyOptions } from '@angular/cdk/overlay';
import {
  DestroyRef,
  Directive,
  Input,
  OnDestroy,
  ViewContainerRef,
  inject,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import {
  MatDialog,
  MatDialogConfig,
  MatDialogRef,
} from '@angular/material/dialog';
import { Subscription, fromEvent, throttleTime } from 'rxjs';

import { DVBreakpoints } from '@dv/shared/model/ui-constants';

import {
  InfoDialogData,
  SharedUiInfoDialogComponent,
} from './shared-ui-info-dialog/shared-ui-info-dialog.component';

@Directive({
  selector: '[dvSharedUiInfoDialog]',
  standalone: true,
  exportAs: 'dvSharedUiInfoDialog',
})
export class SharedUiInfoDialogDirective implements OnDestroy {
  @Input({ required: true }) dialogTitleKey = '';
  @Input({ required: true }) dialogMessageKey = '';
  @Input() forceDialogPosition = false;

  containerRef = inject(ViewContainerRef);
  dialog = inject(MatDialog);
  scrollStrategyOptions = inject(ScrollStrategyOptions);
  destroyRef = inject(DestroyRef);
  dialogRef: MatDialogRef<SharedUiInfoDialogComponent> | undefined;

  scrollSub: Subscription | undefined;

  toggle() {
    const openInfoDialog = this.dialog.getDialogById('info-dialog');

    if (openInfoDialog && openInfoDialog === this.dialogRef) {
      this.scrollSub?.unsubscribe();
      openInfoDialog.close();
      this.dialogRef = undefined;
    } else if (openInfoDialog) {
      openInfoDialog
        .afterClosed()
        .pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe(() => {
          this.openDialog();
        });
      this.scrollSub?.unsubscribe();
      openInfoDialog.close();
    } else {
      this.openDialog();
    }
  }

  openDialog() {
    const isColumnar = this.forceDialogPosition
      ? false
      : window.innerWidth >= DVBreakpoints.XL;

    let dialogConfig: MatDialogConfig<InfoDialogData> = {
      data: {
        titleKey: this.dialogTitleKey,
        messageKey: this.dialogMessageKey,
      },
      id: 'info-dialog',
    };

    if (isColumnar) {
      const anchor: HTMLElement = this.containerRef.element.nativeElement;
      const anchorRect = anchor.getBoundingClientRect();

      dialogConfig = {
        ...dialogConfig,
        position: {
          top: `${anchorRect.top}px`,
          left: `${anchorRect.left}px`,
        },
        width: `${anchor.offsetWidth}px`,
        height: 'auto',
        hasBackdrop: false,
        panelClass: 'info-dialog-columnar',
        scrollStrategy: this.scrollStrategyOptions.reposition({
          autoClose: true,
        }),
      };
    }

    this.dialogRef = this.dialog.open<
      SharedUiInfoDialogComponent,
      InfoDialogData
    >(SharedUiInfoDialogComponent, dialogConfig);

    if (isColumnar && (!this.scrollSub || this.scrollSub.closed)) {
      this.initPositionUpdates();
    }
  }

  private initPositionUpdates() {
    const header = document.querySelector('header');

    this.scrollSub = fromEvent(window, 'scroll')
      .pipe(takeUntilDestroyed(this.destroyRef), throttleTime(10))
      .subscribe(() => {
        const anchor: HTMLElement = this.containerRef.element.nativeElement;
        const anchorRect = anchor.getBoundingClientRect();

        // Check if the dialog's position is less than or equal to the header's height
        if (
          anchorRect &&
          anchorRect.top <=
            (header?.offsetHeight ?? 0) + (header?.offsetTop ?? 0)
        ) {
          this.dialogRef?.close();
          this.dialogRef = undefined;
        }

        // update the positon so that the dialog stays next to the anchor
        this.dialogRef?.updatePosition({
          top: `${anchorRect.top}px`,
          left: `${anchorRect.left}px`,
        });
      });

    this.dialogRef
      ?.afterClosed()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => {
        this.scrollSub?.unsubscribe();
      });
  }

  ngOnDestroy() {
    this.dialogRef?.close();
    this.scrollSub?.unsubscribe();
  }
}
