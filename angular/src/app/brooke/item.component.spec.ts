import { ComponentFixture, TestBed } from '@angular/core/testing';

import { beforeEach, describe, expect, it } from 'vitest';
import { configureCommonTestBed } from '../../app-test/common-test-bed';
import { ItemComponent } from './item.component';

describe('ItemComponent', () => {
  let component: ItemComponent;
  let fixture: ComponentFixture<ItemComponent>;

  beforeEach(async () => {
    await configureCommonTestBed({
      imports: [ItemComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ItemComponent);
    component = fixture.componentInstance;
		component.item = {
			name: 'test',
			childItems: []
		};
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
