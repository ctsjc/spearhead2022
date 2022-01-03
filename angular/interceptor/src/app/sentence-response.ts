export interface SentenceResponse {
    parse: string;  /* Input parameter, Accept input first from http get, then  add CSS Marker, The adds input element.*/
    original: string;   /* Pretty useless */
    id:string;/* Uniquely identifies the sentence in paragraph */
    firstCopy: string;  /* it appends Start Tag */
    secondCopy: string;/* It contains the value  that user added in text box */
}
