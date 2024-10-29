export type Modify<T, R> = Omit<T, keyof R> & R;
export type ModifyList<T, R> =
  T extends Array<infer U> ? Array<Modify<U, R>> : never;
