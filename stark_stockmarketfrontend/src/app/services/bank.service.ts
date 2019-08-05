import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import { User } from 'firebase';
import { BlockUI, NgBlockUI } from 'ng-block-ui';

@Injectable({
  providedIn: 'root'
})
export class BankService {

  /**
   * User  of bank service
   */
  user: User;
  /**
   * Block ui of bank service
   */
  @BlockUI() blockUI: NgBlockUI;

  /**
   * Creates an instance of bank service.
   * @param http 
   */
  constructor(private http: HttpClient) {
  }

  /**
   * Get bank account details of bank service
   */
  getBankAccountDetails = (userId: string, month: number, year: number): Observable<any> => {
    let url: string = environment.apiUrl + 'bank/' + userId + '/' + month + '/' + year;
    return this.http.get(url);
  };


  /**
   * Get account balance of bank service
   */
  getAccountBalance = (userId: string): Observable<any> => {
    let url: string = environment.apiUrl + "getBankDetails?userId=" + userId
    return this.http.get(url);
  }
}
