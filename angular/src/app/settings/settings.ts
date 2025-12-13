import { Component, inject, Injector, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatTableModule } from '@angular/material/table';
import { AppComponent } from '../app.component';
import { Orator } from '../audio/orator';
import { LibraryDB } from '../db/library-db';
import { Category } from '../model/category';
import { Collection } from '../model/collection';
import { PossibleItem } from '../model/possible-item';
import { asyncComputed } from '../shared/signals/async-computed';
import { resourceStatusToPromise } from '../shared/signals/res-status-to-promise';
import { WebFS } from '../shared/web-fs';

@Component({
  selector: 'settings',
  imports: [
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    MatInputModule,
    MatSelectModule,
    FormsModule,
    MatSlideToggleModule,
  ],
  templateUrl: './settings.html',
  styles: ``,
})
export class SettingsComponent {
  sampleText: string = 'The quick brown fox jumped over the lazy dog';
  app = inject(AppComponent);
  appDB = inject(LibraryDB);
  injector = inject(Injector);

  orator = inject(Orator);

  busy = signal<boolean>(false);
  busyMessage = signal<string[]>(['not busy']);
  voice: SpeechSynthesisVoice;

	cacheDirectory = asyncComputed(async () => {
		const cacheDir = this.app.resources().storedLibrary.value()?.cacheDirectory;
		if(!cacheDir) return {
			cachedirectory: cacheDir,
			rw: false
		};
		return {
			cachedirectory: cacheDir,
			rw: await WebFS.hasPermission(cacheDir, 'readwrite')
		};
	})

  async requestCollectionPermission(permissable: Collection) {
    this.busy.set(true);
    this.busyMessage.set(['Requesting Permission']);
    permissable.hasRWPermission = await WebFS.getPermission(permissable.handle, 'readwrite');
    permissable.hasRPermission = await WebFS.getPermission(permissable.handle, 'read');
    this.app.resources()?.storedLibrary.reload();
    this.busy.set(false);
  }

  async requestCacheDirPermission(handle: FileSystemDirectoryHandle) {
    this.busy.set(true);
    this.busyMessage.set(['Requesting Permission']);
    await WebFS.getPermission(handle, 'readwrite');
    this.app.resources()?.storedLibrary.reload();
    this.busy.set(false);
  }

  async updateCollectionFromFiles(collection: Collection) {
    this.busy.set(true);
    this.busyMessage.set(['Downloading Collection']);
    let handle = collection.handle;
    this.removeCollection(collection);
    this.addCollectionFromHandle(handle);
    this.busy.set(false);
  }

  async removeCollection(collection: Collection) {
    const alreadyBusy = this.busy();
    if (!alreadyBusy) {
      this.busy.set(true);
      this.busyMessage.set(['Removing Collection']);
    }
    await this.appDB.removeCollection(collection);
    await this.appDB.removeCategories(collection);
    await this.appDB.removeItems(collection);
    await this.appDB.removeThumbnails(collection);
    this.app.resources()?.storedLibrary.reload();
    if (!alreadyBusy) {
      this.busy.set(false);
    }
  }

  async setLocalCacheDirectory() {
    let handle: FileSystemDirectoryHandle = await window.showDirectoryPicker();

    this.busy.set(true);
    this.busyMessage.set(['Setting cache directory']);

    await WebFS.getPermission(handle, 'readwrite');

    await this.appDB.addSetting({
      name: 'cacheDirectory',
      value: handle,
    });
    this.app.resources()?.storedLibrary.reload();

    resourceStatusToPromise(this.app.resources()?.storedLibrary, this.injector).then(() => {
      this.busy.set(false);
    });
  }

