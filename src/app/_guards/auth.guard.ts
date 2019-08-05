import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth.service';

@Injectable()
export class AuthGuard implements CanActivate {

    /**
     * Creates an instance of auth guard.
     * @param authService 
     * @param router 
     */
    constructor(private authService: AuthService, private router: Router) { }

    /**
     * Determines whether activate can
     * @returns activate 
     */
    canActivate(): Observable<boolean> {
        return new Observable(obs => {
            this.authService.isUserLoggedIn().subscribe(res => {
                if (res) {
                    obs.next(true);
                    obs.complete();
                } else {
                    this.router.navigate(['/auth/login']);
                    obs.next(false);
                    obs.complete();
                }
            }, error => {
                this.router.navigate(['/auth/login']);
                obs.next(false);
                obs.complete();
            })
        });
    }
}
