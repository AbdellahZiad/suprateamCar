import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {IconsProviderModule} from './icons-provider.module';
import {NzLayoutModule} from 'ng-zorro-antd/layout';
import {NzMenuModule} from 'ng-zorro-antd/menu';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {en_US, NZ_I18N} from 'ng-zorro-antd/i18n';
import {registerLocaleData} from '@angular/common';
import en from '@angular/common/locales/en';
import {TableModule} from "primeng/table";
import {ToastModule} from "primeng/toast";
import {CalendarModule} from "primeng/calendar";
import {NzFormModule, NzGridModule, NzInputModule, NzMessageService, NzModalModule} from "ng-zorro-antd";
import {BasicAuthHtppInterceptorService} from "./services/basic-auth-htpp-interceptor.service";
// import {MDBBootstrapModule} from 'angular-bootstrap-md';


registerLocaleData(en);

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    IconsProviderModule,
    NzLayoutModule,
    NzMenuModule,
    FormsModule,
    TableModule,
    HttpClientModule,
    BrowserAnimationsModule,
    ToastModule,
    CalendarModule,
    NzModalModule,
    NzGridModule,
    ReactiveFormsModule,
    NzInputModule,
    NzFormModule,
    // MDBBootstrapModule.forRoot()

  ],
  providers: [{ provide: NZ_I18N, useValue: en_US },NzMessageService,{ provide: HTTP_INTERCEPTORS, useClass: BasicAuthHtppInterceptorService, multi: true }],
  bootstrap: [AppComponent]
})
export class AppModule { }
