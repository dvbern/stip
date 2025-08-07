import { signal } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import {
  MAT_DIALOG_DATA,
  MatDialogModule,
  MatDialogRef,
} from '@angular/material/dialog';
import { provideMockStore } from '@ngrx/store/testing';

import { ChangeGesuchsperiodeStore } from '@dv/shared/data-access/change-gesuchsperiode';
// eslint-disable-next-line @nx/enforce-module-boundaries
import { provideSharedPatternJestTestSetup } from '@dv/shared/pattern/jest-test-setup';
import { success } from '@dv/shared/util/remote-data';

import { SharedDialogChangeGesuchsperiodeComponent } from './shared-dialog-change-gesuchsperiode.component';

class MatDialogRefMock {
  // eslint-disable-next-line @typescript-eslint/no-empty-function
  close() {}
}

class GesuchsperiodeStoreServiceMock {
  assignableGesuchsperioden = signal(success([]));
  changeGesuchsperiode = signal(success(undefined));

  // eslint-disable-next-line @typescript-eslint/no-empty-function
  getAllAssignableGesuchsperiode$() {}
  // eslint-disable-next-line @typescript-eslint/no-empty-function
  setGesuchsperiodeForGesuch$() {}
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
        provideMockStore(),
        provideSharedPatternJestTestSetup(),
        {
          provide: ChangeGesuchsperiodeStore,
          useClass: GesuchsperiodeStoreServiceMock,
        },
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
