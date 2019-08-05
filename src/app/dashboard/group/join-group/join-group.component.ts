import { Component, OnInit } from '@angular/core';
import { JoinGroupService } from '../../../services/join-group.service';
import { NgBlockUI, BlockUI } from 'ng-block-ui';
import { MatDialog } from '@angular/material/dialog';
import { LobbyComponent } from '../../../popups/lobby/lobby.component';
import { GroupInfo } from '../../../models/join-group.model';
import { UserService } from '../../../services/user.service';
import { FirebaseService } from '../../../services/firebase/firebase.service';
import { GameBoardService } from '../../../services/game-board.service';

@Component({
    templateUrl: './join-group.component.html',
    styleUrls: ['./join-group.component.scss']
})

export class JoinGroupComponent implements OnInit {
    @BlockUI() blockUI: NgBlockUI;
    public gridData: any[];
    private groupInfo: GroupInfo = new GroupInfo();
    groupResponse: any;
    isGroupPacked: boolean = false;
    groupName: string;
    user: any;

    /**
     * Creates an instance of join group component.
     * @param joinGroupService 
     * @param dialog 
     * @param userService 
     * @param firebaseService 
     * @param gameBoardService 
     */
    constructor(private joinGroupService: JoinGroupService, public dialog: MatDialog, private userService: UserService, private firebaseService: FirebaseService, private gameBoardService: GameBoardService) {
        this.gameBoardService.changeRouteOfTheGame();
        this.getGroupData();
    }

    /**
     * on init
     */
    ngOnInit() {
        this.loadCreatedGroups();
        this.loadUser();
    }

    /**
     * Load created groups of join group component
     */
    loadCreatedGroups = () => {
        this.blockUI.start()
        this.joinGroupService.getCreatedGroups().subscribe((groups: any[]) => {
            if (groups && groups.length != 0) {
                this.gridData = groups;
                this.getGroupData();
                this.blockUI.stop();
            }
            this.blockUI.stop();
        }, error => { this.blockUI.stop() })
    }

    /**
     * Open lobby of join group component
     */
    openLobby = (dataItem: any): void => {
        if (dataItem) {
            let modalReference = this.dialog.open(LobbyComponent, {
                width: '60em',
                height: '30em',
                data: { groupData: this.buildObject(dataItem) }
            });
        }
    }

    /**
     * Build object of join group component
     */
    buildObject = (dataItem) => {
        let user = this.userService.getVerifiedUser()
        //-------------------------------------------
        this.groupInfo.creatorName = dataItem.userName;
        this.groupInfo.creatorId = dataItem.userId;
        this.groupInfo.groupName = dataItem.groupName;
        this.groupInfo.joinerName = user.userName;
        this.groupInfo.joinerId = user.userId;
        this.groupInfo.isActive = true;
        this.groupInfo.players = dataItem.players;
        this.groupInfo.profileUrl = this.user[0].photoURL;
        //-----------------------------------------------
        return this.groupInfo;
    }

    /**
     * Get group data of join group component
     */
    getGroupData = () => {
        let noOfMembers: number = 0
        let groupRes: any;
        this.firebaseService.getGameRequest().subscribe((groupResponse: any) => {
            if (groupResponse && this.gridData) {
                this.gridData.forEach(group => {
                    groupResponse.forEach(response => {
                        groupRes = response.payload.val()
                        if (group.groupName === groupRes.groupName) {
                            noOfMembers += 1;
                        }
                    })
                })
            }
        })
    }

   /**
    * Load user of join group component
    */
   loadUser = () => {
        this.user = this.userService.getProfile();
    }
}