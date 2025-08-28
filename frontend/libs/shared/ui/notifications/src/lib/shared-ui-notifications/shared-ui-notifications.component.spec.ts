import { ComponentFixture, TestBed } from '@angular/core/testing';

// eslint-disable-next-line @nx/enforce-module-boundaries
import { getTranslocoModule } from '@dv/shared/pattern/vitest-test-setup';

import { SharedUiNotificationsComponent } from './shared-ui-notifications.component';

describe('SharedUiNotificationsComponent', () => {
  let component: SharedUiNotificationsComponent;
  let fixture: ComponentFixture<SharedUiNotificationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SharedUiNotificationsComponent, getTranslocoModule()],
    }).compileComponents();

    fixture = TestBed.createComponent(SharedUiNotificationsComponent);
    component = fixture.componentInstance;
    fixture.componentRef.setInput('notifications', []);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
