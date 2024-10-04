import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { EinreichenStore } from './shared-data-access-einreichen.store';

describe('EinreichenStore', () => {
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  let store: EinreichenStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [EinreichenStore, provideHttpClient()],
    });
    store = TestBed.inject(EinreichenStore);
  });

  // TODO: rework previous test
  // it('should select correct invalidFormularProps', () => {
  //   const state = {
  //     gesuchFormular: {
  //       personInAusbildung: {} as any,
  //       partner: {} as any,
  //       kinds: [],
  //     },
  //   };
  //   const result = selectSharedDataAccessGesuchValidationView.projector(
  //     {
  //       tranchenChanges: null,
  //     },
  //     state,
  //   );
  //   expect(result.invalidFormularProps.validations).toEqual({
  //     errors: ['partner', 'kinds'],
  //     warnings: undefined,
  //     hasDocuments: null,
  //   });
  // });
});
