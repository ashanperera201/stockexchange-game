import { Routes, RouterModule } from '@angular/router';
import { GameBoardLandingComponent } from './game-board-landing/game-board-landing.component';
import { GameGuard } from '../../_guards/game.guard';


let routes: Routes = [  
    {
        path: 'landing', component: GameBoardLandingComponent
    }
];

export let GamboardRoutes = RouterModule.forChild(routes);
