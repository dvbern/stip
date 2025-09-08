import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormControl } from '@angular/forms';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

// eslint-disable-next-line @nx/enforce-module-boundaries
import { getTranslocoModule } from '@dv/shared/pattern/vitest-test-setup';

import { SharedUiWohnsitzSplitterComponent } from './shared-ui-wohnsitz-splitter.component';

describe('SharedUiWohnsitzSplitterComponent', () => {
  let component: SharedUiWohnsitzSplitterComponent;
  let fixture: ComponentFixture<SharedUiWohnsitzSplitterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        NoopAnimationsModule,
        SharedUiWohnsitzSplitterComponent,
        getTranslocoModule(),
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(SharedUiWohnsitzSplitterComponent);
    component = fixture.componentInstance;
    component.controls = {
      wohnsitzAnteilMutter: new FormControl(<string | undefined>undefined, {
        nonNullable: true,
      }),
      wohnsitzAnteilVater: new FormControl(<string | undefined>undefined, {
        nonNullable: true,
      }),
    };
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
