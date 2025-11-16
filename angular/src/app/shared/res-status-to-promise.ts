import { Resource, Injector, ResourceStatus, effect } from '@angular/core';

export function resourceStatusToPromise(
  resource: Resource<any>,
  injector: Injector,
  status: ResourceStatus = 'resolved',
) {
  return new Promise<any>((resolve) => {
    const effectRef = effect(
      () => {
        const value = resource.status();
        if (value === 'resolved') {
          resolve(value as any);
          effectRef.destroy();
        }
      },
      { injector },
    );
  });
}
