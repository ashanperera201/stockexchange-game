import { Component, OnInit } from '@angular/core';
import { NgBlockUI, BlockUI } from 'ng-block-ui';
import { GameBoardService } from '../../../services/game-board.service';
import { CommonEmitterService } from '../../../services/common-emitter.service';
import { ToastrService } from 'ngx-toastr';
import { UserService } from '../../../services/user.service';
import { AuthService } from '../../../services/auth.service';
@Component({
    templateUrl: './game-board-landing.component.html',
    styleUrls: ['./game-board-landing.component.scss']
})

export class GameBoardLandingComponent implements OnInit {
    @BlockUI() blockUI: NgBlockUI;
    commonGameData: any;

    /**
     * Creates an instance of game board landing component.
     * @param gameBoardService 
     * @param toastrService 
     * @param commonEmitterService 
     * @param userService 
     */
    constructor(private gameBoardService: GameBoardService, private toastrService: ToastrService, private commonEmitterService: CommonEmitterService, private userService: UserService, private authService: AuthService) { }

    /**
     * on init
     */
    ngOnInit() {
        this.blockUI.start('Loading please wait .......');
        setTimeout(() => {
            this.blockUI.stop();
        }, 1000);
        this.pumpGameCommonData();
    }

    /**
     * Pump game common data of game board landing component
     */
    pumpGameCommonData = () => {
        this.gameBoardService.getGameCommonData(this.userService.getUserId()).subscribe((serviceResponse: any) => {
            if (serviceResponse) {
                this.commonEmitterService.emitCommonGameData.emit(serviceResponse);
                this.commonEmitterService.playerAmount.emit(serviceResponse.value2.amount);
            }
        });
    }
}
