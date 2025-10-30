export class Task {
  constructor(
		public pipelineName: string,
    public itemFolder: string,
    public assigned: boolean = false
  ) {}
}
