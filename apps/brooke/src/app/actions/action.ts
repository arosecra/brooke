import { resource } from '@angular/core';
import {
  AppDB,
  resourceStatusToPromise,
} from '../../shared';
import { App } from '../../shared/services/App';

export declare interface Action {
  act: () => void;
}

export function action(
  appContext: App,
  loader: () => Promise<void>,
): Action {
  const res = resource<void, void>({
    loader,
  });
  const act = () => {
    appContext.busy.set(true);
    res.reload();
    const pr = resourceStatusToPromise(res);
    pr.then(() => {
      appContext.busy.set(false);
    });
  };
  return {
    act,
  } as Action;
}
