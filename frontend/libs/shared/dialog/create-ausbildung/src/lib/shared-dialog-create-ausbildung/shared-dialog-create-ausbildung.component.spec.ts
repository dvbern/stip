import { provideHttpClient } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import {
  MAT_DIALOG_DATA,
  MatDialogModule,
  MatDialogRef,
} from '@angular/material/dialog';
// eslint-disable-next-line @nx/enforce-module-boundaries
import { provideMockStore } from '@ngrx/store/testing';
import { provideTranslateService } from '@ngx-translate/core';

import { RolesMap } from '@dv/shared/model/benutzer';
// eslint-disable-next-line @nx/enforce-module-boundaries
import {
  mockConfigsState,
  mockedGesuchAppWritableGesuchState,
  provideSharedPatternJestTestSetup,
} from '@dv/shared/pattern/jest-test-setup';

import {
  CreateAusbildungData,
  SharedDialogCreateAusbildungComponent,
} from './shared-dialog-create-ausbildung.component';

class MatDialogRefMock {
  // eslint-disable-next-line @typescript-eslint/no-empty-function
  close() {}
}

const dialogData: CreateAusbildungData = {
  fallId: 'asdf',
};

describe('SharedDialogCreateAusbildungComponent', () => {
  let component: SharedDialogCreateAusbildungComponent;
  let fixture: ComponentFixture<SharedDialogCreateAusbildungComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SharedDialogCreateAusbildungComponent, MatDialogModule],
      providers: [
        provideMockStore({
          initialState: {
            benutzers: {
              rolesMap: {
                V0_Gesuchsteller: true,
              } satisfies RolesMap,
            },
            configs: mockConfigsState(),
            gesuchs: mockedGesuchAppWritableGesuchState({}),
          },
        }),
        provideHttpClient(),
        provideSharedPatternJestTestSetup(),
        provideTranslateService(),
        { provide: MatDialogRef, useClass: MatDialogRefMock },
        { provide: MAT_DIALOG_DATA, useValue: dialogData },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(SharedDialogCreateAusbildungComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
