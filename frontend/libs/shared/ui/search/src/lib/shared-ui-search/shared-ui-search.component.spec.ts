import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

// eslint-disable-next-line @nx/enforce-module-boundaries
import { getTranslocoModule } from '@dv/shared/pattern/vitest-test-setup';

import { SharedUiSearchComponent } from './shared-ui-search.component';

describe('SharedUiSearchComponent', () => {
  let component: SharedUiSearchComponent;
  let fixture: ComponentFixture<SharedUiSearchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        SharedUiSearchComponent,
        NoopAnimationsModule,
        getTranslocoModule(),
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(SharedUiSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