  async addCollectionFromHandle(handle: FileSystemDirectoryHandle) {
    this.busy.set(true);
    this.busyMessage.set(['Adding Collection', ' ', ' ']);

    await WebFS.getPermission(handle, 'readwrite');

    const children = await WebFS.readdir(handle);
    const childDirs: FileSystemDirectoryHandle[] = [];
    let collectionHandle;
    let categoriesHandle;
    children.forEach((child) => {
      if (child.kind === 'directory') childDirs.push(child);
      else if (child.name === 'collection.yaml') collectionHandle = child;
      else if (child.name === 'categories.yaml') categoriesHandle = child;
    });

    console.log(collectionHandle, categoriesHandle, childDirs);
    let collection: Collection = collectionHandle
      ? await WebFS.readYaml<Collection>(collectionHandle)
      : ({} as Collection);
    collection.handle = handle;
    collection.displayName = collection.name.replaceAll('_', ' ');

    this.busyMessage.set([`Adding Collection`, ' ', ' ']);

    await this.appDB.addCollection(collection);

    let category = categoriesHandle ? await WebFS.readYaml<Category[]>(categoriesHandle) : ([] as Category[]);

    await category.forEach(async (cat) => {
      cat.collectionName = collection.name;
      cat.displayName = cat.name.replaceAll('_', ' ');
      this.busyMessage.set([`Adding Category ${cat.name}`]);
      await this.appDB.addCategory(cat);
    });

    this.busyMessage.set(['Collection and Categories added.', 'Processing items', ' ']);

    let itemToCategory: Record<string, Category> = {};
    category.forEach((cat) => {
      cat.items.forEach((itemRef) => {
        itemRef.displayName = itemRef.name.replaceAll(cat.name, '').replaceAll('_', ' ');
        itemToCategory[itemRef.name] = cat;
				if(!itemRef.childItems) itemRef.childItems = [];
        itemRef.childItems.forEach((itemRef) => {
          itemRef.displayName = itemRef.name.replaceAll(cat.name, '').replaceAll('_', ' ');
          itemToCategory[itemRef.name] = cat;
        });
      });
    });

    let parentDirInterogation: PossibleItem;
    for (let i = 0; i < childDirs.length; i++) {
      let child = childDirs[i];
      this.busyMessage.set(['Collection and Categories added.', 'Processing items in ' + child.name, ' ']);
      await WebFS.onLeafDirs(
        child,
        async (index: number, parent: FileSystemDirectoryHandle, leaf: FileSystemDirectoryHandle) => {
          this.busyMessage.set(['Collection and Categories added.', 'Processing leaf folder ' + leaf.name, ' ']);
          if (parentDirInterogation?.name !== parent.name)
            parentDirInterogation = await this.possibleItem(collection, parent);

          const intero = await this.possibleItem(collection, leaf);

          if(intero.item) {

            if (parentDirInterogation.thumbnail && index === 0) {
              this.busyMessage.set([
                'Collection and Categories added.',
                'Processing leaf folder ' + leaf.name,
                'Adding series item',
              ]);
              await this.appDB.addItem({
                name: parentDirInterogation.name,
                collectionName: collection.name,
                series: true,
                dirHandle: parent,
                childItems: [],
              });
              this.busyMessage.set([
                'Collection and Categories added.',
                'Processing leaf folder ' + leaf.name,
                'Adding series thumbnail',
              ]);

              await this.appDB.addThumbnail({
                itemName: parent.name,
                collectionName: collection.name,
                categoryName: itemToCategory[parent.name]?.name ?? 'unassigned',
                thumbnail: await parentDirInterogation.thumbnail.getFile(),
              });
            }

            this.busyMessage.set([
              'Collection and Categories added.',
              'Processing leaf folder ' + leaf.name,
              'Adding item',
            ]);
            await this.appDB.addItem({
              name: leaf.name,
              collectionName: collection.name,
              handle: intero.item,
              ocrHandle: intero.ocr,
              thumbsHandle: intero.thumbs,
              dirHandle: leaf,
              series: false,
              bookDetails: intero.cbtDetails ? await WebFS.readYaml(intero.cbtDetails) : undefined,
              childItems: [],
            });

            if (intero.thumbnail) {
              this.busyMessage.set([
                'Collection and Categories added.',
                'Processing leaf folder ' + leaf.name,
                'Adding item thumbnail',
              ]);
              await this.appDB.addThumbnail({
                itemName: leaf.name,
                collectionName: collection.name,
                categoryName: itemToCategory[leaf.name]?.name ?? 'unassigned',
                thumbnail: await intero.thumbnail.getFile(),
              });
            }

            this.busyMessage.set([
              'Collection and Categories added.',
              'Processing leaf folder ' + leaf.name,
              'Completed item',
            ]);

          }

          return Promise.resolve(true);
        },
        handle,
        i,
      );
    }

    this.busyMessage.set(['Reloading library']);
    this.app.resources()?.storedLibrary.reload();

    resourceStatusToPromise(this.app.resources().storedLibrary, this.injector).then(() => {
      this.busyMessage.set([`Library reloaded`]);
      this.busy.set(false);
    });
  }

