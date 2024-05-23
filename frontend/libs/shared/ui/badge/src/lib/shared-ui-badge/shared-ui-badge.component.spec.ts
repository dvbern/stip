import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SharedUiBadgeComponent } from './shared-ui-badge.component';

describe('SharedUiBadgeComponent', () => {
  let component: SharedUiBadgeComponent;
  let fixture: ComponentFixture<SharedUiBadgeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SharedUiBadgeComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SharedUiBadgeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
