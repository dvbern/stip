import { createFilterableColumns } from './table-helper';

describe('createFilterableColumns', () => {
  it('should create filterable columns', () => {
    const displayedColumns = ['name', 'age', 'city', 'fullAddress', 'zip-code'];
    const filterableColumns = createFilterableColumns(displayedColumns, [
      'name',
      'fullAddress',
      'zip-code',
    ]);
    expect(filterableColumns).toStrictEqual([
      'name-filter',
      'age',
      'city',
      'fullAddress-filter',
      'zip-code-filter',
    ]);
  });
});
