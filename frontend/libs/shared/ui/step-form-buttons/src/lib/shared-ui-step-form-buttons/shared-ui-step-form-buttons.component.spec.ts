import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SharedUiStepFormButtonsComponent } from './shared-ui-step-form-buttons.component';

describe('SharedUiStepFormButtonsComponent', () => {
  let component: SharedUiStepFormButtonsComponent;
  let fixture: ComponentFixture<SharedUiStepFormButtonsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SharedUiStepFormButtonsComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SharedUiStepFormButtonsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
