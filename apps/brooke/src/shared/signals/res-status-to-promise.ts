import { Resource, Injector, ResourceStatus, effect, inject } from '@angular/core';

export function resourceStatusToPromise(
  resource: Resource<any>,
  status: ResourceStatus = 'resolved',
) {
  // const injector = inject(Injector);
  return new Promise<any>((resolve, reject) => {

    const intrvl = setInterval(() => {
      const value = resource.status();
      if (value === status) {
        clearInterval(intrvl);
        resolve(value as any);
      } else if (value === 'error') {
        clearInterval(intrvl);
        reject(value as any);
      }
    }, 1000);

    // const effectRef = effect(
    //   () => {
    //     const value = resource.status();
    //     if (value === 'resolved') {
    //       resolve(value as any);
    //       effectRef.destroy();
    //     } else if (value === 'error') {
    //       reject(value as any);
    //       effectRef.destroy();
    //     }
    //   },
    //   { injector },
    // );
  });
}
