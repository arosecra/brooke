import { provideHttpClient } from "@angular/common/http";
import { provideHttpClientTesting } from "@angular/common/http/testing";
import { provideExperimentalZonelessChangeDetection } from "@angular/core";
import { TestBed, TestModuleMetadata } from "@angular/core/testing";
import { BrookeService } from "../app/brooke/brooke.service";
import { BrookeServerService } from "../app/brooke/brookeserver.service";
import { provideBackend } from "./http-backend";


export function configureCommonTestBed(testModuleMetaData: TestModuleMetadata): TestBed {
	
	if(!testModuleMetaData.providers) {
		testModuleMetaData.providers = []
	}
	
	testModuleMetaData.providers.push(BrookeService);
	testModuleMetaData.providers.push(BrookeServerService);
	testModuleMetaData.providers?.push(provideHttpClient());
	// testModuleMetaData.providers?.push(provideHttpClientTesting());
	testModuleMetaData.providers?.push(provideBackend());

	testModuleMetaData.providers?.push(provideExperimentalZonelessChangeDetection());

	let result: TestBed = TestBed.configureTestingModule(testModuleMetaData);
	return result;
}