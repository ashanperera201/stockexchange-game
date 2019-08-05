import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { UserService } from './user.service';

@Injectable()
export class DashboardService {

    /**
     * Creates an instance of dashboard service.
     * @param http 
     * @param userService 
     */
    constructor(private http: HttpClient, private userService: UserService) {
    }

    /**
     * Get common widget data of dashboard service
     */
    getCommonWidgetData = (): Observable<any> => {
        let url: string = environment.apiUrl + "getSumSectorsCompany"
        return this.http.get(url);
    }

    /**
     * Common graph data of dashboard service
     */
    commonGraphData = (): Observable<any> => {
        let url: string = environment.apiUrl + "getTopCompanyGainers"
        return this.http.get(url);
    }

    /**
     * Get top shares of dashboard service
     */
    getTopShares = () => { 
        let url:string = environment.apiUrl + "getTopShares";
        return this.http.get(url);
    }
}