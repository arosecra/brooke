import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JobQueueComponent } from './job-queue.component';
import { describe, test, expect, beforeEach, it } from 'vitest';
import { configureCommonTestBed } from '../../test/common-test-bed';

describe('JobQueueComponent', () => {
  let component: JobQueueComponent;
  let fixture: ComponentFixture<JobQueueComponent>;

  beforeEach(async () => {
    await configureCommonTestBed({
      imports: [JobQueueComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(JobQueueComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
