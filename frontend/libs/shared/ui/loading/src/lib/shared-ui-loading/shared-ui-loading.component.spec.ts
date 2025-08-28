import { ComponentFixture, TestBed } from '@angular/core/testing';

// eslint-disable-next-line @nx/enforce-module-boundaries
import { getTranslocoModule } from '@dv/shared/pattern/vitest-test-setup';

import { SharedUiLoadingComponent } from './shared-ui-loading.component';

describe('SharedUiLoadingComponent', () => {
  let component: SharedUiLoadingComponent;
  let fixture: ComponentFixture<SharedUiLoadingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SharedUiLoadingComponent, getTranslocoModule()],
    }).compileComponents();

    fixture = TestBed.createComponent(SharedUiLoadingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
