import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GesuchAppUiAenderungsEntryComponent } from './gesuch-app-ui-aenderungs-entry.component';

describe('GesuchAppUiAenderungsEntryComponent', () => {
  let component: GesuchAppUiAenderungsEntryComponent;
  let fixture: ComponentFixture<GesuchAppUiAenderungsEntryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GesuchAppUiAenderungsEntryComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(GesuchAppUiAenderungsEntryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
