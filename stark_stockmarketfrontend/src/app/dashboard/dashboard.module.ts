//#region Modules
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MaterialModule } from '../shared/material.module';
import { FlexLayoutModule } from '@angular/flex-layout';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { KendoModule } from '../shared/kendo.module';
import { SharedModule } from '@progress/kendo-angular-grid';
import { DashboardRoutes } from './dashboard.routing';
import { AngularFireStorageModule } from '@angular/fire/storage';
import { GroupModule } from './group/group.module';
import { GameBoardModule } from './game-board/game-board.module';
//#endregion
//#region Components
import { DashabordComponent } from './dashboard.component';
import { HomeComponent } from './home/home.component';
import { UserHelperComponent } from './user-helper/user-helper.component';
import { LobbyComponent } from '../popups/lobby/lobby.component';
import { BankAccountComponent } from './bank-account/bank-account.component';
import { CompanyComponent } from './companies/companies.component';
//#endregion
//#region Services
import { CreateGroupService } from '../services/create-group.service';
import { UserService } from '../services/user.service';
import { FirebaseService } from '../services/firebase/firebase.service';
import { CompanyService } from '../services/company.service';
import { CommonEmitterService } from '../services/common-emitter.service';
import { GameBoardService } from '../services/game-board.service';
import { GameGuard } from '../_guards/game.guard';
import { DashboardService } from '../services/dashboard.service';
import { CharStyleService } from '../services/chart-style-services/chart-style.service';
import { MasterService } from '../services/master/master.data.service';
import { UserModule } from './user/user.module';
import { UserHelperService } from '../services/user-helper.service';
//#endregion

@NgModule({
  declarations: [
    DashabordComponent,
    HomeComponent,
    UserHelperComponent,
    LobbyComponent,
    BankAccountComponent,
    CompanyComponent
  ],
  imports: [
    CommonModule,
    DashboardRoutes,
    RouterModule,
    MaterialModule,
    FlexLayoutModule,
    FormsModule,
    ReactiveFormsModule,
    GameBoardModule,
    GroupModule,
    AngularFireStorageModule,
    KendoModule,
    SharedModule,
    UserModule,
  ],
  exports: [],
  entryComponents: [
    LobbyComponent
  ],
  providers: [
    CreateGroupService,
    UserService,
    FirebaseService,
    CompanyService,
    CommonEmitterService,
    GameBoardService,
    GameGuard,
    DashboardService,
    CharStyleService,
    MasterService,
    UserHelperService
  ],
})
export class DashboardModule {

}
