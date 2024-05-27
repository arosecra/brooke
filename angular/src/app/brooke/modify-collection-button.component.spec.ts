import { ComponentFixture, TestBed } from '@angular/core/testing';

import { beforeEach, describe, expect, it } from 'vitest';
import { configureCommonTestBed } from '../../app-test/common-test-bed';
import { ModifyCollectionButtonComponent } from './modify-collection-button.component';

describe('ModifyCollectionButtonComponent', () => {
  let component: ModifyCollectionButtonComponent;
  let fixture: ComponentFixture<ModifyCollectionButtonComponent>;

  beforeEach(async () => {
    await configureCommonTestBed({
      imports: [ModifyCollectionButtonComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ModifyCollectionButtonComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
