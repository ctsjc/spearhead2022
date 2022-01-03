import { Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren, ViewEncapsulation } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { SentenceResponse } from '../sentence-response';

@Component({
  selector: 'app-show-sentences',
  templateUrl: './show-sentences.component.html',
  styleUrls: ['./show-sentences.component.css'],
  encapsulation: ViewEncapsulation.None,
})

export class ShowSentencesComponent implements OnInit {

  sentenceURL: string = "http://localhost:8080/api/interceptor/12345";
  paragraph: SentenceResponse[] = [];
  staticResponse: string = "";

  constructor(private http: HttpClient, private elRef: ElementRef) {
  }

  ngOnInit(): void {
    this.execute();
  }

  execute() {
    console.log('Calling :: ' + this.sentenceURL);
    this.http.get(this.sentenceURL, { responseType: 'json' }).subscribe(
      (data: any) => {
        let paragraph: SentenceResponse[] = data["paragraph"];
        this.paragraph = this.buildUI(paragraph);
      });
  }


  buildUI(paragraph: SentenceResponse[]): SentenceResponse[] {
    let tags: string[] = ['<NP>', '<VP>', '<PP>'];
    let skipTags: string[] = ['<VP>', '<PP>'];
    addSetencesIdentifier();
    addCssMarker();
    addInputElement();
    addFirstCopyMarker();

    return paragraph;


    /* Response do not contain any identifier for each sentence. method adds idenfier */
    function addSetencesIdentifier() {
      for (let i = 0; i < paragraph.length; i++) {
        paragraph[i].id = (1000 + i).toString();
        paragraph[i].firstCopy = paragraph[i].parse
      }
    }

    /*  CSS Marker will highlight Noun phrase, Verb Phrase, NN phrases */
    function addCssMarker(): SentenceResponse[] {

      return paragraph.map(sentence => {
        // start tag
        tags.forEach(tag => {
          sentence.parse = sentence.parse.replace(
            new RegExp(tag, 'g'),
            markerStart(tag));
        });
        // ending tag
        tags.forEach(tag => {
          sentence.parse = sentence.parse.replace(
            new RegExp(tag.replace('<', '<\/'), 'g'),
            markerEnd(tag));
        });

        return sentence;
      });

      function markerEnd(tag: string): string {
        return '</mark>' + tag.replace('<', '<\/') + ' ';
      }

      function markerStart(tag: string): string {
        return tag + '' + '<mark class=' + tag.substring(1, 3) + '>';
      }
    }

    /* Before Noun Phrases, Adds small text box. User will annotate the question */
    function addInputElement(): SentenceResponse[] {

      return paragraph.map(sentence => {
        let idd = sentence.id;
        let index: number = 0;
        // start tag
        tags.filter(tag => !skipTags.includes(tag)).forEach(tag => {
          index = 0;
          sentence.parse = sentence.parse.replace(
            new RegExp(tag, 'g'),
            startInputMarker());

          function startInputMarker(): (substring: string, ...args: any[]) => string {
            return function (tag) {
              const startTag2 = "";
              const startMarker = `${tag}${startTag2}<input type='text' size='4' value=${index}  name=${idd} id=${index++}>`;
              return startMarker;
            };
          }
        });
        return sentence;
      });
    }

    /*  Adds mandatory Start tag before Name entity. */
    function addFirstCopyMarker(): SentenceResponse[] {
      return paragraph.map(sentence => {
        let idd = sentence.id;
        let index: number = 0;
        // start tag
        tags.filter(tag => !skipTags.includes(tag)).forEach(tag => {
          sentence.firstCopy = sentence.firstCopy.replace(
            new RegExp(tag, 'g'),
            startMarker());

          function startMarker(): (substring: string, ...args: any[]) => string {
            return function (tag) {
              const startTag2 = "<START:#" + idd + "-" + (index++) + ">";
              return startTag2;
            };
          }
        });
        // ending tag
        tags.filter(tag => !skipTags.includes(tag)).forEach(tag => {
          sentence.firstCopy = sentence.firstCopy.replace(
            new RegExp(tag.replace('<', '<\/'), 'g'),
            inputMarkerEnd());

          function inputMarkerEnd(): string {
            const endTag = '<END>';
            //const endTag = '';
            return endTag;
          }
        });
        // remove skips
        tags.filter(tag => skipTags.includes(tag)).forEach(tag => {
          sentence.firstCopy = sentence.firstCopy.replace(
            new RegExp(tag, 'g'),
            '');

          sentence.firstCopy = sentence.firstCopy.replace(
            new RegExp(tag.replace('<', '<\/'), 'g'),
            '');
        });
        return sentence;
      });



      /* 
            function inputMarkerEnd(tag: string): string {
              const endTag = '<END>';
              //const endTag = '';
              return endTag + tag.replace('<', '<\/') + ' ';
            } */
    }

  }//end of buildUI

  onShow() {
    for (let index = 0; index < this.paragraph.length; index++) {
      const sentence = this.paragraph[index];
      if (sentence != null) {
        let nodes: NodeListOf<HTMLInputElement> =
          document.getElementsByName(sentence.id) as NodeListOf<HTMLInputElement>;

        for (let j = 0; j < nodes.length; j++) {          
          sentence.firstCopy =
            sentence.firstCopy.replace(
              "<START:#" + sentence.id + "-" + j + ">",
              "<START:" + nodes[j].value + ">")
        }
      }
    }
  }//end of show

  onSave() {
    for (let index = 0; index < this.paragraph.length; index++) {
      const sentence = this.paragraph[index];
      sentence.secondCopy = sentence.firstCopy;
      console.log(sentence.secondCopy);
    }
  }

  onAdd() {
    this.staticResponse= this.staticResponse.replace(/(\r\n|\n|\r|\t|(  ))/gm,"");
    console.log(this.staticResponse);
    let paragraph=JSON.parse(this.staticResponse);
    console.log(paragraph);
    this.paragraph = this.buildUI(paragraph);
  }
}//end of Component