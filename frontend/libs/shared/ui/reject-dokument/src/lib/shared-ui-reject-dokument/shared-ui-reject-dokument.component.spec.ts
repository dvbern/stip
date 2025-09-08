import { ComponentFixture, TestBed } from '@angular/core/testing';
import {
  MAT_DIALOG_DATA,
  MatDialogModule,
  MatDialogRef,
} from '@angular/material/dialog';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

import { GesuchDokument } from '@dv/shared/model/gesuch';
// eslint-disable-next-line @nx/enforce-module-boundaries
import { getTranslocoModule } from '@dv/shared/pattern/vitest-test-setup';

import { SharedUiRejectDokumentComponent } from './shared-ui-reject-dokument.component';

const dialogData: GesuchDokument = {
  id: '123',
} as any;

class MatDialogRefMock {
  // eslint-disable-next-line @typescript-eslint/no-empty-function
  close() {}
}

describe('SharedUiRejectDokumentComponent', () => {
  let component: SharedUiRejectDokumentComponent;
  let fixture: ComponentFixture<SharedUiRejectDokumentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        SharedUiRejectDokumentComponent,
        MatDialogModule,
        NoopAnimationsModule,
        getTranslocoModule(),
      ],
      providers: [
        { provide: MatDialogRef, useClass: MatDialogRefMock },
        { provide: MAT_DIALOG_DATA, useValue: dialogData },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(SharedUiRejectDokumentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
