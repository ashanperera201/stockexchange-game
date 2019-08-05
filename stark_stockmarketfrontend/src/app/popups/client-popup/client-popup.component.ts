import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { UserService } from '../../services/user.service';
import { ToastrService } from 'ngx-toastr';
import { GameBoardService } from '../../services/game-board.service';

@Component({
    templateUrl: './client-popup.component.html',
    styleUrls: ['./client-popup.component.scss']
})

export class ClientComponent implements OnInit {

    public clientDataList: any[] = [];

    /**
     * Creates an instance of client component.
     * @param dialogRef 
     * @param data 
     * @param userService 
     * @param toastrService 
     * @param gameBoardService 
     */
    constructor(public dialogRef: MatDialogRef<ClientComponent>, @Inject(MAT_DIALOG_DATA) public data: any, private userService: UserService, private toastrService: ToastrService, private gameBoardService: GameBoardService) {
        this.dialogRef.disableClose = true;        
    }

    /**
     * on init
     */
    ngOnInit() {
        this.getPortfolioData(this.getUserId());
    }

    /**
     * Users id
     * @param userId 
     */
    getPortfolioData = (userId: string) => {
        if (userId) {
            this.gameBoardService.getUserPortfolio(userId).subscribe((portfolioServiceResponse: any[]) => {
                if (portfolioServiceResponse && portfolioServiceResponse.length != 0) {
                    this.clientDataList = portfolioServiceResponse;
                } 
            });
        } 
    }

    /**
     * Get user id of client component
     */
    getUserId = () => {
        return this.userService.getUserId();
    }

    /**
     * Dialogs ref
     */
    closeEvent = () => {
        this.dialogRef.disableClose = true;
        this.dialogRef.close();
    }
}