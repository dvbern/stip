import { provideHttpClient } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

// eslint-disable-next-line @nx/enforce-module-boundaries
import { provideSharedPatternVitestTestSetup } from '@dv/shared/pattern/vitest-test-setup';

import { SharedDialogTrancheErstellenComponent } from './shared-dialog-tranche-erstellen.component';

describe('SharedDialogTrancheErstellenComponent', () => {
  let component: SharedDialogTrancheErstellenComponent;
  let fixture: ComponentFixture<SharedDialogTrancheErstellenComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SharedDialogTrancheErstellenComponent],
      providers: [
        provideSharedPatternVitestTestSetup(),
        provideHttpClient(),
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

    fixture = TestBed.createComponent(SharedDialogTrancheErstellenComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
