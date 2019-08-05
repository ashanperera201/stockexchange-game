import { Injectable } from '@angular/core';
import { AngularFireDatabase } from '@angular/fire/database';
import { Observable } from 'rxjs';

@Injectable()
export class FirebaseService {

    /**
     * Creates an instance of firebase service.
     * @param angularFireDatabase 
     */
    constructor(private angularFireDatabase: AngularFireDatabase) { }
    
    /**
     * Get group data of firebase service
     */
    getGroupData = (): Observable<any> => {
        return this.angularFireDatabase.list('/joinGroup').snapshotChanges();
    }

    /**
     * Push to queue join group data of firebase service
     */
    pushToQueueJoinGroupData = (joinGroupData: any): Promise<any> => {
        return this.angularFireDatabase.list('/joinGroup').push(joinGroupData);
    }

    /**
     * Start game request of firebase service
     */
    startGameRequest = (payload: any): Promise<any> => {
        return this.angularFireDatabase.list('/game').push(payload);
    }

    /**
     * Get game request of firebase service
     */
    getGameRequest = (): Observable<any> => {
        return this.angularFireDatabase.list('/game').snapshotChanges();
    }

    /**
     * Update game request of firebase service
     */
    updateGameRequest = (key$: any, payload: any): Promise<any> => {
        return this.angularFireDatabase.list('/game').update(key$, payload);
    }

    /**
     * Clean game once started of firebase service
     */
    cleanGameOnceStarted = (key$: any) => {
        return this.angularFireDatabase.list('/game').remove(key$);
    }

    /**
     * Delete the group of firebase service
     */
    deleteTheGroup = (key$: any) => {
        return this.angularFireDatabase.list('/joinGroup').remove(key$);
    }
}