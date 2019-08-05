import { Component, OnInit, Inject, OnDestroy } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { GameBoardService } from '../../services/game-board.service';

@Component({
    templateUrl: './game-summery-popup.component.html',
    styleUrls: ['./game-summery-popup.component.scss']
})

export class GameSummeryComponent implements OnInit, OnDestroy {

    /**
     * Game object list of game summery component
     */
    gameObjectList: any[] = [];
    /**
     * Group name of game summery component
     */
    groupName: any;
    isClseClicked: boolean = false;

    /**
     * Creates an instance of game summery component.
     * @param dialogRef 
     * @param data 
     * @param gameBoardService 
     */
    constructor(public dialogRef: MatDialogRef<GameSummeryComponent>, @Inject(MAT_DIALOG_DATA) public data: any, private gameBoardService: GameBoardService) { }

    /**
     * on init
     */
    ngOnInit() {
        this.dialogRef.disableClose = true;
        this.getSummeryData();
    }

    /**
     * Get summery data of game summery component
     */
    getSummeryData = () => {
        this.gameBoardService.getSummeryDetails(this.data).subscribe((gameSummeryResponse: any[]) => {
            if (gameSummeryResponse && gameSummeryResponse.length != 0) {
                if (gameSummeryResponse.some(x => x.groupName === this.data)) {
                    this.groupName = this.data
                    this.gameObjectList = gameSummeryResponse;
                }
            }
        })
    }

    /**
     * Get profile image of game summery component
     */
    getProfileImage = (game: any): string => {
        if (game) {
            if (game.userId.toString().endsWith("BOT")) {
                return "../../../assets/images/avatars/avatar.png";
            }
            else {
                return game.photoURL;
            }
        }
        return ''
    }

    /**
     * Close the summery modal of game summery component
     */
    closeTheSummeryModal = () => {
        this.dialogRef.disableClose = false;
        this.isClseClicked = true;
        this.deleteGame();
        this.dialogRef.close();
    }

    /**
     * Append profit loss output of game summery component
     */
    AppendProfitLossOutput = (finalAmountValue: number, initialValueAmount: number) => {
        let netValue: number = finalAmountValue - initialValueAmount;
        if (0 <= netValue) {
            //profit
            return "Profit : " + (netValue);
        } else {
            //loss
            return "Loss : " + (netValue);
        }
    }

    /**
     * Delete game of game summery component
     */
    deleteGame = () => {
        this.gameBoardService.deleteObsolete(this.data).subscribe(res => {
        });
    }

    /**
     * on destroy
     */
    ngOnDestroy() {
        if (!this.isClseClicked)
            this.deleteGame();
        else
            this.deleteGame();
    }
}
