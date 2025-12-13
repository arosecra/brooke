

export declare interface Actionable {
	act: (...args: any[]) => Promise<any>;
}