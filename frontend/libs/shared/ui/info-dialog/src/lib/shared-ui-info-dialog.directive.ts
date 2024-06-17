import { ScrollStrategyOptions } from '@angular/cdk/overlay';
import {
  DestroyRef,
  Directive,
  Input,
  OnDestroy,
  OnInit,
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

import {
  InfoDialogData,
  SharedUiInfoDialogComponent,
} from './shared-ui-info-dialog/shared-ui-info-dialog.component';

@Directive({
  selector: '[dvSharedUiInfoDialog]',
  standalone: true,
})
export class SharedUiInfoDialogDirective implements OnInit, OnDestroy {
  containerRef = inject(ViewContainerRef);
  dialog = inject(MatDialog);
  scrollStrategyOptions = inject(ScrollStrategyOptions);
  destroyRef = inject(DestroyRef);

  @Input({ required: true }) dialogTitle = '';
  @Input({ required: true }) dialogMessage = '';
  @Input() trigger: HTMLButtonElement | undefined = undefined;

  dialogRef: MatDialogRef<SharedUiInfoDialogComponent> | undefined;

  scrollSub: Subscription | undefined;

  ngOnInit() {
    if (this.trigger) {
      this.trigger.addEventListener('click', (e) => {
        e.stopPropagation();
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
      });
    }
  }

  openDialog() {
    const isColumnar = window.innerWidth >= 1200;

    let dialogConfig: MatDialogConfig<InfoDialogData> = {
      data: {
        title: this.dialogTitle,
        message: this.dialogMessage,
      },
      id: 'info-dialog',
    };

    if (isColumnar) {
      const anchor = this.containerRef.element.nativeElement;
      const anchorRect =
        this.containerRef.element.nativeElement.getBoundingClientRect();

      dialogConfig = {
        ...dialogConfig,
        position: {
          top: `${anchorRect.top}px`,
          left: `${anchorRect.left}px`,
        },
        width: `${anchor?.offsetWidth}px`,
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

    if (isColumnar) {
      this.updateDialogPosition();
      this.dialogRef
        .afterClosed()
        .pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe(() => {
          this.scrollSub?.unsubscribe();
        });
    }
  }

  updateDialogPosition() {
    this.scrollSub = fromEvent(window, 'scroll')
      .pipe(takeUntilDestroyed(this.destroyRef), throttleTime(10))
      .subscribe(() => {
        const anchorRect =
          this.containerRef.element.nativeElement.getBoundingClientRect();

        // update the positon so that the dialog stays next to the anchor
        this.dialogRef?.updatePosition({
          top: `${anchorRect.top}px`,
          left: `${anchorRect.left}px`,
        });
      });
  }

  ngOnDestroy() {
    this.dialogRef?.close();
    this.scrollSub?.unsubscribe();
  }
}
