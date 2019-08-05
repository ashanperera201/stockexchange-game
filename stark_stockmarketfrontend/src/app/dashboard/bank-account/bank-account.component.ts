import { Component, OnInit } from '@angular/core';
import { NgBlockUI, BlockUI } from 'ng-block-ui';
import { BankService } from '../../services/bank.service';
import { Chart } from 'chart.js';
import { UserService } from '../../services/user.service';
import { MasterService } from '../../services/master/master.data.service';

@Component({
  selector: 'app-bank-account',
  templateUrl: './bank-account.component.html',
  styleUrls: ['./bank-account.component.scss'],

})

export class BankAccountComponent implements OnInit {

  @BlockUI() blockUI: NgBlockUI;
  Months: any[];
  Years: any[];
  SelectedMonth: number;
  SelectedYear: number;
  data: any;
  accDets: any[];
  amounts: number[] = [];
  chart: any;
  lineChartData: any = [
    {
      data: []
    }
  ];
  accountBalance: any;

  /**
   * Creates an instance of bank account component.
   * @param bankService
   * @param toastrService
   * @param ngZone
   * @param userService
   */
  constructor(private bankService: BankService, private userService: UserService, private masterService: MasterService) {
    this.SelectedYear = 2019;
    this.SelectedMonth = 7;
  }

  /**
   * on init
   */
  ngOnInit() {
    this.loadBalance();
    this.getMasterData();
    this.initiateChart();
    this.getChartData();
  }

  loadBalance = () => {
    this.bankService.getAccountBalance(this.userService.getUserId()).subscribe(res => {
      if (res) { this.accountBalance = res.amount }
    })
  }

  /**
   * Get master data of bank account component
   */
  getMasterData = () => {
    this.Years = this.masterService.getViewYearList();
    this.Months = this.masterService.getViewMonthList();
  }

  /**
   * Filters changed
   */
  filtersChanged() {
    this.amounts = [];
    this.getChartData();
  }

  /**
   * Initiate chart of bank account component
   */
  initiateChart = () => {
    this.chart = new Chart('chart-line', {
      type: 'line',
      data: {
        labels: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '24', '25', '26', '27', '28', '29', '30', '31'],
        datasets: [{
          label: 'Account Balance',
          data: this.lineChartData,
          backgroundColor: 'transparent',
          borderColor: '#5b6582',
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
              padding: 10
            }
          }]
        },
        spanGaps: true,
        responsive: true,
        legend: {
          position: 'bottom',
          display: false
        },
      },
    });
  };


  /**
   * Get chart data of bank account component
   */
  getChartData = () => {
    this.blockUI.start('Fetching Account data , please wait .......');
    this.bankService.getBankAccountDetails(this.userService.getUserId(), this.SelectedMonth, this.SelectedYear)
      .subscribe((response: any[]) => {
        if (response && response.length != 0) {
          response.forEach(resElement => {
            let date = new Date(resElement.date);
            this.amounts[date.getDate() - 1] = resElement.amount;
          });
          this.updateChart();
          this.blockUI.stop();
        }
        else {
          this.blockUI.stop();
          this.amounts = [];
          this.updateChart();
        }
      }, error => this.blockUI.stop());
  };

  /**
   * Update chart of bank account component
   */
  updateChart = () => {
    this.lineChartData = this.amounts as any[];
    this.chart.data.datasets[0].data = this.lineChartData;
    this.chart.update();

  };
}