import {Component, OnInit, Inject} from '@angular/core';
import {CompanyService} from '../../services/company.service';
import {MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';
import {FormGroup, FormControl} from '@angular/forms';
import {BlockUI, NgBlockUI} from 'ng-block-ui';
import {ToastrService} from 'ngx-toastr';


@Component({
  templateUrl: './statistic.component.html',
  styleUrls: ['./statistic.component.scss']
})

export class StatisticComponent implements OnInit {
  @BlockUI() blockUI: NgBlockUI;
  calculationFormGroup: FormGroup;
  calculator: any;
  // Dynamic  Server Logic purpose field variables ,Begins
  totalInvest: any;
  grossReturn: any;
  profit: any;
  amount: any;
  // Dynamic  Server Logic purpose field variables ,End

  constructor(public dialogRef: MatDialogRef<StatisticComponent>, @Inject(MAT_DIALOG_DATA) public data: any,
              private companyService: CompanyService, private toastrService: ToastrService) {}
  /**
   * on init
   */
  ngOnInit() {
    this.initFormGroup();
  }

  /**
   * Init form group of statistic component
   */
  initFormGroup = () => {
    this.calculationFormGroup = new FormGroup({
      buyDate:     new FormControl(null),
      sellDate:    new FormControl(null),
      purchPrice:  new FormControl(null),
      quantity:    new FormControl(null),
      purchComm:   new FormControl(null),
      soldPrice:   new FormControl(null),
      soldComm:    new FormControl(null),
      totalInvest: new FormControl(null),
      grossReturn: new FormControl(null),
      profit:      new FormControl(null),
      amount:      new FormControl(null),
    } );
  }
  /**
   * Close modal of statistic component
   */
  closeModal = () => {
    this.dialogRef.disableClose = true;
    this.dialogRef.close();
  }

  /**
   * Trigger calculate of statistic component
   */
  triggerCalculate = () => {
    // Call to company service to fetch the Calculated Values as per the user input
    this.companyService.getCalculator(this.calculationFormGroup.value).subscribe((calculator: any) => {
        if (calculator) {
           this.calculator = calculator;
           // Trigger to Assign the Calculated Values
           this.fetchOutputValue();
           this.toastrService.success('Successfully Calculated the Values.', 'Success', {closeButton: true});
           this.blockUI.stop();
         }
       },
       error => {
       });
  }

  /**
   * Trigger reset of statistic component
   */
  triggerReset = () => {
    this.calculationFormGroup.reset();
    // Trigger Clear Client Fields
    this.clearFields();
  }

  /**
   * Trigger to fetch Calculated values from server
   */
  fetchOutputValue = () => {

    this.totalInvest  = this.calculator.totalInvest;
    this.grossReturn  = this.calculator.grossReturn;
    this.profit       = this.calculator.profit;
    if ( this.calculator.profit < 0) {
      this.amount       = 0;
    } else {
      this.amount       = this.calculator.grossReturn + this.calculator.profit;
    }
  }
  /**
   * Clear Variable Values
   */
  clearFields = () => {
    this.totalInvest = 0;
    this.grossReturn = 0;
    this.profit = 0;
    this.amount = 0;
  }

}
