import { TestBed } from '@angular/core/testing';

import { AppComponent } from './app.component';
import { BrookeService } from './brooke.service';
import { BrookeServerService } from './brookeserver.service';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { describe, test, expect, beforeEach, it } from 'vitest';

describe('AppComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppComponent, ],
			providers: [BrookeService, BrookeServerService, provideHttpClient(), provideHttpClientTesting()]
    }).compileComponents();
  });

  it('should match the snapshot', () => {
    const fixture = TestBed.createComponent(AppComponent);

    expect(fixture).toBeDefined();
  });
});