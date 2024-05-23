/// <reference types="vitest" />
import { defineConfig } from 'vite'

export default defineConfig({
  test: {
		include: ['**/*.{test,spec}.?(c|m)[jt]s?(x)'],
		environment: 'jsdom',
		root: './src/app',
    setupFiles: ['../test/vite-setup.ts'],
		coverage: {
      provider: 'istanbul'
    },
  },
})