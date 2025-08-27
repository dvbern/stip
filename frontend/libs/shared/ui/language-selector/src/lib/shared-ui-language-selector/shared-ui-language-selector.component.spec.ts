import { ComponentFixture, TestBed } from '@angular/core/testing';

// eslint-disable-next-line @nx/enforce-module-boundaries
import { getTranslocoModule } from '@dv/shared/pattern/vitest-test-setup';

import { SharedUiLanguageSelectorComponent } from './shared-ui-language-selector.component';

describe('SharedUiLanguageSelectorComponent', () => {
  let component: SharedUiLanguageSelectorComponent;
  let fixture: ComponentFixture<SharedUiLanguageSelectorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SharedUiLanguageSelectorComponent, getTranslocoModule()],
    }).compileComponents();

    fixture = TestBed.createComponent(SharedUiLanguageSelectorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
