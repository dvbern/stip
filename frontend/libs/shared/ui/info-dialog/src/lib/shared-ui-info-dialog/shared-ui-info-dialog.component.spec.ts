import { ComponentFixture, TestBed } from '@angular/core/testing';
import {
  MAT_DIALOG_DATA,
  MatDialogModule,
  MatDialogRef,
} from '@angular/material/dialog';
import { provideTranslateService } from '@ngx-translate/core';

import {
  InfoDialogData,
  SharedUiInfoDialogComponent,
} from './shared-ui-info-dialog.component';

const dialogData: InfoDialogData = {
  titleKey: 'title',
  messageKey: 'message',
};

class MatDialogRefMock {
  // eslint-disable-next-line @typescript-eslint/no-empty-function
  close() {}
}

describe('SharedUiInfoDialogComponent', () => {
  let component: SharedUiInfoDialogComponent;
  let fixture: ComponentFixture<SharedUiInfoDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SharedUiInfoDialogComponent, MatDialogModule],
      providers: [
        provideTranslateService(),
        { provide: MatDialogRef, useClass: MatDialogRefMock },
        { provide: MAT_DIALOG_DATA, useValue: dialogData },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(SharedUiInfoDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
