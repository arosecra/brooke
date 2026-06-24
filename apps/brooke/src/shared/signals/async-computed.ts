

import { signal, effect, Signal, WritableSignal } from '@angular/core';

export function asyncComputed<T>(
  computation: () => Promise<T> | undefined | null
): Signal<T | undefined | null> {
  const resultSignal = signal<T | undefined | null>(undefined);

  effect(async () => {
    const result = await computation();
    resultSignal.set(result);
  });

  return resultSignal.asReadonly();
}