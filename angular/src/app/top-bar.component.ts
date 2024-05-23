import { Component, inject } from '@angular/core';
import { BookPageTurnerComponent } from './book-page-turner.component';
import { BookOptionsComponent } from './book-options.component';
import { BookTocComponent } from './book-toc.component';
import { ModifyCollectionButtonComponent } from './modify-collection-button.component';
import { BreadcrumbComponent } from './breadcrumb.component';
import { BrookeService } from './brooke.service';
import { IconComponent } from "./icon.component";

@Component({
    selector: 'top-bar',
    // templateUrl: './top-bar.component.html',
		template: `
<!-- <header class="brooke-header primary" role="navigation" aria-label="main navigation">
	<div class="brooke-header-leftside">
		<div class="">
			<button class="button primary"
				(click)="onToggleAsideMenuButtonClick()"
			><strong>&#9776;</strong></button>
			 <h2>Brooke</h2> 
			 <a class="navbar-item" href="https://bulma.io">
				<img src="https://bulma.io/images/bulma-logo.png" width="112" height="28">
			</a> 
		</div>
		<breadcrumb class="" style="display: contents; "></breadcrumb>
	
	</div>

	<div class="brooke-header-rightside">
		<modify-collection-button class="m-r--xs" style="display: contents;"></modify-collection-button>
		<book-toc class="m-r--xs" style="display: contents;"></book-toc>
		<book-options class="m-r--xs" style="display: contents;"></book-options>
		<book-page-turner class="" style="display: contents;"></book-page-turner> 
	</div>
</header> -->
<header class="bg-white">
  <div
    class="mx-auto flex h-16 items-left gap-8 px-4 "
  >
    <button
      class="block rounded bg-gray-100 p-2.5 text-gray-600 transition hover:text-gray-600/75"
      (click)="onToggleAsideMenuButtonClick()"
    >
      <span class="sr-only">Toggle menu</span>
      <icon name="hamburger"></icon>
    </button>

    <div class="flex flex-1 items-center justify-end md:justify-between">
      <!--       <nav aria-label="Global" class="hidden md:block">
        <ul class="flex items-center gap-6 text-sm">
          <li>
            <a class="text-gray-500 transition hover:text-gray-500/75" href="#"> About </a>
          </li>

          <li>
            <a class="text-gray-500 transition hover:text-gray-500/75" href="#"> Careers </a>
          </li>

          <li>
            <a class="text-gray-500 transition hover:text-gray-500/75" href="#"> History </a>
          </li>

          <li>
            <a class="text-gray-500 transition hover:text-gray-500/75" href="#"> Services </a>
          </li>

          <li>
            <a class="text-gray-500 transition hover:text-gray-500/75" href="#"> Projects </a>
          </li>

          <li>
            <a class="text-gray-500 transition hover:text-gray-500/75" href="#"> Blog </a>
          </li>
        </ul>
      </nav> -->
      <breadcrumb class="" style="display: contents"></breadcrumb>

      <div class="flex items-center gap-4">
        <div class="sm:flex sm:gap-4">
					@if(brookeService.currentItem()) {
          <book-page-turner
            class=""
            style="display: contents"
          ></book-page-turner>
					}

          <!-- <a
            class="block rounded-md bg-teal-600 px-5 py-2.5 text-sm font-medium text-white transition hover:bg-teal-700"
            href="#"
          >
            Login
          </a>

          <a
            class="hidden rounded-md bg-gray-100 px-5 py-2.5 text-sm font-medium text-teal-600 transition hover:text-teal-600/75 sm:block"
            href="#"
          >
            Register
          </a> -->
        </div>
      </div>
    </div>
  </div>
</header>
		`,
    standalone: true,
    imports: [BreadcrumbComponent, ModifyCollectionButtonComponent, BookTocComponent, BookOptionsComponent, BookPageTurnerComponent, IconComponent]
})
export class TopBarComponent {
	public brookeService: BrookeService = inject(BrookeService)

	onToggleAsideMenuButtonClick() {
		this.brookeService.widgets.asideMenuExpanded.update(() => !this.brookeService.widgets.asideMenuExpanded())
	}
}
