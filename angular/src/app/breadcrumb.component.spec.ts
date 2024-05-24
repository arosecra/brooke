import { ComponentFixture, TestBed } from '@angular/core/testing';

import { beforeEach, describe, expect, it } from 'vitest';
import { configureCommonTestBed } from '../test/common-test-bed';
import { BreadcrumbComponent } from './breadcrumb.component';

describe('BreadcrumbComponent', () => {
  let component: BreadcrumbComponent;
  let fixture: ComponentFixture<BreadcrumbComponent>;

  beforeEach(async () => {
    await configureCommonTestBed({
      imports: [BreadcrumbComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(BreadcrumbComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
