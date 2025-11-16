
import remarkParse from 'remark-parse'
import {unified} from 'unified'

export class Orator {
	processor: any;
	synthesis: SpeechSynthesis = window.speechSynthesis;

	constructor() {
		this.processor = unified().use(remarkParse);
	}

	stop() {

	}

	getVoices() {
		return this.synthesis.getVoices().filter((voice) => voice.lang.includes('en'));
	}

	async readBlank() {

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

		const paragraphs = this.flatten(tree).flat().map((child) => child.join(' '));

		for(let i = 0; i < paragraphs.length; i++) {
			const paragraph = paragraphs[i];
			console.log(paragraph);
			await this.read(paragraph, voiceName);
		}

	}

	flatten(node: any) {
		const res = [];
		if(node.value) {
			res.push(node.value);
		}
		if(node.children) {
			return [...res, node.children.map((child: any) => this.flatten(child))]
		}
		return res;
	}
}
