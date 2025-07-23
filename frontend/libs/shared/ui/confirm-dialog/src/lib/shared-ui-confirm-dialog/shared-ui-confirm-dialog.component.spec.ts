import { ComponentFixture, TestBed } from '@angular/core/testing';
import {
  MAT_DIALOG_DATA,
  MatDialogModule,
  MatDialogRef,
} from '@angular/material/dialog';
import { provideTranslateService } from '@ngx-translate/core';

import {
  ConfirmDialogData,
  SharedUiConfirmDialogComponent,
} from './shared-ui-confirm-dialog.component';

class MatDialogRefMock {
  // eslint-disable-next-line @typescript-eslint/no-empty-function
  close() {}
}

const dialogData: ConfirmDialogData<string> = {
  title: 'title',
  message: 'message',
  confirmText: 'confirmText',
  cancelText: 'cancelText',
};

describe('SharedUiConfirmDialogComponent', () => {
  let component: SharedUiConfirmDialogComponent;
  let fixture: ComponentFixture<SharedUiConfirmDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SharedUiConfirmDialogComponent, MatDialogModule],
      providers: [
        provideTranslateService(),
        { provide: MatDialogRef, useClass: MatDialogRefMock },
        { provide: MAT_DIALOG_DATA, useValue: dialogData },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(SharedUiConfirmDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
