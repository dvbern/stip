import {
  cleanupZuweisung,
  removeDuplicates,
  sortZuweisung,
} from './sachbearbeiter-helper';

const testDataset = {
  cleanupZuweisung: [
    ['a-', 'a'],
    ['-a', 'a'],
    ['a-b', 'a-b'],
    ['a-b-', 'a-b'],
  ],
  removeDuplicates: [
    ['a,,b', 'a,b'],
    ['a--b', 'a-b'],
    ['a,,b,,c', 'a,b,c'],
    ['a,,b,c', 'a,b,c'],
  ],
  sortZuweisung: [
    ['a,b', 'a,b'],
    ['b,a', 'a,b'],
    ['b,a,c', 'a,b,c'],
    ['b-a,c-z', 'a-b,c-z'],
    ['daa-baa,az-zz', 'az-zz,baa-daa'],
  ],
  endResult: [
    ['f,,b', 'b,f'],
    ['a,,b,,  c', 'a,b,c'],
    ['v, ,b,a', 'a,b,v'],
    ['b,a,c-', 'a,b,c'],
    ['b-a, c-z', 'a-b,c-z'],
    ['daa- -baa, az--zz', 'az-zz,baa-daa'],
  ],
} as const;

describe('sachbearbeiter-helper', () => {
  it('cleanupZuweisung', () => {
    testDataset.cleanupZuweisung.forEach(([input, expected]) => {
      expect(cleanupZuweisung(input)).toBe(expected);
    });
  });

  it('removeDuplicates', () => {
    testDataset.removeDuplicates.forEach(([input, expected]) => {
      expect(removeDuplicates(input)).toBe(expected);
    });
  });

  it('sortZuweisung', () => {
    testDataset.sortZuweisung.forEach(([input, expected]) => {
      expect(sortZuweisung(input)).toBe(expected);
    });
  });

  it('endResult', () => {
    testDataset.endResult.forEach(([input, expected]) => {
      expect(sortZuweisung(removeDuplicates(cleanupZuweisung(input)))).toBe(
        expected,
      );
    });
  });
});
