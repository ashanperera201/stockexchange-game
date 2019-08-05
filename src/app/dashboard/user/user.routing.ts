import { Routes, RouterModule } from '@angular/router'
import { UserLandingComponent } from './landing/user-landing.component';
import { UserUpdateComponent } from './update/user-update.component';

let routes: Routes = [
    { path: '', redirectTo: 'landing', pathMatch: 'full' },
    { path: 'landing', component: UserLandingComponent },
    { path: 'update', component: UserUpdateComponent }
]

export let UserRoutes = RouterModule.forChild(routes)