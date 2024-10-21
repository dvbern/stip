import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SharedUiInfoContainerComponent } from './shared-ui-info-container.component';

describe('SharedUiInfoContainerComponent', () => {
  let component: SharedUiInfoContainerComponent;
  let fixture: ComponentFixture<SharedUiInfoContainerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SharedUiInfoContainerComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SharedUiInfoContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
