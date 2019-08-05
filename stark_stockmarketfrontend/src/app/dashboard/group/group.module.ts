import { NgModule } from '@angular/core'
import { CommonModule } from '@angular/common'
import { GroupRoutings } from './group.routing'
import { FormsModule, ReactiveFormsModule } from '@angular/forms'
import { FlexLayoutModule } from '@angular/flex-layout'
import { MaterialModule } from '../../shared/material.module'
import { KendoModule } from '../../shared/kendo.module'
import { GroupComponent } from './group.component'
import { CreateGroupComponent } from './create-group/create-group.component'
import { JoinGroupComponent } from './join-group/join-group.component';
import { JoinGroupService } from '../../services/join-group.service'

@NgModule({
    declarations: [
        GroupComponent,
        CreateGroupComponent,
        JoinGroupComponent
    ],
    imports: [
        CommonModule,
        GroupRoutings,
        FormsModule,
        ReactiveFormsModule,
        FlexLayoutModule,
        MaterialModule,
        KendoModule,      
    ],
    exports: [],
    providers: [
        JoinGroupService
    ],
})
export class GroupModule {
    constructor() { }
}