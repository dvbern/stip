import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SharedUiTranslatedLandPipe } from './shared-ui-translated-land.pipe';

describe('SharedUiTranslatedLandPipe', () => {
  let component: SharedUiTranslatedLandPipe;
  let fixture: ComponentFixture<SharedUiTranslatedLandPipe>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SharedUiTranslatedLandPipe],
    }).compileComponents();

    fixture = TestBed.createComponent(SharedUiTranslatedLandPipe);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
