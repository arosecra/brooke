export type Nullable<T> = {
  [K in keyof T]: T[K] extends number ? T[K] : T[K] | null;
};
