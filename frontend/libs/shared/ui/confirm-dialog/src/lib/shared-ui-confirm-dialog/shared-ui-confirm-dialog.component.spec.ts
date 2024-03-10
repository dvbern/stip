import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SharedUiConfirmDialogComponent } from './shared-ui-confirm-dialog.component';

describe('SharedUiConfirmDialogComponent', () => {
  let component: SharedUiConfirmDialogComponent;
  let fixture: ComponentFixture<SharedUiConfirmDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SharedUiConfirmDialogComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SharedUiConfirmDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
