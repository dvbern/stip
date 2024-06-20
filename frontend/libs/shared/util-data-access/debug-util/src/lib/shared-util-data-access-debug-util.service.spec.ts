import { TestBed } from '@angular/core/testing';
import { Store } from '@ngrx/store';
import { MockStore, provideMockStore } from '@ngrx/store/testing';

import { SharedUtilDataAccessDebugUtilService } from './shared-util-data-access-debug-util.service';

describe('SharedUtilDataAccessDebugUtilService', () => {
  let service: SharedUtilDataAccessDebugUtilService;
  let storeMock: MockStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideMockStore({
          initialState: {
            configs: {
              deploymentConfig: undefined,
            },
          },
        }),
      ],
    });
    service = TestBed.inject(SharedUtilDataAccessDebugUtilService);
    storeMock = TestBed.inject(Store) as any;
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it.each([
    // current, level, shouldLog
    ['prod', 'prod', true],
    ['prod', 'uat', false],
    ['prod', 'dev', false],
    ['prod', 'local', false],
    ['uat', 'prod', true],
    ['uat', 'uat', true],
    ['uat', 'dev', false],
    ['uat', 'local', false],
    ['dev', 'prod', true],
    ['dev', 'uat', true],
    ['dev', 'dev', true],
    ['dev', 'local', false],
    ['local', 'prod', true],
    ['local', 'uat', true],
    ['local', 'dev', true],
    ['local', 'local', true],
  ] as const)(
    'should return a logger when current env is [%s] and target env is [%s], it should log: %s',
    (current, level, shouldLog) => {
      storeMock.setState({
        configs: { deploymentConfig: { environment: current } },
      });
      const logger = service.logAtLevel(level);
      const spy = jest.spyOn(console, 'info');
      logger.info(`should be called [${level}:${current}] = ${shouldLog}`);
      if (shouldLog) {
        expect(spy).toHaveBeenCalled();
      } else {
        expect(spy).not.toHaveBeenCalled();
      }
      spy.mockReset();
    },
  );
});
