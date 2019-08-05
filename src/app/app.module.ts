import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRouterModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AuthGuard } from './_guards/auth.guard';
import { AngularFireModule } from "@angular/fire";
import { AngularFireAuthModule } from "@angular/fire/auth";
import { environment } from 'src/environments/environment';
import { BlockUIModule } from 'ng-block-ui';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FlexLayoutModule } from '@angular/flex-layout'
import { MaterialModule } from './shared/material.module';
import { HttpClientModule } from '@angular/common/http';
import { ToastrModule } from 'ngx-toastr';
import { AngularFireDatabaseModule } from '@angular/fire/database';
import { GameBoardService } from './services/game-board.service';
import { AuthService } from './services/auth.service';
@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRouterModule,
    AngularFireModule.initializeApp(environment.firebase),
    AngularFireAuthModule,
    AngularFireDatabaseModule,
    BlockUIModule.forRoot(),
    BrowserAnimationsModule,
    MaterialModule,
    FlexLayoutModule,
    HttpClientModule,
    ToastrModule.forRoot(),
  ],
  providers: [
    AuthGuard,
    GameBoardService,
    AuthService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
