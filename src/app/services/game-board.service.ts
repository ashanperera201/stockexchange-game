import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { UserService } from './user.service';

@Injectable({ providedIn: "root" })
export class GameBoardService {

    /**
     * Creates an instance of game board service.
     * @param http 
     * @param router 
     * @param userService 
     */
    constructor(private http: HttpClient, private router: Router, private userService: UserService) { }

    /**
     * Determines whether game started is
     */
    isGameStarted = (): boolean => {
        let gameStatus: any = JSON.parse(sessionStorage.getItem('game-started'));
        if (gameStatus) {
            return gameStatus.isStarted;
        }
        return null;
    }

    /**
     * Get game status of game board service
     */
    getGameStatus = () => {
        let gameStatus: any = JSON.parse(sessionStorage.getItem('game-started'));
        if (gameStatus) return gameStatus;
        return null;
    }

    /**
     * Change route of the game of game board service
     */
    changeRouteOfTheGame = () => {
        if (this.isGameStarted()) {
            this.router.navigate(['/dashboard/game-board/landing']);
        }
    }

    /**
     * Game end process of game board service
     */
    gameEndProcess = () => {
        sessionStorage.removeItem('game-started');
    }

    /**
     * Get game common data of game board service
     */
    getGameCommonData = (userId: string): Observable<any> => {
        let url: string = environment.apiUrl + "BasicData-all?userId=" + userId;
        return this.http.get(url);
    }

    /**
     * Get game shares of game board service
     */
    getGameShares = (): Observable<any> => {
        let url: string = environment.apiUrl + "share-all";
        return this.http.get(url);
    }

    /**
     * Process share game update of game board service
     */
    processShareGameUpdate = (playerProcessedData: any): Observable<any> => {
        let url: string = environment.apiUrl + "shareUpdate";
        return this.http.post(url, playerProcessedData);
    }

    /**
     * Get user portfolio of game board service
     */
    getUserPortfolio = (userId: string): Observable<any> => {
        let url: string = environment.apiUrl + "GetUserPortfolio?userId=" + userId;
        return this.http.get(url);
    }

    /**
     * Save user portfolio of game board service
     */
    saveUserPortfolio = (userPortfolioPayload: any): Observable<any> => {
        let url: string = environment.apiUrl + "saveUserPortfoloio";
        return this.http.post(url, userPortfolioPayload);
    }

    /**
     * Push account details of client of game board service
     */
    pushAccountDetailsOfClient = (accountPayload: any): Observable<any> => {
        let url: string = environment.apiUrl + "updateGame";
        return this.http.put(url, accountPayload);
    }

    /**
     * Market boom of game board service
     */
    marketBoom = (roundNo: number): Observable<any> => {
        let url: string = environment.apiUrl + "marketBoom?round=" + roundNo;
        return this.http.get(url)
    }

    /**
     * Get summery details of game board service
     */
    getSummeryDetails = (groupName: any): Observable<any> => {
        let url: string = environment.apiUrl + "gameSummary?groupName=" + groupName;
        return this.http.get(url);
    }

    /**
     * Load sector grid data of game board service
     */
    loadSectorGridData = (sectorId: any): Observable<any> => {
        let url: string = environment.apiUrl + "getSectorCompany?sector=" + sectorId;
        return this.http.get(url);
    }

    /**
     * Load port folio grid data of game board service
     */
    loadPortFolioGridData = (portFolioId: any): Observable<any> => {
        let url: string = environment.apiUrl;
        if (portFolioId == 1)
            url = url.concat("getShareSummarySector");
        else if (portFolioId == 2)
            url = url.concat("getShareSummaryCompany");
        return this.http.get(url);
    }

    /**
     * Load system grid data of game board service
     */
    loadSystemGridData = (systemId: any): Observable<any> => {
        let url: string = environment.apiUrl;
        if (systemId == 1)
            url = url.concat("getTopCompanyGainers");
        else if (systemId == 2)
            url = url.concat("getTopCompanyLoosers");
        else if (systemId == 3)
            url = url.concat("getTopShares");
        return this.http.get(url);
    }

    /**
     * Get all companies of game board service
     */
    getAllCompanies = (): Observable<any> => {
        let url: string = environment.apiUrl + "companies-all"
        return this.http.get(url);
    }

    /**
     * Delete obsolete of game board service
     */
    deleteObsolete = (groupName: any): Observable<any> => {
        let url: string = environment.apiUrl + "obsoleteGame?groupName=" + groupName;
        return this.http.delete(url);
    }

    /**
     * Delete user group of game board service
     */
    deleteUserGroup = (userId: any, groupName: string): Observable<any> => {
        let url: string = environment.apiUrl + "obsoleteGameUser?groupName=" + groupName + "&User=" + userId
        return this.http.delete(url);
    }
}