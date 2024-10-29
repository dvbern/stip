import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SharedUiAusbildungComponent } from './shared-ui-ausbildung.component';

describe('SharedUiAusbildungComponent', () => {
  let component: SharedUiAusbildungComponent;
  let fixture: ComponentFixture<SharedUiAusbildungComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SharedUiAusbildungComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SharedUiAusbildungComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
