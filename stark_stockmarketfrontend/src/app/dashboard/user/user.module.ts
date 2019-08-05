import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserLandingComponent } from './landing/user-landing.component';
import { UserRoutes } from './user.routing';
import { UserUpdateComponent } from './update/user-update.component';
import { UserComponent } from './user.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from 'src/app/shared/material.module';
import { FlexLayoutModule } from '@angular/flex-layout';

@NgModule({
    declarations: [
        UserComponent,
        UserLandingComponent,
        UserUpdateComponent
    ],
    imports: [
        CommonModule,
        UserRoutes,
        FormsModule,
        ReactiveFormsModule,
        MaterialModule,
        FlexLayoutModule,
    ],
    exports: [],
    providers: [],
})
export class UserModule {

    constructor() {

    }
}