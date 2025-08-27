import { ComponentFixture, TestBed } from '@angular/core/testing';

// eslint-disable-next-line @nx/enforce-module-boundaries
import { getTranslocoModule } from '@dv/shared/pattern/vitest-test-setup';

import { SharedUiDropFileComponent } from './shared-ui-drop-file.component';

describe('SharedUiDropFileComponent', () => {
  let component: SharedUiDropFileComponent;
  let fixture: ComponentFixture<SharedUiDropFileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SharedUiDropFileComponent, getTranslocoModule()],
    }).compileComponents();

    fixture = TestBed.createComponent(SharedUiDropFileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
