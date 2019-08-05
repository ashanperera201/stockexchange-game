import { Component, OnInit, NgZone, ViewChild, HostListener } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { UserService } from '../services/user.service';
import { CommonEmitterService } from '../services/common-emitter.service';
import { GameBoardService } from '../services/game-board.service';
import { MatSidenav } from '@angular/material';
import { Router } from '@angular/router';
import { JoinGroupService } from '../services/join-group.service';

@Component({
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.scss']
})

export class DashabordComponent implements OnInit {
    @ViewChild('sidenav', { static: true }) drawer: MatSidenav;
    navbarOpen = false;
    width: any;
    height: any;
    mode: string = 'side';
    opened = true;
    user: any;
    isGameStarted: boolean = true;
    isNavHidden: boolean = false;

    /**
     * Creates an instance of dashabord component.
     * @param _ngZone 
     * @param authService 
     * @param userService 
     * @param commonEmitterService 
     * @param gameBoardService 
     */
    constructor(_ngZone: NgZone, private authService: AuthService, private userService: UserService, private commonEmitterService: CommonEmitterService, private gameBoardService: GameBoardService, private router: Router, private joinGroupService: JoinGroupService) {
        this.changeMode()
        this.gameBoardService.changeRouteOfTheGame();
        window.onresize = (e) => {
            _ngZone.run(() => {
                this.changeMode();
            })
        }
    }

    /**
     * on init
     */
    ngOnInit() {
        this.setUserInToolbar();
        this.isGameStartedYet();
        this.changeNavigation();
    }

    /**
     * Toggle navbar of dashabord component
     */
    toggleNavbar = () => {
        this.navbarOpen = !this.navbarOpen;
    }

    /**
     * Change mode of dashabord component
     */
    changeMode = () => {
        this.width = window.innerWidth;
        this.height = window.innerHeight;
        if (!this.isNavHidden) {
            if (this.width <= 1120) {
                this.mode = 'over';
                this.opened = false;
            }
            else if (this.width > 1120) {
                this.mode = 'side';
                this.opened = true;
            }
        }
    }

    /**
     * Change navigation of dashabord component
     */
    changeNavigation = () => {
        this.commonEmitterService.toggleSideNav.subscribe((isOpened: boolean) => {
            if (!isOpened) {
                this.isNavHidden = true;
                this.isGameStarted = isOpened;
                this.mode = 'over';
                this.opened = false;
            } else {
                this.isNavHidden = false;
                this.isGameStarted = isOpened;
                this.mode = 'side';
                this.opened = true;
            }
        })
    }

    /**
     * Set user in toolbar of dashabord component
     */
    setUserInToolbar = () => {
        let userProfile: any[] = this.userService.getProfile()
        if (userProfile.length != 0) {
            this.user = userProfile[0];
        }
    }

    /**
     * Logout  of dashabord component
     */
    logout = () => {
        if (!this.isGameStarted) {
            let groupName: string;
            let botGroup: string;

            botGroup = JSON.parse(sessionStorage.getItem("bot-group"));
            if (botGroup) {
                groupName = botGroup;
            } else {
                groupName = this.joinGroupService.getGroupName();
            }

            let userId = this.userService.getUserId();
            this.gameBoardService.deleteUserGroup(userId, groupName).subscribe(res => {
                if (res) {
                    //successfully deleted. if result is there.
                }
            });
        }
        this.authService.signOut();
    }

    /**
     * Determines whether game started yet is
     */
    isGameStartedYet = () => {
        if (this.gameBoardService.isGameStarted()) {
            this.isGameStarted = false;
            this.mode = 'over';
            this.opened = false;
        }
    }

    /**
     * Trigger responsive close of dashabord component
     */
    triggerResponsiveClose = () => {
        this.width = window.innerWidth;
        this.height = window.innerHeight;
        if (this.width <= 1120) {
            this.drawer.close();
        }
    }

    /**
     * Trigger bot player of dashabord component
     */
    triggerBotPlayer = () => {
        sessionStorage.setItem('game-started', JSON.stringify({ isStarted: true, isBot: true }));
        this.commonEmitterService.toggleSideNav.emit(false);
        this.router.navigate(['/dashboard/game-board/landing']);
    }

    /**
     * Hosts listener
     * @param event 
     */
    @HostListener('window:beforeunload', ['$event'])
    beforeunloadHandler(event) {
        this.authService.signOut();
    }
}
