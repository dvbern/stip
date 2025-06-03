import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SharedUiLandAutocompleteComponent } from './shared-ui-land-autocomplete.component';

describe('SharedUiLandAutocompleteComponent', () => {
  let component: SharedUiLandAutocompleteComponent;
  let fixture: ComponentFixture<SharedUiLandAutocompleteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SharedUiLandAutocompleteComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SharedUiLandAutocompleteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
