import { ScrollStrategyOptions } from '@angular/cdk/overlay';
import {
  DestroyRef,
  Directive,
  Input,
  OnDestroy,
  ViewContainerRef,
  inject,
  input,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import {
  MatDialog,
  MatDialogConfig,
  MatDialogRef,
} from '@angular/material/dialog';
import { Subscription, fromEvent, throttleTime } from 'rxjs';

import { SharedTranslationKey } from '@dv/shared/assets/i18n';
import { SharedModelCompileTimeConfig } from '@dv/shared/model/config';
import { TranslocoHashMap } from '@dv/shared/model/type-util';
import { DVBreakpoints } from '@dv/shared/model/ui-constants';

import {
  InfoDialogData,
  SharedUiInfoDialogComponent,
} from './shared-ui-info-dialog/shared-ui-info-dialog.component';

@Directive({
  selector: '[dvSharedUiInfoDialog]',
  exportAs: 'dvSharedUiInfoDialog',
})
export class SharedUiInfoDialogDirective implements OnDestroy {
  dialogTitleKeySig = input.required<SharedTranslationKey>();
  @Input() dialogTitleParams?: TranslocoHashMap;
  dialogMessageKeySig = input.required<SharedTranslationKey>();
  @Input() dialogMessageParams?: TranslocoHashMap;
  @Input() forceDialogPosition = false;

  containerRef = inject(ViewContainerRef);
  dialog = inject(MatDialog);
  scrollStrategyOptions = inject(ScrollStrategyOptions);
  destroyRef = inject(DestroyRef);
  dialogRef: MatDialogRef<SharedUiInfoDialogComponent> | undefined;
  config = inject(SharedModelCompileTimeConfig);

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
        titleKey: this.dialogTitleKeySig(),
        titleParams: this.dialogTitleParams,
        messageKey: this.dialogMessageKeySig(),
        messageParams: this.dialogMessageParams,
      },
      id: 'info-dialog',
    };

    if (isColumnar) {
      const anchor: HTMLElement = this.containerRef.element.nativeElement;
      const anchorRect = anchor.getBoundingClientRect();
      const isSachbearbeitungApp = this.config.app.view === 'sachbearbeiter';

      dialogConfig = {
        ...dialogConfig,
        position: {
          top: isSachbearbeitungApp
            ? `calc(var(--header-sub-size) + calc(var(--header-size) + var(--tw-spacing) * 6)`
            : `calc(var(--header-size) + var(--tw-spacing) * 6)`,
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
