import { ComponentFixture, TestBed } from '@angular/core/testing';

import { beforeEach, describe, expect, it } from 'vitest';
import { configureCommonTestBed } from '../../test/common-test-bed';
import { BookOptionsComponent } from './book-options.component';

describe('BookOptionsComponent', () => {
  let component: BookOptionsComponent;
  let fixture: ComponentFixture<BookOptionsComponent>;

  beforeEach(async () => {
    await configureCommonTestBed({
      imports: [BookOptionsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(BookOptionsComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
