import { AfterViewInit, Directive, ElementRef, inject } from "@angular/core";


@Directive({
	selector: 'button[matMiniFab]'
})
export class TestDir implements AfterViewInit {
	el = inject(ElementRef);

	ngAfterViewInit(): void {
		if(this.el.nativeElement.click && this.el.nativeElement.name) {
			console.log(this.el.nativeElement.name);
			const originalClick = this.el.nativeElement.click;
			this.el.nativeElement.click = () => {
				console.log('here');
				originalClick();
			}

		}
	}
	
}