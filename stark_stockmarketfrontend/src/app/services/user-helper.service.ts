import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

@Injectable()
export class UserHelperService {
    /**
     * Creates an instance of user helper service.
     * @param http 
     */
    constructor(private http: HttpClient) {
    }

    /**
     * Get user helper data of user helper service
     */
    getUserHelperData = (): Observable<any> => {
        let url: string = environment.apiUrl + 'helpText'
        return this.http.get(url)
    }
}