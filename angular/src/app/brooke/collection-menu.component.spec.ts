import { ComponentFixture, TestBed } from '@angular/core/testing';

import { beforeEach, describe, expect, it } from 'vitest';
import { configureCommonTestBed } from '../../test/common-test-bed';
import { CollectionMenuComponent } from './collection-menu.component';

describe('CollectionMenuComponent', () => {
  let component: CollectionMenuComponent;
  let fixture: ComponentFixture<CollectionMenuComponent>;

  beforeEach(async () => {
    await configureCommonTestBed({
      imports: [CollectionMenuComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CollectionMenuComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
