import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { NgbAccordionModule } from '@ng-bootstrap/ng-bootstrap';
import { HttpClientModule } from '@angular/common/http';
import { AppComponent } from './app.component';
import { SchemasComponent } from './schemas/schemas.component';
import { FormsModule} from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { MDBBootstrapModule } from 'angular-bootstrap-md';
import { AuthGuard } from './auth.guard';
import { AuthComponent } from './auth/auth.component';
import { ProfileComponent } from './profile/profile.component';
import {AmplifyAngularModule, AmplifyService} from 'aws-amplify-angular';
import {BackendService} from "./backend.service";

@NgModule({
  declarations: [
    AppComponent,
    SchemasComponent,
    AuthComponent,
    ProfileComponent
  ],
  imports: [
    BrowserModule,
    NgbAccordionModule,
    HttpClientModule,
    AppRoutingModule,
    AmplifyAngularModule,
    FormsModule,
    MDBBootstrapModule
  ],
  providers: [AmplifyService, AuthGuard, BackendService],
  bootstrap: [AppComponent]
})
export class AppModule { }