  async possibleItem(collection: Collection, dir: FileSystemDirectoryHandle): Promise<PossibleItem> {
    let thumbnail;
    let cbtDetails;
    let item;
    let ocr;
    let thumbs;

    const files = await WebFS.readdir(dir);
    files.forEach((file) => {
      if (file.kind === 'file' && file.name.startsWith('thumbnail')) thumbnail = file;
      if (file.kind === 'file' && file.name.startsWith('cbtDetails')) cbtDetails = file;
      if (file.kind === 'file' && file.name.endsWith('.ocr.gz')) ocr = file;
      if (file.kind === 'file' && file.name.endsWith('.tmb.gz')) thumbs = file;
      if (file.kind === 'file' && file.name.endsWith(collection.itemExtension)) item = file;
    });

    return {
      thumbnail,
      cbtDetails,
      item,
      ocr,
      thumbs,
      name: dir.name,
    };
  }

  async addNewCollection() {
    let handle: FileSystemDirectoryHandle = await window.showDirectoryPicker();
    this.addCollectionFromHandle(handle);
  }

  async playSampleText() {
    this.busy.set(true);
    this.busyMessage.set([' ']);
    this.orator.read(this.sampleText, this.app.resources().storedLibrary.value()!.voice).then(() => {
      this.busy.set(false);
    });
  }

  saveVoice() {
    this.busy.set(true);
    this.busyMessage.set([' ']);
    this.appDB
      .addSetting({
        name: 'voice',
        value: this.app.resources().storedLibrary.value()!.voice,
      })
      .then(() => {
        this.app.resources()?.storedLibrary.reload();

        resourceStatusToPromise(this.app.resources()?.storedLibrary, this.injector).then(() => {
          this.busy.set(false);
        });
      });
  }

  async share() {
    const input = document.getElementById('files');
    const output = document.getElementById('output');

    const files = (input as any)!.files;

    if (files.length === 0) {
      output!.textContent = 'No files selected.';
      return;
    }

    // feature detecting navigator.canShare() also implies
    // the same for the navigator.share()
    if (!navigator.canShare) {
      output!.textContent = `Your browser doesn't support the Web Share API.`;
      return;
    }

    // if (navigator.canShare({ files })) {
    try {
      await navigator.share({
        files,
        title: 'Images',
        text: 'Beautiful images',
      });
      output!.textContent = 'Shared!';
    } catch (error: any) {
      output!.textContent = `Error: ${error.message}`;
    }
  }

  async shareUrl(url: string) {
    const input = document.getElementById('files');
    const output = document.getElementById('output');

    // feature detecting navigator.canShare() also implies
    // the same for the navigator.share()
    if (!navigator.canShare) {
      output!.textContent = `Your browser doesn't support the Web Share API.`;
      return;
    }

    // if (navigator.canShare({ files })) {
    try {
      await navigator.share({
        title: 'mkv',
        text: 'test',
        url,
      });
      output!.textContent = 'Shared!';
    } catch (error: any) {
      output!.textContent = `Error: ${JSON.stringify(error)}`;
    }
    // } else {
    // 	output!.textContent = `Your system doesn't support sharing these files.`;
    // }
  }
}
