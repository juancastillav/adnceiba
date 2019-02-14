import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { map } from 'rxjs/operators';
import { FormsModule } from '@angular/forms';
import { NgKnifeModule } from 'ng-knife';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    NgKnifeModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
