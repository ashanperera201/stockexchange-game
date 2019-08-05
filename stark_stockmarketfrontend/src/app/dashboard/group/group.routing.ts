import { Routes, RouterModule } from '@angular/router'
import { CreateGroupComponent } from './create-group/create-group.component'
import { JoinGroupComponent } from './join-group/join-group.component'

let routes: Routes = [
    { path: 'create-group', component: CreateGroupComponent },
    { path: 'join-group', component: JoinGroupComponent }
]

export let GroupRoutings = RouterModule.forChild(routes)