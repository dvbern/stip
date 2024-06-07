import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SharedUiSeverityIconComponent } from './shared-ui-severity-icon.component';

describe('SharedUiSeverityIconComponent', () => {
  let component: SharedUiSeverityIconComponent;
  let fixture: ComponentFixture<SharedUiSeverityIconComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SharedUiSeverityIconComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SharedUiSeverityIconComponent);
    fixture.componentRef.setInput('type', 'info');
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
