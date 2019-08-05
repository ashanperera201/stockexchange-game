import { Injectable, NgZone } from '@angular/core';
import { Router } from '@angular/router';
import { AngularFireAuth } from '@angular/fire/auth';
import * as firebase from 'firebase';
import { User } from 'firebase';
import { BlockUI, NgBlockUI } from 'ng-block-ui';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';

/**
 * Injectable
 */
@Injectable({ providedIn: 'root' })
export class AuthService {
    /**
     * User  of auth service
     */
    user: User
    /**
     * Block ui of auth service
     */
    @BlockUI() blockUI: NgBlockUI

    /**
     * Creates an instance of auth service.
     * @param angularFireAuth 
     * @param router 
     * @param ngZone 
     * @param httpClient 
     * @param toastrService 
     */
    constructor(private angularFireAuth: AngularFireAuth, private router: Router, private ngZone: NgZone, private httpClient: HttpClient, private toastrService: ToastrService) {
    }

    /**
     * Google authentication of auth service
     */
    googleAuthentication = () => {
        this.angularFireAuth.auth.signInWithPopup(new firebase.auth.GoogleAuthProvider).then((success: any) => {
            this.blockUI.start('Authenticating, please wait .......');
            this.loggedUser(success.user.email).subscribe((verifiedUser: any) => {
                if (verifiedUser && verifiedUser.userId) {
                    this.ngZone.run(() => {
                        sessionStorage.setItem("access_token", success.credential.accessToken);
                        this.setUserProfile(success.user.providerData, verifiedUser);
                        this.blockUI.stop();
                        this.router.navigate(['/dashboard/home']);
                    })
                }
                else {
                    this.toastrService.error('Your not authorized,please sign up', 'Error', { closeButton: true });
                    this.blockUI.stop();
                    this.router.navigate(['/auth/login']);
                }
            }, error => {
                this.blockUI.stop();
                this.router.navigate(['/auth/login']);
            })
        })
    }

    /**
     * Login via user email password of auth service
     */
    loginViaUserEmailPassword = (email: string, passowrd: string) => {
        let result = this.angularFireAuth.auth.createUserWithEmailAndPassword(email, passowrd);
        this.router.navigate(['/dashboard/home']);
    }

    /**
     * Determines whether user logged in is
     */
    isUserLoggedIn = (): Observable<boolean> => {
        return new Observable(obs => {
            this.getGoogleLoggedInUser().subscribe((user: any) => {
                if (user && user.providerData) {
                    let access_token = sessionStorage.getItem('access_token');
                    let user_session = sessionStorage.getItem('user-profile');
                    if (access_token && user_session) {
                        obs.next(true);
                        obs.complete();
                    } else { 
                        obs.next(false);
                        obs.complete();
                    }
                }
                else {
                    obs.next(false);
                    obs.complete();
                }
            });
        });
    }

    /**
     * Get google logged in user of auth service
     */
    getGoogleLoggedInUser = (): Observable<any> => {
        return this.angularFireAuth.authState;
    }

    /**
     * Sign out of auth service
     */
    signOut = () => {
        this.blockUI.start('Logging out, please wait .......');
        //----------------------------------------------------------------------
        let url: string = environment.apiUrl + "logoutService";
        let user: any = JSON.parse(sessionStorage.getItem('user-profile'))[0].verifiedUser;
        //----------------------------------------------------------------------       

        this.httpClient.post(url, user).subscribe((response: any) => {
            if (response) {
                this.angularFireAuth.auth.signOut();
                sessionStorage.removeItem('access_token');
                sessionStorage.removeItem('user-profile');
                sessionStorage.removeItem('game-started');
                sessionStorage.removeItem('joined-players');
                sessionStorage.removeItem('bot-saved');
                sessionStorage.removeItem('bot-group');
                setTimeout(() => {
                    this.blockUI.stop();
                    this.router.navigate(['/auth/login']);
                }, 1000)
            } else {
                this.toastrService.error('Error in sign out', 'Error');
                this.blockUI.stop();
            }
        }, error => {
            this.toastrService.error('Error in sign out', 'Error');
            this.blockUI.stop();
        })
    }

    /**
     * Register user of auth service
     */
    registerUser = (user: any): Observable<any> => {
        let url: string = environment.apiUrl + "user";
        return this.httpClient.post(url, user);
    }

    /**
     * Logged user of auth service
     */
    loggedUser = (email: string): Observable<any> => {
        let url: string = environment.apiUrl + "email?email=" + email;
        return this.httpClient.get(url);
    }

    /**
     * Set user profile of auth service
     */
    setUserProfile = (user: any, verifiedUser: any) => {
        let userResult: any[] = user;
        userResult.forEach(result => {
            result["verifiedUser"] = verifiedUser;
        })
        sessionStorage.setItem('user-profile', JSON.stringify(userResult));
    }
}
