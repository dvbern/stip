import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideMockStore } from '@ngrx/store/testing';
import { SharedDialogChangeGesuchsperiodeComponent } from './shared-dialog-change-gesuchsperiode.component';
import {
  MAT_DIALOG_DATA,
  MatDialogModule,
  MatDialogRef,
} from '@angular/material/dialog';
import { ChangeGesuchsperiodeStore } from '@dv/shared/data-access/change-gesuchsperiode';
import { GlobalNotificationStore } from '@dv/shared/global/notification';
import { provideSharedPatternJestTestSetup } from '@dv/shared/pattern/jest-test-setup';

class MatDialogRefMock {
  // eslint-disable-next-line @typescript-eslint/no-empty-function
  close() {}
}

const dialogData = {
  gesuchTrancheId: 'test-gesuch-tranche-id',
};

describe('SharedDialogChangeGesuchsperiodeComponent', () => {
  let component: SharedDialogChangeGesuchsperiodeComponent;
  let fixture: ComponentFixture<SharedDialogChangeGesuchsperiodeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SharedDialogChangeGesuchsperiodeComponent, MatDialogModule],
      providers: [
        provideMockStore({
          initialState: {
            language: 'de',
          },
        }),
        // GlobalNotificationStore,
        provideSharedPatternJestTestSetup(),
        ChangeGesuchsperiodeStore,
        { provide: MatDialogRef, useClass: MatDialogRefMock },
        { provide: MAT_DIALOG_DATA, useValue: dialogData },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(
      SharedDialogChangeGesuchsperiodeComponent,
    );
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
