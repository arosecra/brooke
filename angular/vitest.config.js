/// <reference types="vitest" />
import { defineConfig } from 'vite'

export default defineConfig({
  test: {
		include: ['**/*.{test,spec}.?(c|m)[jt]s?(x)'],
		environment: 'jsdom',
		root: './src/app',
    reporters: ['basic'],
		setupFiles: ['../app-test/vite-setup.ts'],

		coverage: {
      provider: 'istanbul',
			reporter: ['html', 'text'],
			reportsDirectory: '../../coverage/'
    },
  },
})