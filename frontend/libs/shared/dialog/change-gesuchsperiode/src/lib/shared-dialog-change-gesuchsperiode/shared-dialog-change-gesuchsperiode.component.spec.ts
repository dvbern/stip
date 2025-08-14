import { provideHttpClient } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import {
  MAT_DIALOG_DATA,
  MatDialogModule,
  MatDialogRef,
} from '@angular/material/dialog';
import { provideMockStore } from '@ngrx/store/testing';
import { TranslateModule } from '@ngx-translate/core';

import { ChangeGesuchsperiodeStore } from '@dv/shared/data-access/change-gesuchsperiode';

import {
  ChangeGesuchsperiodeDialogData,
  SharedDialogChangeGesuchsperiodeComponent,
} from './shared-dialog-change-gesuchsperiode.component';

class MatDialogRefMock {
  // eslint-disable-next-line @typescript-eslint/no-empty-function
  close() {}
}

const dialogData: ChangeGesuchsperiodeDialogData = {
  gesuchTrancheId: 'test-gesuch-tranche-id',
  gesuchFormular: {
    ausbildung: {
      fallId: '',
      ausbildungBegin: '',
      ausbildungEnd: '',
      pensum: 'VOLLZEIT',
      status: 'AKTIV',
      editable: false,
    },
  },
  gesuchId: 'test-gesuch-id',
  trancheSetting: {
    type: 'AENDERUNG',
    gesuchUrlTyp: 'AENDERUNG',
    routesSuffix: [],
  },
};

describe('SharedDialogChangeGesuchsperiodeComponent', () => {
  let component: SharedDialogChangeGesuchsperiodeComponent;
  let fixture: ComponentFixture<SharedDialogChangeGesuchsperiodeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        SharedDialogChangeGesuchsperiodeComponent,
        MatDialogModule,
        TranslateModule.forRoot(),
      ],
      providers: [
        provideMockStore(),
        provideHttpClient(),
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
