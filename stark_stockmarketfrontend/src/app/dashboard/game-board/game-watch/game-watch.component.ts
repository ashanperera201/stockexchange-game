import { Component, OnInit } from '@angular/core';
import { CommonEmitterService } from '../../../services/common-emitter.service';
import { MasterService } from '../../../services/master/master.data.service';
import { switchMap } from 'rxjs/operators/switchMap';
import { GameBoardService } from '../../../services/game-board.service';
import { DataStateChangeEvent, GridDataResult } from '@progress/kendo-angular-grid';
import { State, process } from '@progress/kendo-data-query';

@Component({
    selector: 'game-watch-layout',
    templateUrl: './game-watch.component.html',
    styleUrls: ['./game-watch.component.scss']
})

export class GameWatchComponent implements OnInit {

    isWatchDropDownDisabled: boolean = true;
    systemDataArray: any[] = [];
    portFolioDataArray: any[] = [];
    commonGameData: any;
    isSector: boolean = false;
    isPortFolio: boolean = false;
    isSystem: boolean = false;
    sector: any[];
    portFolio: any[];
    system: any[];

    //grid configs
    state: State = {
        skip: 0,
        take: 5
    };
    buttonCount = 5;
    info = true;
    type: 'numeric' | 'input' = 'numeric';
    pageSizes = true;
    previousNext = true;


    /**
     * Grid data for sector of game watch component
     */
    gridDataForSector: GridDataResult = process(this.sector ? this.sector : [], this.state);
    /**
     * Grid data for portfolio of game watch component
     */
    gridDataForPortfolio: GridDataResult = process(this.portFolio ? this.portFolio : [], this.state);
    /**
     * Grid data for system of game watch component
     */
    gridDataForSystem: GridDataResult = process(this.system ? this.system : [], this.state);

    /**
   * Creates an instance of game watch component.
   * @param commonEmitterService 
   * @param masterService 
   * @param gameBoardService 
   */
    constructor(private commonEmitterService: CommonEmitterService, private masterService: MasterService, private gameBoardService: GameBoardService) { }

    /**
     * on init
     */
    ngOnInit() {
        this.loadAllCompanies();
        this.getCommonGameData();
        this.getMasterData();
    }

    loadAllCompanies = () => {
        this.isSector = true;
        this.isPortFolio = false;
        this.isSystem = false;
        this.gameBoardService.getAllCompanies().subscribe((companyAll: any[]) => {
            this.sector = [];
            if (companyAll) { 
                this.sector = companyAll;
                this.gridDataForSector = process(this.sector, this.state);
            }
        })
    }

    /**
     * Get common game data of game watch component
     */
    getCommonGameData = () => {
        this.commonEmitterService.emitCommonGameData.subscribe((serviceResponse: any) => {
            if (serviceResponse) {
                this.commonGameData = serviceResponse;
            }
        });
    }

    /**
     * Get master data of game watch component
     */
    getMasterData = () => {
        this.masterService.getSystemData().pipe(switchMap((systemData: any[]) => {
            this.systemDataArray = systemData
            return this.masterService.getPortFolio()
        })).subscribe((portFolioData: any[]) => {
            this.portFolioDataArray = portFolioData;
        });
    }

    /**
     * Trigger value change for sector drop down of game watch component
     */
    triggerValueChangeForSectorDropDown = ($key: any) => {
        this.isSector = true;
        this.isPortFolio = false;
        this.isSystem = false;
        this.gameBoardService.loadSectorGridData($key).subscribe((res: any[]) => {
            this.sector = [];
            if (res && res.length != 0) {
                this.sector = res;
                this.gridDataForSector = process(this.sector, this.state);
            }
        });
    }

    /**
     * Trigger value change for port folio of game watch component
     */
    triggerValueChangeForPortFolio = ($key: any) => {
        this.isSector = false;
        this.isPortFolio = true;
        this.isSystem = false;
        this.gameBoardService.loadPortFolioGridData($key).subscribe((res: any[]) => {
            this.portFolio = [];
            if (res && res.length != 0) {
                this.portFolio = res;
                this.gridDataForPortfolio = process(this.portFolio, this.state);
            }
        });
    }

    /**
     * Trigger value change for system drop down of game watch component
     */
    triggerValueChangeForSystemDropDown = ($key: any) => {
        this.isSector = false;
        this.isPortFolio = false;
        this.isSystem = true;
        this.gameBoardService.loadSystemGridData($key).subscribe((res: any[]) => {
            this.system = [];
            if (res && res.length != 0) {
                this.system = res;
                this.gridDataForSystem = process(this.system, this.state);
            }
        });
    }

    /**
     * Sectors data change event
     * @param state 
     */
    sectorDataChangeEvent(state: DataStateChangeEvent): void {
        this.state = state;
        this.gridDataForSector = process(this.sector, this.state);
    }

    /**
     * Portfolios data change event
     * @param state 
     */
    portfolioDataChangeEvent(state: DataStateChangeEvent): void {
        this.state = state;
        this.gridDataForPortfolio = process(this.portFolio, this.state);
    }

    /**
     * Systems data change event
     * @param state 
     */
    systemDataChangeEvent(state: DataStateChangeEvent): void {
        this.state = state;
        this.gridDataForSystem = process(this.system, this.state);
    }
}