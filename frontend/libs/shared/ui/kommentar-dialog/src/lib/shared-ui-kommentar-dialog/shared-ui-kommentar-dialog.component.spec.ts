import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { TranslateModule } from '@ngx-translate/core';

import { SharedUiKommentarDialogComponent } from './shared-ui-kommentar-dialog.component';

describe('SharedUiKommentarDialogComponent', () => {
  let component: SharedUiKommentarDialogComponent;
  let fixture: ComponentFixture<SharedUiKommentarDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        SharedUiKommentarDialogComponent,
        NoopAnimationsModule,
        TranslateModule.forRoot(),
      ],
      providers: [
        {
          provide: MAT_DIALOG_DATA,
          useValue: {
            entityId: 'entityId',
            titleKey: 'titleKey',
            messageKey: 'messageKey',
            placeholderKey: 'placeholderKey',
            confirmKey: 'confirmKey',
          },
        },
        {
          provide: MatDialogRef,
          useValue: {},
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(SharedUiKommentarDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
