import { Component, OnInit, OnDestroy } from '@angular/core';
import * as moment from 'moment';
import { MasterService } from '../../../services/master/master.data.service';
import { NgBlockUI, BlockUI } from 'ng-block-ui';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { GameBoardService } from '../../../services/game-board.service';
import { ToastrService } from 'ngx-toastr';
import { CommonEmitterService } from '../../../services/common-emitter.service';
import { timer } from 'rxjs/observable/timer';
import { Subscription } from 'rxjs';
import { UserService } from '../../../services/user.service';
import { switchMap } from 'rxjs/operators/switchMap';

@Component({
    selector: 'game-middle-layout',
    templateUrl: './game-middle.component.html',
    styleUrls: ['./game-middle.component.scss']
})

export class GameMiddleLayoutComponent implements OnInit, OnDestroy {

    //-------------------------------------Pre-defined variables that need for full fill the requirement.
    @BlockUI() blockUI: NgBlockUI;
    shareTickerSubscriptor: Subscription;
    processData: any[];
    gameProceedFormGroup: FormGroup;
    commonGameData: any;
    shares: any;
    playerAmount: number;
    source: any = timer(0, 1000);
    toastConfig: any = { positionClass: 'toast-top-left' }
    btnDisability: boolean;
    userViewName: any;
    //-------------------------------------Pre-defined variables that need for full fill the requirement.

    /**
     * Creates an instance of game middle layout component.
     * @param masterService 
     * @param gameBoardService 
     * @param toastrService 
     * @param commonEmitterService 
     * @param userService 
     */
    constructor(private masterService: MasterService, private gameBoardService: GameBoardService, private toastrService: ToastrService, private commonEmitterService: CommonEmitterService, private userService: UserService) { }

    /**
     * on init
     */
    ngOnInit() {
        this.userViewName = this.userService.getUserName();
        this.blockUI.start('Loading please wait........');
        this.initiateFormGroup();
        this.loadProcessData();
        this.refreshShareData();
        this.pumpGameCommonData();
        this.bindTheInitialScore();
        this.blockUI.stop();
    }

    /**
     * Bind the initial score of game middle layout component
     */
    bindTheInitialScore = () => {
        this.commonEmitterService.playerAmount.subscribe(accountInitialAmount => {
            this.playerAmount = accountInitialAmount;
            if (accountInitialAmount == 0)
                this.btnDisability = true;
        });
    }

    /**
     * Load process data of game middle layout component
     */
    loadProcessData = () => {
        this.masterService.getCreditTypes().subscribe(data => {
            this.processData = data;
        });
    }

    /**
     * Initiate form group of game middle layout component
     */
    initiateFormGroup = () => {
        this.gameProceedFormGroup = new FormGroup({
            company: new FormControl(null, Validators.required),
            buyOrSell: new FormControl(null, Validators.required),
            quantity: new FormControl(null, Validators.required)
        });
        this.listenToFormGroup();
    }


    /**
     * Listen to form group of game middle layout component
     */
    listenToFormGroup = () => {
        this.gameProceedFormGroup.valueChanges.subscribe((type: any) => {
            if (this.playerAmount == 0) {
                let type = this.gameProceedFormGroup.get("buyOrSell").value;
                if (type == 1) {
                    //buy
                    this.btnDisability = true;
                } else if (type == 2) {
                    //sell
                    this.btnDisability = false;
                }
            }
        })
    }

    /**
     * Push game play data of game middle layout component
     */
    pushGamePlayData = () => {
        Object.keys(this.gameProceedFormGroup.controls).forEach(field => {
            const control = this.gameProceedFormGroup.get(field)
            control.markAsTouched({ onlySelf: true })
            control.updateValueAndValidity({ onlySelf: true })
        });

        if (this.gameProceedFormGroup.valid) {
            let shareObject: any = this.shares.filter(x => x.companyId == this.gameProceedFormGroup.get("company").value)[0];
            let typeOfBuy: any = this.gameProceedFormGroup.get("buyOrSell").value;
            let insertedQuantity = this.gameProceedFormGroup.get("quantity").value;
            if (typeOfBuy == 1) {
                if (shareObject.quantity < +insertedQuantity)
                    this.toastrService.error('Share quantity cannot be more than ' + shareObject.quantity + ' for company ' + shareObject.companyId, "Error", this.toastConfig);
                else
                    this.processGamePlay(shareObject);
            } else {
                this.gameBoardService.getUserPortfolio(this.userService.getUserId()).subscribe((serviceResponse: any[]) => {
                    let isAvailable: boolean = serviceResponse.some(x => x.companyId === shareObject.companyId);
                    if (isAvailable) {
                        let quantity: number = +serviceResponse.filter(x => x.companyId == shareObject.companyId).map(c => c.quantity);
                        if (quantity < insertedQuantity) {
                            this.toastrService.error("Sell quantity is invalid , please check with user portfolio.", "Error", this.toastConfig);
                        } else {
                            this.processGamePlay(shareObject);
                        }
                    } else {
                        this.toastrService.error("You don't have shares for this company.", "Error", this.toastConfig);
                    }
                });
            }
        }
    }

