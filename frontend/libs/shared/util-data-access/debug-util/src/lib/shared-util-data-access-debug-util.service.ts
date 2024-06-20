import { Injectable, computed, inject } from '@angular/core';
import { Store } from '@ngrx/store';

import { selectSharedDataAccessConfigsView } from '@dv/shared/data-access/config';
import { isDefined } from '@dv/shared/util-fn/type-guards';

@Injectable({
  providedIn: 'root',
})
export class SharedUtilDataAccessDebugUtilService {
  store = inject(Store);
  environmentSig = computed(() => {
    const config = this.store.selectSignal(selectSharedDataAccessConfigsView)();
    return config.deploymentConfig?.environment;
  });

  /**
   * Returns a logger that logs at the specified level of environment or lower.
   * The levels are ordered as follows:
   * - prod
   * - uat
   * - dev
   * - local
   *
   * @example
   * // Usage
   * export class MyComponent {
   *   logger = inject(SharedUtilDataAccessDebugUtilService).logAtLevel('dev');
   *
   *   someMethod() {
   *     this.logger.log('This will not log if the environment is "prod" or "uat"');
   *   }
   * }
   */
  logAtLevel(level: 'prod' | 'uat' | 'dev' | 'local') {
    return safeLogger(level, this.environmentSig());
  }
}

// eslint-disable-next-line @typescript-eslint/no-empty-function
const noop = () => {};
type LogMethods = Pick<
  typeof console,
  'log' | 'info' | 'debug' | 'warn' | 'error'
>;
const ENV_LEVELS = ['prod', 'uat', 'dev', 'local'] as const;
type EnvLevel = (typeof ENV_LEVELS)[number];
const noopLogger = {
  log: noop,
  info: noop,
  debug: noop,
  warn: noop,
  error: noop,
};

/**
 * Returns a logger that logs at the specified level of environment.
 * If the current level is not found or is lower than the desired level, it returns a noop logger.
 *
 * The levels are ordered as follows:
 * - prod
 * - uat
 * - dev
 * - local
 *
 * @example
 * safeLogger('uat', 'prod').log('This will not log');
 * safeLogger('prod', 'prod').log('This will log');
 * safeLogger('dev', 'local').log('This will log');
 */
export function safeLogger(level: EnvLevel, currentLevel?: string): LogMethods {
  if (!currentLevel) {
    return noopLogger;
  }
  const desiredLevelIndex = ENV_LEVELS.indexOf(level);
  const currentLevelIndex = parseCurrentLevel(currentLevel);
  if (isDefined(currentLevelIndex) && currentLevelIndex >= desiredLevelIndex) {
    return console;
  }
  return noopLogger;
}

/**
 * Parses the current level of environment. Returns null if the level is not found.
 */
const parseCurrentLevel = (currentLevel: string) => {
  const currentLevelIndex = ENV_LEVELS.findIndex((l) => l === currentLevel);
  if (currentLevelIndex === -1) {
    return null;
  }
  return currentLevelIndex;
};
