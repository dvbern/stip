export const createFilterableColumns = <T extends string>(
  columns: readonly T[],
  filterable: T[],
) => {
  return columns.map((column) =>
    filterable.includes(column) ? (`${column}-filter` as const) : column,
  );
};
