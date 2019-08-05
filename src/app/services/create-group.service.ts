import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class CreateGroupService {

    constructor(private http: HttpClient) {
    }

    /**
     * Create group of create group service
     */
    createGroup = (createGroup: any): Observable<any> => {
        let url: string = environment.apiUrl + 'saveGroup';
        return this.http.post(url, createGroup);
    }
}
