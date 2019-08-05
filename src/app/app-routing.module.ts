import { Routes, RouterModule } from '@angular/router';
import { AuthGuard } from './_guards/auth.guard';

let routes: Routes = [
    { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
    { path: 'auth', loadChildren: './auth/auth.module#AuthModule' },
    {
        path: 'dashboard', loadChildren: './dashboard/dashboard.module#DashboardModule',
        canActivate: [AuthGuard]
    },
    { path: '**', redirectTo: 'dashboard' }
];

export let AppRouterModule = RouterModule.forRoot(routes, { useHash: true })
