import { Routes, RouterModule } from '@angular/router';
import { DashabordComponent } from './dashboard.component';
import { HomeComponent } from './home/home.component';
import { BankAccountComponent } from './bank-account/bank-account.component';
import { CompanyComponent } from './companies/companies.component';
import { GameGuard } from '../_guards/game.guard';
import { UserHelperComponent } from './user-helper/user-helper.component';

let routes: Routes = [
  { path: '', redirectTo: 'dashboard/home', pathMatch: 'full' },
  {
    path: '', component: DashabordComponent,
    children: [
     
      { path: '', redirectTo: 'home', pathMatch: 'full' },
      { path: 'home', component: HomeComponent },
      { path: 'company', component: CompanyComponent },
      { path: 'bank-account', component: BankAccountComponent },
      { path: 'group', loadChildren: './group/group.module#GroupModule' },
      { path: 'help', component: UserHelperComponent },     
      {
        path: 'game-board', loadChildren: './game-board/game-board.module#GameBoardModule',
        canActivate: [GameGuard]
      },
      { path: 'user', loadChildren: './user/user.module#UserModule' },
     
      { path: '**', redirectTo: 'home' }
    ]
  },
];

export let DashboardRoutes = RouterModule.forChild(routes);
