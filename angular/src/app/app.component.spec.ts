import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AppComponent } from './app.component';
import { BrookeService } from './brooke.service';
import { BrookeServerService } from './brookeserver.service';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { describe, test, expect, beforeEach, it } from 'vitest';
import { provideExperimentalZonelessChangeDetection } from '@angular/core';

describe('AppComponent', () => {
	let fixture: ComponentFixture<AppComponent> | undefined = undefined;
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppComponent, ],
			providers: [BrookeService, BrookeServerService, provideHttpClient(), provideHttpClientTesting(), provideExperimentalZonelessChangeDetection()]
    }).compileComponents();
    fixture = TestBed.createComponent(AppComponent);
		await fixture.whenStable();
  });

  it('should match the snapshot', () => {
    expect(fixture).toBeDefined();
  });
});