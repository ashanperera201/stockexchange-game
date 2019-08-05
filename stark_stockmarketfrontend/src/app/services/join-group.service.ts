import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

@Injectable()
export class JoinGroupService {
    /**
     * Creates an instance of join group service.
     * @param http 
     */
    constructor(private http: HttpClient) { }

    /**
     * Get created groups of join group service
     */
    getCreatedGroups = (): Observable<any> => {
        let url: string = environment.apiUrl + 'groups-all';
        return this.http.get(url);
    }

    /**
     * Save joined players of join group service
     */
    saveJoinedPlayers = (joinedPlayers: any): Observable<any> => {
        let url: string = environment.apiUrl + 'saveGame';
        return this.http.post(url, joinedPlayers);
    }

    /**
     * Get group name of join group service
     */
    getGroupName = () => {
        let groupResponse: any = JSON.parse(sessionStorage.getItem('joined-players'));
        if (groupResponse) {
            return groupResponse[0].groupName;;
        }
        return null;
    }

    /**
     * Delete group from api end of join group service
     */
    deleteGroupFromApiEnd = (joinedPlayers: any): Observable<any> => {
        let url: string = environment.apiUrl + 'deleteGroup';
        return this.http.post(url, joinedPlayers);
    }

}