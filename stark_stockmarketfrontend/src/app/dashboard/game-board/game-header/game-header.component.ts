import { Component, OnInit, OnDestroy, ViewChild, HostListener } from '@angular/core';
import { Router } from '@angular/router';
import { NgBlockUI, BlockUI } from 'ng-block-ui';
import { GameBoardService } from '../../../services/game-board.service';
import { MatDialog } from '@angular/material';
import { MasterService } from '../../../services/master/master.data.service';
import { ClientComponent } from '../../../popups/client-popup/client-popup.component';
import { ToastrService } from 'ngx-toastr';
import { switchMap } from 'rxjs/operators/switchMap';
import { CompanyAnalysisComponent } from '../../../popups/company-analysis/company-analysis.component';
import { StatisticComponent } from '../../../popups/statistic/statistic.component';
import { CommonEmitterService } from '../../../services/common-emitter.service';
import { UserService } from '../../../services/user.service';
import { JoinGroupService } from '../../../services/join-group.service';
import { CountdownComponent } from 'ngx-countdown';
import { GameSummeryComponent } from '../../../popups/game-summery-popup/game-summery-popup.component';
import { Subscription } from 'rxjs';
import { BotService } from '../../../services/bot.service';
import { AuthService } from '../../../services/auth.service';
@Component({
    selector: 'game-header-layout',
    templateUrl: './game-header.component.html',
    styleUrls: ['./game-header.component.scss']
})

export class GameHeaderLayoutComponent implements OnInit, OnDestroy {
    @ViewChild(CountdownComponent, { static: false }) counter: CountdownComponent;
    @BlockUI() blockUI: NgBlockUI;
    botSubscriptor: Subscription;
    gameSubscriptor: Subscription;
    roundCount: number = 1;
    clients: any[];
    analysis: any[];
    statistics: any[];
    playerAmount: any;
    isFinished: boolean = false;
    timeToBeSpent: number = 60 * 2;
    config: any = { leftTime: this.timeToBeSpent }
    toastConfig: any = { positionClass: 'toast-top-left' }
    user: any;
    interval: any;
    status: any;
    botCount: number = 20;
    groupNameView: any;

    /**
     * Creates an instance of game header layout component.
     * @param router 
     * @param gameBoardService 
     * @param masterService 
     * @param dialog 
     * @param toastrService 
     * @param commonEmitterService 
     * @param userService 
     * @param joinGroupService 
     */
    constructor(private router: Router, private gameBoardService: GameBoardService, private masterService: MasterService, public dialog: MatDialog, private toastrService: ToastrService, private commonEmitterService: CommonEmitterService, private userService: UserService, private joinGroupService: JoinGroupService, private botService: BotService, private authService: AuthService) {
    }

    /**
     * on init
     */
    ngOnInit() {
        this.getMasterData();
        this.getUser();
        this.triggerThePlayerScore();
        this.checkWhetherTheOpponentIsBot();
    }

    /**
     * Get user of game header layout component
     */
    getUser = () => {
        this.user = this.userService.getProfile();
    }

    /**
     * Check whether the opponent is bot of game header layout component
     */
    checkWhetherTheOpponentIsBot = () => {
        this.status = this.gameBoardService.getGameStatus();
        if (this.status && this.status.isBot)
            if (!this.botService.getBotFromLocally()) {
                this.blockUI.start('Processing........');
                let groupName: string = this.userService.getUserId() + "BOT";
                this.botService.addBotGroupName(groupName);
                this.botService.addPostGame(this.createGameForBot()).subscribe(response => {
                    if (response) {
                        //success.
                        this.botCountDown(this.botCount);
                        this.botService.saveBotSessionStorage();
                        this.blockUI.stop();
                    } else {
                        //failed
                        this.blockUI.stop();
                    }
                }, error => { this.blockUI.stop(); });
            } else {
                this.botCountDown(this.botCount);
            }
    }

    /**
     * Create game for bot of game header layout component
     */
    createGameForBot = () => {
        let payload = {
            amount: 0,
            email: this.user[0].email,
            groupName: this.user[0].verifiedUser.userId + "BOT",
            initialValue: 0,
            photoURL: this.userService.getUserProfilePicture(),
            roundNo: 0,
            userId: this.user[0].verifiedUser.userId,
            userName: this.user[0].verifiedUser.userName
        }
        return payload;
    }


    /**
     * Bot count down of game header layout component
     */
    botCountDown = (count: number) => {
        let seconds = count;
        this.interval = setInterval(() => {
            seconds--;
            if (seconds == 0) {
                clearInterval(this.interval)
                this.botService.executeBot(this.botExecutionPayload()).subscribe((resopnse: any) => {
                    this.botCountDown(count);
                })
            }
        }, 1000);
    }

    /**
     * Bot execution payload of game header layout component
     */
    botExecutionPayload = () => {
        let payload = {
            amount: this.playerAmount ? this.playerAmount : 0,
            email: this.user[0].email,
            groupName: this.user[0].verifiedUser.userId + "BOT",
            initialValue: 0,
            photoURL: this.userService.getUserProfilePicture(),
            roundNo: this.roundCount ? this.roundCount : 0,
            userId: this.user[0].verifiedUser.userId,
            userName: this.user[0].verifiedUser.userName
        }
        return payload;
    }

