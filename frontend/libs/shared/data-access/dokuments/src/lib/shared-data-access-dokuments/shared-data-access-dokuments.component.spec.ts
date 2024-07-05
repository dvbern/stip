import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SharedDataAccessDokumentsComponent } from './shared-data-access-dokuments.component';

describe('SharedDataAccessDokumentsComponent', () => {
  let component: SharedDataAccessDokumentsComponent;
  let fixture: ComponentFixture<SharedDataAccessDokumentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SharedDataAccessDokumentsComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SharedDataAccessDokumentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
