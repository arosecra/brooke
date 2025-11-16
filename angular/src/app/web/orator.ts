
import remarkParse from 'remark-parse'
import {unified} from 'unified'
import { flatten } from '../util/flatten-tree';

export class Orator {
	processor: any;
	synthesis: SpeechSynthesis = window.speechSynthesis;
	cancelled: boolean = false;

	constructor() {
		this.processor = unified().use(remarkParse);
	}

	stop() {
		this.cancelled = true;
		this.synthesis.cancel();
	}

	getVoices() {
		return this.synthesis.getVoices().filter((voice) => voice.lang.includes('en'));
	}

	async readBlank() {
		return new Promise((resolve) => {
			setTimeout(() => {
				resolve(true);
			}, 3 * 1000);
		});
	}

  async read(text: string, voiceName: string) {
		const speechSynthesisVoice = this.getVoices().find((voice: SpeechSynthesisVoice) => voice.name === voiceName)
    return new Promise((resolve) => {
      var msg = new SpeechSynthesisUtterance();
      msg.text = text;
			if(speechSynthesisVoice) msg.voice = speechSynthesisVoice;
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

		const paragraphs = flatten(tree, 'value', 'children').flat().map((child) => child.join(' '));

		for(let i = 0; i < paragraphs.length && !this.cancelled; i++) {
			const paragraph = paragraphs[i];
			await this.read(paragraph, voiceName);
		}

	}
}
