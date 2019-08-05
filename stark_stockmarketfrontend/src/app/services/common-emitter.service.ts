import { Injectable, EventEmitter } from '@angular/core';

@Injectable()
export class CommonEmitterService {

    /**
     * Toggle side nav of common emitter service
     */
    toggleSideNav: EventEmitter<boolean> = new EventEmitter<boolean>();
    /**
     * Emit common game data of common emitter service
     */
    emitCommonGameData: EventEmitter<any> = new EventEmitter<any>();
    /**
     * Player amount of common emitter service
     */
    playerAmount: EventEmitter<any> = new EventEmitter<any>();
    /**
     * Determines whether bot is
     */
    constructor() {
    }
}