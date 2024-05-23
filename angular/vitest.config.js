/// <reference types="vitest" />
import { defineConfig } from 'vite'

export default defineConfig({
  test: {
		root: './src/app',
    setupFiles: ['../test/vite-setup.ts'],
		testTransformMode: {
			web: 'jest-preset-angular'
		}
  },
})