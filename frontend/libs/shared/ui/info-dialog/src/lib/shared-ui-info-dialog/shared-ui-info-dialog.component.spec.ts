import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SharedUiInfoDialogComponent } from './shared-ui-info-dialog.component';

describe('SharedUiInfoDialogComponent', () => {
  let component: SharedUiInfoDialogComponent;
  let fixture: ComponentFixture<SharedUiInfoDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SharedUiInfoDialogComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SharedUiInfoDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
