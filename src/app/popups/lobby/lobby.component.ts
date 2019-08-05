import { Component, OnInit, Inject, OnDestroy, HostListener } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FirebaseService } from '../../services/firebase/firebase.service';
import { ToastrService } from 'ngx-toastr';
import { NgBlockUI, BlockUI } from 'ng-block-ui';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';
import { CommonEmitterService } from '../../services/common-emitter.service';
import { JoinGroupService } from '../../services/join-group.service';
import { GroupRequestPayload } from '../../models/join-group.model';

@Component({
    selector: 'game-lobby',
    templateUrl: './lobby.component.html',
    styleUrls: ['./lobby.component.scss']
})

export class LobbyComponent implements OnInit, OnDestroy {
    @BlockUI() blockUI: NgBlockUI;
    dialogData: any;
    joinedPlayers: any[];
    creatorName: any;
    groupData: any;
    isCreator: boolean = false;
    groupRequestPayload: GroupRequestPayload[] = [];
    groupKeys: any[] = [];

    botPayload: any = {
        isBot: true
    }

    /**
    * Creates an instance of lobby component.
    * @param dialogRef 
    * @param data 
    * @param firebaseService 
    * @param toastrService 
    * @param userService 
    * @param router 
    * @param commonEmitterService 
    * @param joinGroupService 
    */
    constructor(public dialogRef: MatDialogRef<LobbyComponent>, @Inject(MAT_DIALOG_DATA) public data: any, private firebaseService: FirebaseService, private toastrService: ToastrService, private userService: UserService, private router: Router, private commonEmitterService: CommonEmitterService, private joinGroupService: JoinGroupService) {
        //join process
        dialogRef.disableClose = true;
        this.blockUI.start('Please wait,loading ............');
        this.letsJoinToTheGroup(data);
        this.blockUI.stop();
        this.haveYouStartedTheGame();
    }

    /**
     * on init
     */
    ngOnInit() { }

    /**
     * Lets join to the group of lobby component
     */
    letsJoinToTheGroup = (data) => {
        if (data) {
            this.groupData = data.groupData;
            this.firebaseService.pushToQueueJoinGroupData(this.groupData).then((dataRes: any) => {
                if (dataRes)
                    this.firebaseService.getGroupData().subscribe((tunnelResponse: any[]) => {
                        if (tunnelResponse) {
                            this.joinedPlayers = [];
                            this.groupRequestPayload = [];
                            tunnelResponse.forEach(response => {
                                if (this.groupData.groupName == response.payload.val().groupName) {
                                    this.joinedPlayers.push(response.payload.val());
                                    this.groupKeys.push(response.key);
                                    this.createObjectArray(response.payload.val());
                                }
                            })
                        }
                        this.whoIsCreator();
                    })
            })
        }
        else {
            this.toastrService.error('Error in joining to the game.', 'Error')
        }
    }

    /**
     * Lets play the game of lobby component
     */
    letsPlayTheGame = () => {
        this.firebaseService.startGameRequest(this.buildRequestGamePayload(true)).then((res: any) => {
            if (res) {
                this.joinGroupService.saveJoinedPlayers(this.groupRequestPayload).subscribe((serviceResponse: any) => {
                    //clean the game table from here
                    if (serviceResponse) {
                        //delete the groups
                        this.deleteGroupDataFromFirebase();
                        this.joinGroupService.deleteGroupFromApiEnd(this.groupData.groupName).subscribe((response: any) => {
                            if (response) {
                                this.toastrService.success("Let's join to the game", "Get Ready");
                            }
                        });
                    } else {
                        this.toastrService.error("Error in server side.", "Error");
                    }
                });
            }
        });
    }

    /**
     * Who is creator of lobby component
     */
    whoIsCreator = () => {
        let userId = this.userService.getUserId();
        if (userId && this.joinedPlayers.length != 0) {
            this.creatorName = this.joinedPlayers[0].creatorName;
            if (this.joinedPlayers.every(x => x.creatorId === userId)) {
                this.isCreator = true;
            }
            else {
                this.isCreator = false;
            }
        }
    }

    /**
     * Determines whether you started the game have
     */
    haveYouStartedTheGame = () => {
        this.firebaseService.getGameRequest().subscribe((response: any[]) => {
            let key$ = response.filter(x => x.payload.val().groupName == this.groupData.groupName && x.payload.val().isActive).map(x => x.key).toString()
            if (key$) {
                this.firebaseService.updateGameRequest(key$, this.buildRequestGamePayload(false)).then(() => {
                    this.dialogRef.disableClose = false;
                    this.dialogRef.close();
                    this.commonEmitterService.toggleSideNav.emit(false);                    
                    sessionStorage.setItem('game-started', JSON.stringify({ isStarted: true, isBot: false }));
                    this.router.navigate(['/dashboard/game-board/landing']);
                });
            }
        });
    }

    /**
     * Build request game payload of lobby component
     */
    buildRequestGamePayload = (active: boolean) => {
        return {
            groupName: this.groupData.groupName,
            players: this.groupData.players,
            gameStarted: true,
            isActive: active
        }
    }

    /**
     * Create object array of lobby component
     */
    createObjectArray = (payload: any) => {
        if (payload) {
            let groupRequestObject: GroupRequestPayload = new GroupRequestPayload();
            //--------------------------------------------------------------------
            groupRequestObject.groupName = payload.groupName;
            groupRequestObject.userId = payload.joinerId;
            groupRequestObject.roundNo = 0;
            groupRequestObject.amount = 0;
            groupRequestObject.photoURL = payload.profileUrl;
            //---------------------------------------------------------------------
            this.groupRequestPayload.push(groupRequestObject);
            sessionStorage.setItem('joined-players', JSON.stringify(this.groupRequestPayload));
        }
    }

    /**
     * Delete group data from firebase of lobby component
     */
    deleteGroupDataFromFirebase = () => {
        if (this.groupKeys && this.groupKeys.length != 0) {
            this.groupKeys.forEach(keyElement => {
                this.firebaseService.deleteTheGroup(keyElement)
            });
        }
    }

     /**
     * Hosts listener
     * @param event 
     */
    @HostListener('window:beforeunload', ['$event'])
    beforeunloadHandler(event) {
        this.deleteGroupDataFromFirebase();        
    }

    /**
     * on destroy
     */
    ngOnDestroy() {
        this.deleteGroupDataFromFirebase();
    }
}
