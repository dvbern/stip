import { ComponentFixture, TestBed } from '@angular/core/testing';
import { GesuchAppUiCreateAusbildungDialogComponent } from './gesuch-app-ui-create-ausbildung-dialog.component';

describe('GesuchAppUiCreateAusbildungDialogComponent', () => {
  let component: GesuchAppUiCreateAusbildungDialogComponent;
  let fixture: ComponentFixture<GesuchAppUiCreateAusbildungDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GesuchAppUiCreateAusbildungDialogComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(
      GesuchAppUiCreateAusbildungDialogComponent,
    );
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
