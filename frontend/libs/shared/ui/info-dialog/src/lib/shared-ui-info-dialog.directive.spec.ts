import { ChangeDetectionStrategy, Component } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SharedUiInfoDialogDirective } from './shared-ui-info-dialog.directive';

@Component({
  selector: 'dv-test-item-component',
  standalone: true,
  imports: [SharedUiInfoDialogDirective],
  template: `<div dvSharedUiInfoDialog></div>`,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
class TestComponent {}

describe('SharedUiInfoDialogDirective', () => {
  let component: TestComponent;
  let fixture: ComponentFixture<TestComponent>;

  beforeEach(async () => {
    fixture = TestBed.createComponent(TestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
