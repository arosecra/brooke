import { ComponentFixture, TestBed } from '@angular/core/testing';

import { beforeEach, describe, expect, it } from 'vitest';
import { configureCommonTestBed } from '../../app-test/common-test-bed';
import { BookPageTurnerComponent } from './book-page-turner.component';

describe('BookPageTurnerComponent', () => {
  let component: BookPageTurnerComponent;
  let fixture: ComponentFixture<BookPageTurnerComponent>;

  beforeEach(async () => {
    await configureCommonTestBed({
      imports: [BookPageTurnerComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(BookPageTurnerComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
