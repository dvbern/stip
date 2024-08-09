import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

// eslint-disable-next-line @nx/enforce-module-boundaries
import { provideSharedPatternJestTestSetup } from '@dv/shared/pattern/jest-test-setup';

import { SharedUiAenderungMeldenDialogComponent } from './shared-ui-aenderung-melden-dialog.component';

describe('SharedUiAenderungMeldenDialogComponent', () => {
  let component: SharedUiAenderungMeldenDialogComponent;
  let fixture: ComponentFixture<SharedUiAenderungMeldenDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SharedUiAenderungMeldenDialogComponent],
      providers: [
        provideSharedPatternJestTestSetup(),
        {
          provide: MAT_DIALOG_DATA,
          useValue: {
            minDate: new Date(),
            maxDate: new Date(),
          },
        },
        {
          provide: MatDialogRef,
          useValue: {},
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(SharedUiAenderungMeldenDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