    /**
     * Shares object
     * @param shareObject 
     */
    processGamePlay = (shareObject: any) => {
        let shareRequestPayload: any = this.buildShareObject(shareObject);
        let userPortfolio: any = this.buildUserPortFolioObject(shareObject, shareRequestPayload);
        this.gameBoardService.processShareGameUpdate(shareRequestPayload).pipe(switchMap((savedShareResponse: any) => {
            if (savedShareResponse) {
                this.toastrService.success('Proceed successfully done.', "Success", this.toastConfig);
                return this.gameBoardService.saveUserPortfolio(userPortfolio);
            } else {
                this.toastrService.error('failed to save data.', "Error", this.toastConfig);
            }
        })).subscribe((portFolioReponse: any) => {
            if (portFolioReponse) {
                this.commonEmitterService.playerAmount.emit(portFolioReponse.accBalance);
                this.toastrService.success('User portfolio updated.', "Success", this.toastConfig);
            } else {
                this.toastrService.error("Get ready for next round", "Information", this.toastConfig);
            }
        });
    }

    /**
     * Pump game common data of game middle layout component
     */
    pumpGameCommonData = () => {
        this.commonEmitterService.emitCommonGameData.subscribe((serviceResponse: any) => {
            if (serviceResponse) {
                this.commonGameData = serviceResponse;
            }
        });
    }

    /**
     * Get game shares of game middle layout component
     */
    getGameShares = () => {
        this.gameBoardService.getGameShares().subscribe((shareResponse: any[]) => {
            if (shareResponse) {
                this.shares = shareResponse;
                this.shares.forEach(share => {
                    share["Time"] = moment(new Date()).format('LTS');
                });
            } else {
                this.toastrService.error('Error on loading shares', 'Error', this.toastConfig);
            }
        });
    }

    /**
     * Build share object of game middle layout component
     */
    buildShareObject = (shareObject: any) => {
        let boughtOption: any = this.gameProceedFormGroup.get("buyOrSell").value;
        return {
            shareId: shareObject.shareId,
            companyId: this.gameProceedFormGroup.get("company").value,
            originalPrice: shareObject.originalPrice,
            currentPrice: shareObject.currentPrice,
            quantity: boughtOption && boughtOption === 1 ? shareObject.quantity - +this.gameProceedFormGroup.get("quantity").value :
                shareObject.quantity + +this.gameProceedFormGroup.get("quantity").value,
            type: boughtOption && boughtOption === 1 ? 'buy' : 'sell',
            insertedQuantity: this.gameProceedFormGroup.get("quantity").value,
            userId: this.userService.getUserId(),
            name: null,
            gainLoss: 0.0
        }
    }

    /**
     * Build user port folio object of game middle layout component
     */
    buildUserPortFolioObject = (shareObject: any, shareRequestPayload: any) => {
        return {
            userId: this.userService.getUserId(),
            userName: this.userService.getUserName(),
            companyId: shareObject.companyId,
            companyName: shareObject.name,
            quantity: this.gameProceedFormGroup.get("quantity").value,
            marketPrice: shareObject.currentPrice,
            profitLoss: 0,
            totalAmount: 0,
            type: shareRequestPayload.type,
            accBalance: this.playerAmount ? this.playerAmount : 0
        }
    }

    /**
     * Refresh share data of game middle layout component
     */
    refreshShareData = () => {
        this.shareTickerSubscriptor = this.source.subscribe((tiker: any) => {
            if (tiker) {
                this.getGameShares();
            }
        })
    }

    /**
     * Verify button availability of game middle layout component
     */
    verifyButtonAvailability = () => {
        return this.gameProceedFormGroup.invalid || this.btnDisability;
    }

    /**
     * on destroy
     */
    ngOnDestroy() {
        this.shareTickerSubscriptor.unsubscribe();
    }
}