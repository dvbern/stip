import { provideHttpClient } from '@angular/common/http';
import { signal } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatDialog } from '@angular/material/dialog';
import { provideRouter } from '@angular/router';
import { provideMockStore } from '@ngrx/store/testing';
import { of } from 'rxjs';

import { GesuchStore } from '@dv/sachbearbeitung-app/data-access/gesuch';
import { GesuchInfoStore } from '@dv/shared/data-access/gesuch-info';
import { provideSharedPatternJestTestSetup } from '@dv/shared/pattern/jest-test-setup';
import { StatusUebergang } from '@dv/shared/util/gesuch';
import { success } from '@dv/shared/util/remote-data';

import { SachbearbeitungAppPatternGesuchHeaderComponent } from './sachbearbeitung-app-pattern-gesuch-header.component';

describe('SachbearbeitungAppPatternGesuchHeaderComponent', () => {
  let component: SachbearbeitungAppPatternGesuchHeaderComponent;
  let fixture: ComponentFixture<SachbearbeitungAppPatternGesuchHeaderComponent>;
  const setStatus$ = {
    SET_TO_BEARBEITUNG: jest.fn(),
    ANSPRUCH_PRUEFEN: jest.fn(),
    BEARBEITUNG_ABSCHLIESSEN: jest.fn(),
    ZURUECK_ZU_BEREIT_FUER_BEARBEITUNG: jest.fn(),
    BEREIT_FUER_BEARBEITUNG: jest.fn(),
    NEGATIVE_VERFUEGUNG_ERSTELLEN: jest.fn(),
    VERFUEGT: jest.fn(),
    VERSENDET: jest.fn(),
    ZURUECKWEISEN: jest.fn(),
    STATUS_PRUEFUNG_AUSLOESEN: jest.fn(),
  } satisfies Record<StatusUebergang, unknown>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SachbearbeitungAppPatternGesuchHeaderComponent],
      providers: [
        GesuchInfoStore,
        provideRouter([]),
        provideHttpClient(),
        provideMockStore({
          initialState: {
            gesuchs: {
              cache: {},
            },
            configs: {
              compileTimeConfig: undefined,
            },
          },
        }),
        provideSharedPatternJestTestSetup(),
        {
          provide: MatDialog,
          useValue: {
            open: () => ({ afterClosed: () => of({ type: 'grund' }) }),
          },
        },
      ],
    })
      .overrideProvider(GesuchStore, {
        useValue: {
          gesuchInfo: signal(
            success({
              gesuchStatus: 'BEREIT_FUER_BEARBEITUNG',
            }),
          ),
          setStatus$,
        },
      })
      .compileComponents();

    fixture = TestBed.createComponent(
      SachbearbeitungAppPatternGesuchHeaderComponent,
    );
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it.each([
    ['BEARBEITUNG_ABSCHLIESSEN'],
    ['BEREIT_FUER_BEARBEITUNG'],
    // ['ZURUECK_ZU_BEREIT_FUER_BEARBEITUNG'], is the same as 'BEREIT_FUER_BEARBEITUNG'
    ['NEGATIVE_VERFUEGUNG_ERSTELLEN'],
    ['VERFUEGT'],
    ['STATUS_PRUEFUNG_AUSLOESEN'],
  ] satisfies [StatusUebergang][])(
    'should call setStatus$[%s] when using setStatusUebergang',
    (nextStatus) => {
      component.setStatusUebergang(nextStatus, 'gesuchId', 'gesuchTrancheId');
      expect(setStatus$[nextStatus]).toHaveBeenCalledWith({
        gesuchTrancheId: 'gesuchTrancheId',
      });
    },
  );

  it.each([['VERSENDET'], ['ZURUECKWEISEN']] satisfies [StatusUebergang][])(
    'should call setStatus$[%s] when using setStatusUebergang with onSuccess',
    (nextStatus) => {
      component.setStatusUebergang(nextStatus, 'gesuchId', 'gesuchTrancheId');
      expect(setStatus$[nextStatus]).toHaveBeenCalledWith({
        gesuchTrancheId: 'gesuchTrancheId',
        onSuccess: expect.any(Function),
      });
    },
  );
});
