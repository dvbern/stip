import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SharedDataAccessDokumenteComponent } from './shared-data-access-dokumente.component';

describe('SharedDataAccessDokumenteComponent', () => {
  let component: SharedDataAccessDokumenteComponent;
  let fixture: ComponentFixture<SharedDataAccessDokumenteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SharedDataAccessDokumenteComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SharedDataAccessDokumenteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
