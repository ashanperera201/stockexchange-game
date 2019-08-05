import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment.prod';
import { Observable } from 'rxjs';

@Injectable()
export class BotService {

    /**
     * Creates an instance of bot service.
     * @param http 
     */
    constructor(private http: HttpClient) {
    }

    /**
     * Save bot local storage of bot service
     */
    saveBotSessionStorage = () => {
        sessionStorage.setItem('bot-saved', JSON.stringify({ botSaved: true }));
    }

    /**
     * Get bot from locally of bot service
     */
    getBotFromLocally = () => {
        let botSaved = JSON.parse(sessionStorage.getItem('bot-saved'));
        if (botSaved) {
            return true;
        }
        return false;
    }

    /**
     * Add bot group name of bot service
     */
    addBotGroupName = (groupName) => {
        sessionStorage.setItem("bot-group", JSON.stringify(groupName));
    }

    /**
     * Remove bot from locally of bot service
     */
    removeBotFromLocally = () => {
        sessionStorage.removeItem("bot-saved");
        return true;
    }

    /**
     * Add post game of bot service
     */
    addPostGame = (payload): Observable<any> => {
        let url: string = environment.apiUrl + 'AddBotGame';
        return this.http.post(url, payload);
    }

    /**
     * Execute bot of bot service
     */
    executeBot = (payload): Observable<any> => {
        let url: string = environment.apiUrl + 'BotExecute';
        return this.http.post(url, payload);
    }

}