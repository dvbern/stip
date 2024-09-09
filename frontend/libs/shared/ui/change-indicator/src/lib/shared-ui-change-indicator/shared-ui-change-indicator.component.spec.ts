import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SharedUiChangeIndicatorComponent } from './shared-ui-change-indicator.component';

describe('SharedUiChangeIndicatorComponent', () => {
  let component: SharedUiChangeIndicatorComponent;
  let fixture: ComponentFixture<SharedUiChangeIndicatorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SharedUiChangeIndicatorComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SharedUiChangeIndicatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
