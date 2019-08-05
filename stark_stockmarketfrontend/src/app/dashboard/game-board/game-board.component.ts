import { Component, OnInit } from '@angular/core';
import { GameBoardService } from '../../services/game-board.service';
import { Router } from '@angular/router';

@Component({
    templateUrl: './game-board.component.html',
    styleUrls: ['./game-board.component.scss']
})

export class GameBoardComponent implements OnInit {
    /**
     * Creates an instance of game board component.
     * @param gameBoardService 
     * @param router 
     */
    constructor(private gameBoardService: GameBoardService, private router: Router) {
        if (this.gameBoardService.isGameStarted()) {
            this.router.navigate(['/dashboard/game-board/landing'])
        }
    }

    ngOnInit() { }
}
