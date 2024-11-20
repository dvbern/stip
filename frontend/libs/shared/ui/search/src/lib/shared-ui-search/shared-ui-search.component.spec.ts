import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { provideTranslateService } from '@ngx-translate/core';

import { SharedUiSearchComponent } from './shared-ui-search.component';

describe('SharedUiSearchComponent', () => {
  let component: SharedUiSearchComponent;
  let fixture: ComponentFixture<SharedUiSearchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SharedUiSearchComponent, NoopAnimationsModule],
      providers: [provideTranslateService()],
    }).compileComponents();

    fixture = TestBed.createComponent(SharedUiSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
