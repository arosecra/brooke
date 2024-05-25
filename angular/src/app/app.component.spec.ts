import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AppComponent } from './app.component';
import { BrookeService } from './brooke.service';
import { BrookeServerService } from './brookeserver.service';

import { configureCommonTestBed } from '../test/common-test-bed';
import { beforeEach, describe, expect, it } from 'vitest';

describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;

  beforeEach(async () => {
    await configureCommonTestBed({
      imports: [AppComponent, ],
			providers: [BrookeService, BrookeServerService]
    }).compileComponents();
    
    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(fixture).toBeDefined();
  });
});