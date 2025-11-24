import { NgModule } from '@angular/core';
import { ToggleBookOptionsComponent } from './toggle-book-options-action.component';
import { ToggleFullscreenActionComponent } from './toggle-fullscreen-action.component';
import { ToggleOratorComponent } from './toggle-orator-action.component';
import { ToggleToCComponent } from './toggle-toc-action.component';
import { CloseActionComponent } from './close-action.component';
import { ToggleMarkdownViewActionComponent } from './toggle-markdown-view-action.component';
import { ToggleThumbnailViewActionComponent } from './toggle-thumbnail-view-action.component';
import { ToggleNumPagesComponent } from './toggle-num-pages-action.component';
import { ToggleSideBySideComponent } from './toggle-side-by-side-action.component';
import { ToggleAddToCComponent } from './toggle-add-toc-action.component';
import { SelectCollectionActionComponent } from './select-collection-action.component';

const components = [
	CloseActionComponent,
	SelectCollectionActionComponent,
	ToggleBookOptionsComponent, 
	ToggleFullscreenActionComponent, 
	ToggleMarkdownViewActionComponent,
	ToggleNumPagesComponent,
	ToggleOratorComponent,
	ToggleSideBySideComponent,
	ToggleThumbnailViewActionComponent,
	ToggleAddToCComponent,
	ToggleToCComponent
];

@NgModule({
  imports: components,
  exports: components,
})
export class ActionsModule {}
