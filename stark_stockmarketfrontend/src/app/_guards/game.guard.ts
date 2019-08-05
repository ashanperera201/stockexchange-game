import { Injectable, OnDestroy } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { GameBoardService } from '../services/game-board.service';
import { CommonEmitterService } from '../services/common-emitter.service';

@Injectable({ providedIn: 'root' })
export class GameGuard implements CanActivate, OnDestroy {
    /**
     * Creates an instance of game guard.
     * @param gameBoardService 
     * @param router 
     */
    constructor(private gameBoardService: GameBoardService, private router: Router, private commonEmitterService: CommonEmitterService) { }

    /**
     * Determines whether activate can
     * @returns activate 
     */
    canActivate(): Observable<boolean> {
        return new Observable<boolean>(obs => {
            let isGameStarted: boolean = this.gameBoardService.isGameStarted();
            if (isGameStarted) {
                obs.next(true);
                obs.complete();
            } else {
                obs.next(false);
                this.router.navigate(['/dashboard/home']);
                obs.complete();
            }
        })
    }

    ngOnDestroy() {
    }
}
