import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { FormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ShowSentencesComponent } from './show-sentences/show-sentences.component';
import { HttpClientModule } from '@angular/common/http';
import { SafeHtmlPipe } from './util/safe-html.pipe';

@NgModule({
  declarations: [
    AppComponent,
    ShowSentencesComponent,
    SafeHtmlPipe
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
    ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
