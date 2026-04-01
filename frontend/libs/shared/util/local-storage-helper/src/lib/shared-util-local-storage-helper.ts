import { signal } from '@angular/core';

import { isDefined } from '@dv/shared/model/type-util';

type LocalStorageKeys = {
  DEMO_DATA_PREVIOUS_BERECHNUNG_RESULT: 'Holds a saved run of test all Berechnungen';
};

export const localStorageValue = <T>(key: keyof LocalStorageKeys) => {
  const newValueSig = signal(getOrNullFromLocalStorage<T>(key));
  return {
    valueSig: () => newValueSig(),
    set: (value: T) => {
      newValueSig.set(value);
      return setToLocalStorage<T>(key, value);
    },
    remove: () => {
      newValueSig.set(null);
      return removeFromLocalStorage(key);
    },
  };
};

const getOrNullFromLocalStorage = <T>(
  key: keyof LocalStorageKeys,
): T | null => {
  const item = localStorage.getItem(key);
  if (!isDefined(item)) {
    return null;
  }

  try {
    return JSON.parse(item) as T;
  } catch (e) {
    console.error(`Error parsing localStorage item for key: ${key}:`, e);
    return null;
  }
};

const setToLocalStorage = <T>(key: keyof LocalStorageKeys, value: T) => {
  try {
    localStorage.setItem(key, JSON.stringify(value));
  } catch (e) {
    console.error(`Error setting localStorage item for key: ${key}:`, e);
  }
};

const removeFromLocalStorage = (key: keyof LocalStorageKeys): void => {
  try {
    localStorage.removeItem(key);
  } catch (e) {
    console.error(`Error removing localStorage item for key: ${key}:`, e);
  }
};
