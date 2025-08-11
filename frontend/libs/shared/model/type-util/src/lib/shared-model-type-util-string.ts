export const lowercased = <T extends string>(value: T) =>
  value.toLocaleLowerCase() as Lowercase<T>;

export const uppercased = <T extends string>(value: T) =>
  value.toLocaleUpperCase() as Uppercase<T>;

export const capitalized = <T extends string>(text: T) =>
  (text.length > 1
    ? `${text[0].toLocaleUpperCase()}${text.slice(1)}`
    : text.toUpperCase()) as Capitalize<T>;

export type OnlyString<T> = T extends string ? T : never;
