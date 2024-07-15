import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SharedUiTableHeaderFilterComponent } from './shared-ui-table-header-filter.component';

describe('SharedUiTableHeaderFilterComponent', () => {
  let component: SharedUiTableHeaderFilterComponent;
  let fixture: ComponentFixture<SharedUiTableHeaderFilterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SharedUiTableHeaderFilterComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SharedUiTableHeaderFilterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
