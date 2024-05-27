import { ComponentFixture, TestBed } from '@angular/core/testing';

import { beforeEach, describe, expect, it } from 'vitest';
import { configureCommonTestBed } from '../../test/common-test-bed';
import { BookTocComponent } from './book-toc.component';

describe('BookTocComponent', () => {
  let component: BookTocComponent;
  let fixture: ComponentFixture<BookTocComponent>;

  beforeEach(async () => {
    await configureCommonTestBed({
      imports: [BookTocComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(BookTocComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
