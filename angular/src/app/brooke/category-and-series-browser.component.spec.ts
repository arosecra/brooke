import { ComponentFixture, TestBed } from '@angular/core/testing';

import { beforeEach, describe, expect, it } from 'vitest';
import { configureCommonTestBed } from '../../app-test/common-test-bed';
import { CategoryAndSeriesBrowserComponent } from './category-and-series-browser.component';

describe('CategoryAndSeriesBrowserComponent', () => {
  let component: CategoryAndSeriesBrowserComponent;
  let fixture: ComponentFixture<CategoryAndSeriesBrowserComponent>;

  beforeEach(async () => {
    await configureCommonTestBed({
      imports: [CategoryAndSeriesBrowserComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CategoryAndSeriesBrowserComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
