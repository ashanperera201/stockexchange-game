import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GameBoardComponent } from './game-board.component';
import { GamboardRoutes } from './game-board.routing';
import { GameWatchComponent } from './game-watch/game-watch.component';
import { GameMiddleLayoutComponent } from './game-middle/game-middle.component';
import { GameHeaderLayoutComponent } from './game-header/game-header.component';
import { GameBoardLandingComponent } from './game-board-landing/game-board-landing.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from '../../shared/material.module';
import { KendoModule } from '../../shared/kendo.module';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MasterService } from '../../services/master/master.data.service';
import { GameBoardService } from '../../services/game-board.service';
import { ClientComponent } from '../../popups/client-popup/client-popup.component';
import { CompanyAnalysisComponent } from '../../popups/company-analysis/company-analysis.component';
import { StatisticComponent } from '../../popups/statistic/statistic.component';
import { CountdownModule } from 'ngx-countdown';
import { GameSummeryComponent } from '../../popups/game-summery-popup/game-summery-popup.component';
import { BotService } from '../../services/bot.service';

@NgModule({
    declarations: [
        GameBoardComponent,
        GameWatchComponent,
        GameMiddleLayoutComponent,
        GameHeaderLayoutComponent,
        GameBoardLandingComponent,
        ClientComponent,
        CompanyAnalysisComponent,
        StatisticComponent,
        GameSummeryComponent
    ],
    imports: [
        CommonModule,
        GamboardRoutes,
        FormsModule,
        ReactiveFormsModule,
        MaterialModule,
        KendoModule,
        FlexLayoutModule,
        CountdownModule
    ],
    exports: [
    ],
    entryComponents: [
        ClientComponent,
        CompanyAnalysisComponent,
        StatisticComponent,
        GameSummeryComponent
    ],
    providers: [
        MasterService,
        GameBoardService,
        BotService
    ],
})
export class GameBoardModule { }