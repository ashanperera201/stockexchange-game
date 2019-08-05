import { Component, OnDestroy } from '@angular/core';
import { AuthService } from './services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnDestroy {
  /**
   * Creates an instance of app component.
   * @param authService 
   */
  constructor(private router: Router, private authService: AuthService) {
  }

  /**
   * on destroy
   */
  ngOnDestroy() {
    this.authService.signOut();
    this.router.navigate(['/auth/login']);
  }
}
