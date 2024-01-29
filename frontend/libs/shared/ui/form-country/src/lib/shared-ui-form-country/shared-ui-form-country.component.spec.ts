import { ChangeDetectionStrategy, Component } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { TranslateModule } from '@ngx-translate/core';

import { SharedUiFormCountryComponent } from './shared-ui-form-country.component';

@Component({
  imports: [ReactiveFormsModule],
  template: `<dv-shared-ui-form-country
    [formControl]="control"
    [laender]="[]"
    [labelText]="'test'"
  ></dv-shared-ui-form-country>`,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
class TestComponent {
  control = new FormControl('');
}

describe('SharedUiFormCountryComponent', () => {
  let component: TestComponent;
  let fixture: ComponentFixture<TestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        NoopAnimationsModule,
        ReactiveFormsModule,
        SharedUiFormCountryComponent,
        TranslateModule.forRoot(),
      ],
      declarations: [TestComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(TestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
