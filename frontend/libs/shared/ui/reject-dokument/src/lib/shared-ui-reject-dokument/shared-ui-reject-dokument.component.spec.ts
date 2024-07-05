import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SharedUiRejectDokumentComponent } from './shared-ui-reject-dokument.component';

describe('SharedUiRejectDokumentComponent', () => {
  let component: SharedUiRejectDokumentComponent;
  let fixture: ComponentFixture<SharedUiRejectDokumentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SharedUiRejectDokumentComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SharedUiRejectDokumentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
