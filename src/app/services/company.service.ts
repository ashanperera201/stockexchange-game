import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment.prod';
import { Observable } from 'rxjs';
import {BlockUI, NgBlockUI} from 'ng-block-ui';

@Injectable({
  providedIn: 'root'
})

export class CompanyService {


  /**
   * Block ui of bank service
   */
  @BlockUI() blockUI: NgBlockUI;

  /**
   * Creates an instance of company service.
   * @param http 
   */
  constructor(private httpClient: HttpClient) {
  }

  /**
   * Get company data of company service
   */
  getCompanyData = (sectorId: string): Observable<any> => {
    let url: string = environment.apiUrl + 'sectorcompany/' + sectorId;
    return this.httpClient.get(url);
  }

  /**
   * Get the Caculated Value data of User input
   */
  getCalculator = (calculator: any): Observable<any> => {
    let url: string = environment.apiUrl + 'GetCalculator';
    return this.httpClient.post(url, calculator);
  }

  /**
   * Get company analysis data of company service
   */
  getSharePriceDetails = (userID: string, companyID : string ,startDate: any, endDate: any): Observable<any> => {
    let url: string = environment.apiUrl + 'shareItems/' + userID + '/' + companyID + '/' + startDate + '/' + endDate;
    return this.httpClient.get(url);
  };

  /**
   * Get All company analysis data of company service
   */
  getAllCompanyIds = (): Observable<any> => {
    let url: string = environment.apiUrl + 'companies-all/';
    return this.httpClient.get(url);
  };
}
