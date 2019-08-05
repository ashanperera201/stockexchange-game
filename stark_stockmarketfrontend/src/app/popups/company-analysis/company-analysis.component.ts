import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {BlockUI, NgBlockUI} from 'ng-block-ui';
import {CompanyService} from '../../services/company.service';
import {UserService} from '../../services/user.service';
import {Chart} from 'chart.js';
import {MatDialogRef} from '@angular/material';

@Component({
  templateUrl: './company-analysis.component.html',
  styleUrls: ['./company-analysis.component.scss']
})

export class CompanyAnalysisComponent implements OnInit {

  SelectedToDate: any;
  SelectedFromDate: any;
  data: any;
  commonGameData: any;
  prices: number[] = [];
  chart: any;
  lineChartData: any = [{data: []}];
  AnalysisFormGroup: FormGroup;
  @BlockUI() blockUI: NgBlockUI;
  Companies: any[];
  SelectedCompanyName: any;

  /**
   * Creates an instance of bank account component.
   * @param CompanyService
   * @param userService
   * @param dialogRef
   */
  constructor(private CompanyService: CompanyService, private userService: UserService, public dialogRef: MatDialogRef<CompanyAnalysisComponent>) {
  }

  /**
   * on init
   */
  ngOnInit() {
    this.initFormGroup();
    this.fetchCompanyIDs();
    this.initiateChart();
  }

  /**
   * initiate form group
   */
  initFormGroup = () => {
    this.AnalysisFormGroup = new FormGroup({
      company: new FormControl(null, Validators.required),
      FromDate: new FormControl(null, Validators.required),
      ToDate: new FormControl(null, Validators.required)
    });
  };


  /**
   * initiate Chart
   */
  initiateChart = () => {
    this.chart = new Chart('dchart-line', {
      type: 'line',
      data: {
        labels: [],
        datasets: [{
          label: 'Share Price',
          data: this.lineChartData,
          // backgroundColor: 'transparent',
          backgroundColor: 'rgba(37, 125, 248, 0.09)',
          borderColor: 'rgba(208, 113, 243, 0.69)',
          borderWidth: 2
        }]
      },
      options: {
        scales: {
          yAxes: [{
            ticks: {
              fontColor: 'rgba(0,0,0,.6)',
              fontStyle: 'bold',
              beginAtZero: true,
              maxTicksLimit: 8,
              padding: 5
            }
          }]
        },
        spanGaps: true,
        responsive: true,
        legend: {
          position: 'bottom',
          display: false
        },
        fill: true
      },
    });
  };

  /**
   * get chart Data
   */
  getChartData = () => {
    this.blockUI.start('Fetching Account data , please wait .......');
    this.CompanyService.getSharePriceDetails(this.AnalysisFormGroup.get('company').value.toString(), this.userService.getUserId(), this.SelectedFromDate, this.SelectedToDate)
      .subscribe((response: any[]) => {
          if (response && response.length != 0) {
            response.forEach(resElement => {
              let date = new Date(resElement.marketDate);
              this.prices[response.indexOf(resElement)] = resElement.marketPrice;
              let lblValue = date.toLocaleString();
              this.chart.data.labels.push(lblValue);
            });
            this.updateChart();
            this.blockUI.stop();
          }
          else {
            this.blockUI.stop();
            this.prices = [];
            this.updateChart();
          }
        },
        error => this.blockUI.stop());
  };

  /**
   * Fetch Company Id's to the dropdown
   */
  fetchCompanyIDs = () => {
    this.blockUI.start('Fetching Company data , please wait .......');
    this.CompanyService.getAllCompanyIds()
      .subscribe((response: any[]) => {
          if (response && response.length != 0) {
            this.Companies = response;

            this.blockUI.stop();
          }
          else {
            this.blockUI.stop();
          }
        },
        error => this.blockUI.stop());
  };

  /**
   * Update the chart
   */
  updateChart = () => {
    this.lineChartData = this.prices as any[];
    this.chart.data.datasets[0].data = this.lineChartData;
    this.chart.update();

  };


  /**
   * Close the dialog
   */
  closeDialog = () => {
    this.dialogRef.close();
  };


  /**
   * Analyze the company share price
   */
  Analyze = () => {
    this.SelectedCompanyName = this.AnalysisFormGroup.get('company').value.toString();
    this.initiateChart();
    Object.keys(this.AnalysisFormGroup.controls).forEach(field => {
      const control = this.AnalysisFormGroup.get(field);
      control.markAsTouched({onlySelf: true});
      control.updateValueAndValidity({onlySelf: true});
    });
    this.SelectedToDate = new Date(this.AnalysisFormGroup.controls.ToDate.value.toString()).toISOString();
    this.SelectedFromDate = new Date(this.AnalysisFormGroup.controls.FromDate.value.toString()).toISOString();
    this.getChartData();
  };
}
