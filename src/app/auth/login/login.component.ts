import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { BlockUI, NgBlockUI } from 'ng-block-ui';
@Component({
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})

export class LoginComponent implements OnInit {

    isRegister: boolean = false;
    @BlockUI() blockUI: NgBlockUI;

    /**
     * Creates an instance of login component.
     * @param authService 
     * @param router 
     */
    constructor(private authService: AuthService, private router: Router) {
        this.triggerAuthorization();
    }

    /**
     * on init
     */
    ngOnInit() {
    }

    /**
     * Trigger authorization of login component
     */
    triggerAuthorization = () => {
        this.blockUI.start('Please wait authorizing.......');
        setTimeout(() => {
            this.authService.isUserLoggedIn().subscribe(isLogged => {
                if (isLogged) {
                    this.router.navigate(['/dashboard/home']);
                } else {
                    this.router.navigate(['auth/login']);
                }
            })
            this.blockUI.stop()
        }, 1000);
    }

    /**
     * Trigger css for register of login component
     */
    triggerCssForRegister = (): any => {
        if (this.isRegister) {
            return ['login__register-mode'];
        }
    }

    /**
     * Trigger back event of login component
     */
    triggerBackEvent = (isBack) => {
        this.isRegister = isBack;
    }

    /**
     * Login with google of login component
     */
    loginWithGoogle = () => {
        this.authService.googleAuthentication();
    }

    /**
     * Sign up of login component
     */
    signUp = () => {
        this.isRegister = !this.isRegister;
    }
}
