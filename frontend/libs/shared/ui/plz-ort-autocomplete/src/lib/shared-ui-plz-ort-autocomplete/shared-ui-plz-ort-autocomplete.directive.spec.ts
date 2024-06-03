import { provideHttpClient } from '@angular/common/http';
import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormControl, FormGroup } from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';

import { PlzService } from '@dv/shared/model/gesuch';

import { SharedUiPlzOrtAutocompleteDirective } from './shared-ui-plz-ort-autocomplete.directive';

@Component({
  selector: 'dv-test-item-component',
  standalone: true,
  imports: [SharedUiPlzOrtAutocompleteDirective, MatAutocompleteModule],
  template: `
    <div
      dvPlzOrtAutocomplete
      [autocompleteSig]="autocomplete"
      [plzFormSig]="plzForm"
    ></div>
    <mat-autocomplete #autocomplete="matAutocomplete"></mat-autocomplete>
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
class TestListComponent {
  @Input() plzForm!: FormGroup<{
    plz: FormControl<string | undefined>;
    ort: FormControl<string | undefined>;
  }>;
}

describe('SharedUiPlzOrtAutocompleteComponent', () => {
  let component: TestListComponent;
  let fixture: ComponentFixture<TestListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TestListComponent, SharedUiPlzOrtAutocompleteDirective],
      providers: [PlzService, provideHttpClient()],
    }).compileComponents();

    fixture = TestBed.createComponent(TestListComponent);
    component = fixture.componentInstance;
    component.plzForm = new FormGroup({
      plz: new FormControl(),
      ort: new FormControl(),
    });
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
