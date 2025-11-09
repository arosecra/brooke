import { JobStep } from "./job-step";


export class Pipeline {

	public steps: JobStep[] = [];
	public name: string;
	public uses: string[] = [];
	public produces: string;
	public propertyCheck: (file: string) => boolean;
	public remoteDirectory: string;
	
	public addStep(step: JobStep): Pipeline {
		this.steps.push(step);
		return this;
	}
	
	public setName(name: string) {
		this.name = name;
		return this;
	}
	
	public setRemoteDirectory(remoteDirectory: string): Pipeline {
		this.remoteDirectory = remoteDirectory;
		return this;
	}
	
	public setUses(uses: string[]): Pipeline {
		this.uses.push(...uses);
		return this;
	}
	
	public setProduces(produces: string): Pipeline {
		this.produces = produces;
		return this;
	}

	public setPropertyCheck(propertyCheck: (file: string) => boolean): Pipeline {
		this.propertyCheck = propertyCheck;
		return this;
	}

}