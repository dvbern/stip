import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TranslateModule } from '@ngx-translate/core';

import { SharedUiNotificationsComponent } from './shared-ui-notifications.component';

describe('SharedUiNotificationsComponent', () => {
  let component: SharedUiNotificationsComponent;
  let fixture: ComponentFixture<SharedUiNotificationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SharedUiNotificationsComponent, TranslateModule.forRoot()],
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