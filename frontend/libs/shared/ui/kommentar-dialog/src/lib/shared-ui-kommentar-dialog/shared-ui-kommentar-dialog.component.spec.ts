import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

// eslint-disable-next-line @nx/enforce-module-boundaries
import { getTranslocoModule } from '@dv/shared/pattern/vitest-test-setup';

import { SharedUiKommentarDialogComponent } from './shared-ui-kommentar-dialog.component';

describe('SharedUiKommentarDialogComponent', () => {
  let component: SharedUiKommentarDialogComponent<string>;
  let fixture: ComponentFixture<SharedUiKommentarDialogComponent<string>>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        SharedUiKommentarDialogComponent,
        NoopAnimationsModule,
        getTranslocoModule(),
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
