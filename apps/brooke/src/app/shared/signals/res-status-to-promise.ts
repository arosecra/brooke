import { Resource, Injector, ResourceStatus, effect } from '@angular/core';

export function resourceStatusToPromise(
  resource: Resource<any>,
  injector: Injector,
  status: ResourceStatus = 'resolved',
) {
  return new Promise<any>((resolve, reject) => {
    const effectRef = effect(
      () => {				
        const value = resource.status();
        if (value === 'resolved') {
          resolve(value as any);
          effectRef.destroy();
        } else if (value === 'error') {
					reject(value as any);
          effectRef.destroy();
				}
      },
      { injector },
    );
  });
}
