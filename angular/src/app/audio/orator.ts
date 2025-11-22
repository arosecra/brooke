import remarkParse from 'remark-parse';
import { unified } from 'unified';
import { Page } from '../model/page';
import { flatten } from '../shared/flatten-tree';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class Orator {
  private processor: any;
  private synthesis: SpeechSynthesis = window.speechSynthesis;
  
	public reading: boolean = false;

  constructor() {
    this.processor = unified().use(remarkParse);
  }

  stop() {
    this.reading = false;
    this.synthesis.cancel();
  }

  getVoices() {
    return this.synthesis.getVoices().filter((voice) => voice.lang.includes('en'));
  }

  async readBook(book: Page[], 
		voiceName: string, 
		startingPage: number, 
		pagesInDisplay: number, 
		afterPageCallback: Function
	) {
		this.reading = true;
		for (let i = startingPage; i < book.length && this.reading; i += pagesInDisplay) {
      if (!book[i].markdown) {
        await this.readBlank();
      } else {
        await this.readMarkdown(book[i].markdown, voiceName);
      }
      if (pagesInDisplay === 2 && i + 1 < book.length) {
        const rPage = i + 1;
        if (!book[rPage].markdown) {
          await this.readBlank();
        } else {
          await this.readMarkdown(book[rPage].markdown, voiceName);
        }
      }
      afterPageCallback();
    }
  }

  async read(text: string, voiceName: string) {
    const speechSynthesisVoice = this.getVoices().find((voice: SpeechSynthesisVoice) => voice.name === voiceName);
    return new Promise((resolve) => {
      var msg = new SpeechSynthesisUtterance();
      msg.text = text;
      if (speechSynthesisVoice) msg.voice = speechSynthesisVoice;
      msg.onend = () => {
        resolve(true);
      };
      window.speechSynthesis.speak(msg);
    });
  }

  async readMarkdown(markdown: string, voiceName: string) {
    const root = await this.processor.parse(markdown);
    const tree = await this.processor.run(root);
    console.log(tree);

    const paragraphs = flatten(tree, 'value', 'children')
      .flat()
      .map((child) => child.join(' '));

    for (let i = 0; i < paragraphs.length && this.reading; i++) {
      const paragraph = paragraphs[i];
      await this.read(paragraph, voiceName);
    }
  }

  private async readBlank() {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(true);
      }, 3 * 1000);
    });
  }
}
