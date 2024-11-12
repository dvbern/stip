import { provideHttpClient } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import {
  MAT_DIALOG_DATA,
  MatDialogModule,
  MatDialogRef,
} from '@angular/material/dialog';
// eslint-disable-next-line @nx/enforce-module-boundaries
import { provideMockStore } from '@ngrx/store/testing';
import { TranslateModule } from '@ngx-translate/core';

// eslint-disable-next-line @nx/enforce-module-boundaries
import { provideSharedPatternJestTestSetup } from '@dv/shared/pattern/jest-test-setup';

import {
  CreateAusbildungData,
  GesuchAppDialogCreateAusbildungComponent,
} from './gesuch-app-dialog-create-ausbildung.component';

class MatDialogRefMock {
  // eslint-disable-next-line @typescript-eslint/no-empty-function
  close() {}
}

const dialogData: CreateAusbildungData = {
  fallId: 'asdf',
};

describe('GesuchAppDialogCreateAusbildungComponent', () => {
  let component: GesuchAppDialogCreateAusbildungComponent;
  let fixture: ComponentFixture<GesuchAppDialogCreateAusbildungComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        GesuchAppDialogCreateAusbildungComponent,
        MatDialogModule,
        TranslateModule.forRoot(),
      ],
      providers: [
        provideMockStore(),
        provideHttpClient(),
        provideSharedPatternJestTestSetup(),
        { provide: MatDialogRef, useClass: MatDialogRefMock },
        { provide: MAT_DIALOG_DATA, useValue: dialogData },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(GesuchAppDialogCreateAusbildungComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
