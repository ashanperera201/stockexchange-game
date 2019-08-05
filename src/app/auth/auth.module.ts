import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthComponent } from './auth.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SignUpComponent } from './signup/signup.component';
import { AuthRouterModule } from './auth.routing';
import { LoginComponent } from './login/login.component';
import { AuthService } from '../services/auth.service';
import { RouterModule } from '@angular/router';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MaterialModule } from '../shared/material.module';
import { HttpClientModule } from '@angular/common/http';

@NgModule({
    declarations: [
        AuthComponent,
        LoginComponent,
        SignUpComponent
    ],
    imports: [
        CommonModule,
        AuthRouterModule,
        RouterModule,
        FlexLayoutModule,
        MaterialModule,
        FormsModule,
        ReactiveFormsModule,
        HttpClientModule
    ],
    exports: [

    ],
    providers: [
        AuthService
    ],
})
export class AuthModule { }     