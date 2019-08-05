import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

/**
 * Injectable.
 * this service only used for load common master data.
 */
@Injectable()
export class MasterService {

    constructor() { }

    /**
     * Get credit types of master service
     */
    getCreditTypes = (): Observable<any[]> => {
        let processType: any[] = [
            { id: 1, type: 'Buy' },
            { id: 2, type: 'Sell' }
        ]
        return new Observable<any[]>(obs => {
            obs.next(processType);
            obs.complete();
        });
    }

    /**
     * Get clients of master service
     */
    getClients = (): Observable<any[]> => {
        let clients: any[] = [
            { id: 1, client: 'User Portfolio' }
        ]
        return new Observable<any>(obs => {
            obs.next(clients);
            obs.complete();
        });
    }

    /**
     * Get analysis data of master service
     */
    getAnalysisData = (): Observable<any[]> => {
        let analysisMaster: any[] = [
            { id: 1, analyse: 'Company Analisys' }
        ]
        return new Observable(obs => {
            obs.next(analysisMaster);
            obs.complete();
        });
    }

    /**
     * Get statisticdata of master service
     */
    getStatisticdata = (): Observable<any[]> => {
        let statisticData: any[] = [
            { id: 1, statistic: 'calculator' }
        ]
        return new Observable(obs => {
            obs.next(statisticData);
            obs.complete();
        })
    }

    /**
     * Get port folio of master service
     */
    getPortFolio = (): Observable<any[]> => {
        let portFolio: any[] = [
            { id: 2, value: "Company Share" }
        ]
        return new Observable(obs => {
            obs.next(portFolio);
            obs.complete();
        });
    }

    /**
     * Get system data of master service
     */
    getSystemData = (): Observable<any[]> => {
        let systemData: any[] = [
            { id: 1, value: "Top 10 Gainers" },
            { id: 2, value: "Top 10 Losers" },
            { id: 3, value: "Top 10 Share Value" },
        ]
        return new Observable(obs => {
            obs.next(systemData);
            obs.complete();
        });
    }

    /**
     * Get months of master service
     */
    getViewMonthList = (): any[] => {
        return [
            { month: 1, viewMonth: 'January' },
            { month: 2, viewMonth: 'February' },
            { month: 3, viewMonth: 'March' },
            { month: 4, viewMonth: 'April' },
            { month: 5, viewMonth: 'May' },
            { month: 6, viewMonth: 'June' },
            { month: 7, viewMonth: 'July' },
            { month: 8, viewMonth: 'August' },
            { month: 9, viewMonth: 'September' },
            { month: 10, viewMonth: 'October' },
            { month: 11, viewMonth: 'November' },
            { month: 12, viewMonth: 'December' }
        ];
    }

    /**
     * Get view year list of master service
     */
    getViewYearList = (): any[] => {
        return [
            { year: 2019, viewYear: '2019' },
            { year: 2020, viewYear: '2020' },
            { year: 2021, viewYear: '2021' },
            { year: 2022, viewYear: '2022' }
        ];
    }
}