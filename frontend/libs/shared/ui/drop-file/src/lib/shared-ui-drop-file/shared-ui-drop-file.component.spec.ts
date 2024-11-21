import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideTranslateService } from '@ngx-translate/core';

import { SharedUiDropFileComponent } from './shared-ui-drop-file.component';

describe('SharedUiDropFileComponent', () => {
  let component: SharedUiDropFileComponent;
  let fixture: ComponentFixture<SharedUiDropFileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SharedUiDropFileComponent],
      providers: [provideTranslateService()],
    }).compileComponents();

    fixture = TestBed.createComponent(SharedUiDropFileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
