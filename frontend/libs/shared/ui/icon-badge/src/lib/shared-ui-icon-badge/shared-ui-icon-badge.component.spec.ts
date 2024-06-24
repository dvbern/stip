import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SharedUiIconBadgeComponent } from './shared-ui-icon-badge.component';

describe('SharedUiIconBadgeComponent', () => {
  let component: SharedUiIconBadgeComponent;
  let fixture: ComponentFixture<SharedUiIconBadgeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SharedUiIconBadgeComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SharedUiIconBadgeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
