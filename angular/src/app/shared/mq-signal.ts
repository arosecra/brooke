import { Signal, signal } from "@angular/core";


export declare interface MediaQuerySignal extends Signal<boolean> {
	mediaQueryList: MediaQueryList;
}

export function mqSignal(mediaQuery: string) {
	let writableSignal = signal<boolean>(false);
	let ro: Partial<MediaQuerySignal> = writableSignal.asReadonly();
	ro.mediaQueryList = window.matchMedia(mediaQuery);
	ro.mediaQueryList.addEventListener('change', (match) => {
		writableSignal.set(match.matches);
	});
	return ro as MediaQuerySignal;
}	