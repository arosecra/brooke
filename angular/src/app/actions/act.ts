import { WritableSignal } from "@angular/core";
import { Actionable } from "./actionable";

export function act(busySignal: WritableSignal<boolean>, actionable: Actionable) {
  busySignal.set(true);

  let pr = actionable.act();

	if(pr?.then) {
		pr?.then(() => {
			busySignal.set(false);
		})
	} else {
		busySignal.set(false)
	}
}