    /**
     * Get master data of game header layout component
     */
    getMasterData = () => {
        this.masterService.getClients().pipe((switchMap((clientResponse: any) => {
            if (clientResponse) { this.clients = clientResponse; }
            return this.masterService.getAnalysisData();
        }))).pipe((switchMap((analysisResponse: any) => {
            if (analysisResponse) { this.analysis = analysisResponse; }
            return this.masterService.getStatisticdata();
        }))).subscribe((statisticResponse: any) => {
            if (statisticResponse) {
                this.statistics = statisticResponse
            }
        })
    }

    /**
     * Open client modal of game header layout component
     */
    openClientModal = (selectedClient: any) => {
        if (selectedClient) {
            let modalReference = this.dialog.open(ClientComponent, {
                width: '80em',
                height: '30em',
                data: selectedClient
            })
        } else {
            this.toastrService.warning('Please select a client model.', 'Warning');
        }
    }

    /**
     * Open analysis modal of game header layout component
     */
    openAnalysisModal = (analysisData: any) => {
        let modalReference = this.dialog.open(CompanyAnalysisComponent, {
            maxWidth: '120em',
            width: '70em',
            height: '35rem',
            data: analysisData
        })
    }

    /**
     * Open statistic modal of game header layout component
     */
    openStatisticModal = (statisticData: any) => {
        let modalReference = this.dialog.open(StatisticComponent, {
            maxWidth: '120em',
            width: '70em',
            height: 'auto',
            data: statisticData
        })
    }

    /**
     * Open game summery of game header layout component
     */
    openGameSummery = (gameInformation: any) => {
        let modalReference = this.dialog.open(GameSummeryComponent, {
            maxWidth: '120em',
            width: '70em',
            height: '35em',
            data: gameInformation
        });
    }

    /**
     * Get selected client object of game header layout component
     */
    getSelectedClientObject = (client: any) => {
        if (client) { this.openClientModal(client); }
    }

    /**
     * Get selected analysis object of game header layout component
     */
    getSelectedAnalysisObject = (analysis: any) => {
        if (analysis) { this.openAnalysisModal(analysis) }
    }

    /**
     * Get selected statistic object of game header layout component
     */
    getSelectedStatisticObject = (statistic: any) => {
        if (statistic) { this.openStatisticModal(statistic); }
    }

    /**
     * Trigger the player score of game header layout component
     */
    triggerThePlayerScore = () => {
        this.commonEmitterService.playerAmount.subscribe((playerScore: any) => {
            this.playerAmount = playerScore;
        });
    }

    /**
     * Build the game object of game header layout component
     */
    buildTheGameObject = (): any => {
        let groupName: string = "";
        if (this.status && this.status.isBot) {
            groupName = this.user[0].verifiedUser.userId + "BOT";
        }
        else {
            groupName = this.joinGroupService.getGroupName();
        }
        return {
            groupName: groupName,
            userId: this.userService.getUserId(),
            roundNo: +this.roundCount,
            amount: this.playerAmount,
            photoURL: this.userService.getUserProfilePicture(),
        }
    }

    /**
     * Trigger toggle of game header layout component
     */
    triggerToggle = () => {
        this.commonEmitterService.toggleSideNav.emit(true);
    }

    /**
     * Time ticker change output of game header layout component
     */
    timeTickerChangeOutput = () => {
        this.isFinished = true;
        this.blockUI.start("Processing.......");
        this.gameBoardService.marketBoom(this.roundCount).subscribe((response: any) => {
            if (response) {
                this.gameSubscriptor = this.gameBoardService.pushAccountDetailsOfClient(this.buildTheGameObject()).subscribe((response: any) => {
                    if (response) {
                        this.toastrService.info("Get ready for next round", "Information", this.toastConfig);
                        this.roundCount += 1;
                        if (this.roundCount > 5) {
                            this.triggerSavingPart();
                        }
                    }
                });
            }
        })
        setTimeout(() => {
            this.blockUI.stop();
            this.counter.restart()
        }, 5000);
    }

    /**
     * Trigger saving part of game header layout component
     */
    triggerSavingPart = () => {
        let groupName: string;
        setTimeout(() => {
            this.triggerToggle();
            this.counter.stop();
            this.gameBoardService.gameEndProcess();
            this.router.navigateByUrl('/dashboard/home');
            this.blockUI.stop();
            clearInterval(this.interval);
            this.roundCount = 0;
            this.botService.removeBotFromLocally();
            if (this.status && this.status.isBot) {
                groupName = this.user[0].verifiedUser.userId + "BOT";
            } else {
                groupName = this.joinGroupService.getGroupName();
            }
            this.openGameSummery(groupName);
        }, 5000);
    }


    /**
     * Hosts listener
     * @param event 
     */
    @HostListener('window:beforeunload', ['$event'])
    beforeunloadHandler(event) {
        let userId = this.userService.getUserId();
        if (this.status.isBot) {
            this.groupNameView = this.user[0].verifiedUser.userId + "BOT";
        }
        else {
            this.groupNameView = this.joinGroupService.getGroupName();
        }

        if (this.groupNameView) {
            this.gameBoardService.deleteUserGroup(userId, this.groupNameView).subscribe(res => {
                if (res) {
                    //if result is there then success
                }
            });
            this.authService.signOut();
        }
    }

    /**
     * on destroy
     */
    ngOnDestroy() {
        this.roundCount = 0;
        if (this.gameSubscriptor) {
            this.gameSubscriptor.unsubscribe();
        }
        //clearInterval(this.interval);
        this.counter.stop();
        sessionStorage.removeItem('game-started');
        sessionStorage.removeItem('joined-players');
    }
}