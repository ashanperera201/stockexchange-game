import { Component, OnInit } from '@angular/core';
import { Chart } from 'chart.js';
import { ToastrService } from 'ngx-toastr';
import { NgBlockUI, BlockUI } from 'ng-block-ui';
import { DashboardService } from '../../services/dashboard.service';
import { DashboardEnum } from '../../enums/dashboard.enum';
import { CharStyleService } from '../../services/chart-style-services/chart-style.service';
import { GameBoardService } from '../../services/game-board.service';
import { ScattorRoot, ScattorSlot } from '../../models/scattor-model';
@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.scss']
})

export class HomeComponent implements OnInit {

    @BlockUI() blockUI: NgBlockUI;

    lineChart: any;
    chartBar: any;
    barChart: any;
    pieChart: any;
    radarChart: any;
    bubbleChart: any;

    dashboardEnum = DashboardEnum;
    widgetArray: [];
    companyList: string[] = [];
    originalPriceList: number[] = [];
    currentPriceList: number[] = [];
    gainLoss: number[] = [];
    shareList: number[] = [];
    quantityList: number[] = [];

    quantityForGraphData: number[] = [];
    companyListForGraphData: string[] = [];
    gainLossForGraphData: number[] = [];

    scattorSet: any[] = [];

    scattorSlot: ScattorSlot = {} as any;
    scattorRoot: ScattorRoot = [] as any;
    alphabet: string[] = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"];

    /**
     * Creates an instance of home component.
     * @param toastrService 
     * @param dashboardService 
     * @param charStyleService 
     */
    constructor(private toastrService: ToastrService, private dashboardService: DashboardService, private charStyleService: CharStyleService, private gameBoardService: GameBoardService) {
        this.scattorRoot.data = [] as any;
    }

    /**
     * on init
     */
    ngOnInit() {
        this.blockUI.start('Please wait dashboard is loading.....');
        this.getWidgetData();
        this.fetchChartLineData();
        this.fetchGraphData();
        this.fetchShareAllData();
        setTimeout(() => {
            this.blockUI.stop();
        }, 1000);
    }

    /**
     * Get widget data of home component
     */
    getWidgetData = () => {
        this.dashboardService.getCommonWidgetData().subscribe((widgetResponse: any) => {
            if (widgetResponse) {
                this.widgetArray = widgetResponse;
            }
        });
    }

    /**
     * Fetch share all data of home component
     */
    fetchShareAllData = () => {
        this.gameBoardService.getGameShares().subscribe((shareResponse: any[]) => {
            if (shareResponse && shareResponse.length != 0) {
                shareResponse.forEach(resElement => {
                    this.scattorRoot.label = resElement.name
                    this.scattorSlot.x = resElement.originalPrice;
                    this.scattorSlot.y = resElement.quantity;
                    this.scattorRoot.data.push(this.scattorSlot);
                    this.scattorRoot.backgroundColor = "#0EE37F";
                    this.scattorSet.push(this.scattorRoot);
                    this.scattorSlot = {} as any;
                    this.scattorRoot = [] as any;
                    this.scattorRoot.data = [] as any;
                });
                this.initiateScatterChart();
            }
        });
    }

    /**
     * Fetch chart line data of home component
     */
    fetchChartLineData = () => {
        this.dashboardService.commonGraphData().subscribe((chartData: any[]) => {
            if (chartData && chartData.length != 0) {
                chartData.forEach(data => {
                    this.companyList.push(data.name);
                    this.originalPriceList.push(data.originalPrice);
                    this.currentPriceList.push(data.currentPrice);
                    this.gainLoss.push(data.gainLoss);
                    this.quantityList.push(data.quantity);
                    this.shareList.push(data.shareId);
                })
                this.charLinePayload();
                this.initiateChartLine();
                this.barChartPayload();
                this.initiateBarchart();
                this.PieChartPayload();
                this.initiatePieChart();
                this.redarChartPayload();
                this.initiateRadarChart();
            }
        });
    }

    /**
     * Fetch graph data of home component
     */
    fetchGraphData = () => {
        this.dashboardService.commonGraphData().subscribe((serviceResponse: any) => {
            if (serviceResponse) {
                serviceResponse.forEach(chartData => {
                    this.companyListForGraphData.push(chartData.name);
                    this.quantityForGraphData.push(chartData.quantity);
                    this.gainLossForGraphData.push(chartData.gainLoss);
                });
                this.buildChartBarPayload();
                this.initiateChartBar();
            }
        })
    }

    /**
     * Build chart bar payload of home component
     */
    buildChartBarPayload = () => {
        this.chartBar = {
            data: {
                labels: this.companyListForGraphData ? this.companyListForGraphData : [],
                datasets: [{
                    label: 'Gained Loss',
                    data: this.gainLossForGraphData ? this.gainLossForGraphData : [],
                    backgroundColor: '#5b6582',
                    borderColor: '#5b6582',
                    borderWidth: 2
                },
                {
                    label: 'Quantity',
                    data: this.quantityForGraphData ? this.quantityForGraphData : [],
                    backgroundColor: '#36a2eb',
                    borderColor: '#36a2eb',
                    borderWidth: 2
                }
                ]
            },
            options: {
                barValueSpacing: 1,
                scales: {
                    yAxes: [{
                        ticks: {
                            fontColor: 'rgba(0,0,0,.6)',
                            fontStyle: 'bold',
                            beginAtZero: true,
                            maxTicksLimit: 8,
                            padding: 10
                        }
                    }],
                    xAxes: [{
                        barPercentage: 0.4
                    }]
                },
                responsive: true,
                legend: {
                    position: 'bottom',
                    display: false
                },
            }
        }
    }

    /**
     * Char line payload of home component
     */
    charLinePayload = () => {
        this.lineChart = {
            data: {
                labels: this.companyList ? this.companyList : [],
                datasets: [{
                    label: 'Original Price',
                    data: this.originalPriceList ? this.originalPriceList : [],
                    backgroundColor: 'transparent',
                    borderColor: '#5b6582',
                    borderWidth: 2
                },
                {
                    label: 'Current Price',
                    data: this.currentPriceList ? this.currentPriceList : [],
                    backgroundColor: 'transparent',
                    borderColor: '#36a2eb',
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
                responsive: true,
                legend: {
                    position: 'bottom',
                    display: false
                },
            }
        }
    }

    /**
     * Barchart payload of home component
     */
    barChartPayload = () => {
        this.barChart = {
            type: 'bar',
            data: {
                labels: this.companyList ? this.companyList : [],
                datasets: [{
                    label: 'GAIN LOSS.',
                    data: this.gainLoss ? this.gainLoss : [],
                    backgroundColor: this.charStyleService.getCommonBackgroundList(),
                    borderColor: this.charStyleService.getCommonBorderColorList(),
                    borderWidth: 1
                }]
            },
            options: {
                title: {
                    text: "BAR CHART",
                    display: true
                },
                scales: {
                    yAxes: [{
                        ticks: {
                            beginAtZero: true
                        }
                    }]
                }
            }
        }
    }

    /**
     * Pie chart payload of home component
     */
    PieChartPayload = () => {
        this.pieChart = {
            type: 'pie',
            data: {
                labels: this.shareList ? this.shareList : [],
                datasets: [
                    {
                        data: this.quantityList ? this.quantityList : [],
                        backgroundColor: this.charStyleService.getCommonBackgroundList(),
                        borderColor: this.charStyleService.getCommonBorderColorList(),
                        borderWidth: 1
                    }
                ]
            },
            options: {
                title: {
                    text: "PIE CHART",
                    display: true
                },
                legend: {
                    position: 'bottom',
                    display: false
                },
                scales: {
                    yAxes: [{
                        ticks: {
                            beginAtZero: true
                        }
                    }]
                }
            }
        }
    }


    /**
     * Redar chart payload of home component
     */
    redarChartPayload = () => {
        this.radarChart = {
            labels: this.companyList ? this.companyList : [],
            datasets: [
                {
                    label: "Original Price",
                    backgroundColor: "rgba(200,0,0,0.2)",
                    data: this.originalPriceList ? this.originalPriceList : []
                },
                {
                    label: "Current Price",
                    backgroundColor: "rgba(0,0,200,0.2)",
                    data: this.currentPriceList ? this.currentPriceList : []
                },
                {
                    label: "Shares",
                    backgroundColor: "rgba(137, 91, 236, 1)",
                    data: this.shareList ? this.shareList : []
                }
            ]
        };
    }



    /**
     * Initiate chart line of home component
     */
    initiateChartLine = () => {
        new Chart('chart-line', {
            type: 'line',
            data: this.lineChart ? this.lineChart.data : [],
            options: this.lineChart ? this.lineChart.options : []
        });
    }

    /**
     * Initiate barchart of home component
     */
    initiateBarchart = () => {
        new Chart('bar-chart', {
            type: 'bar',
            data: this.barChart ? this.barChart.data : [],
            options: this.barChart ? this.barChart.options : []
        })
    }

    /**
     * Initiate chart of home component
     */
    initiateChartBar = () => {
        new Chart('chart-bar', {
            type: 'bar',
            data: this.chartBar ? this.chartBar.data : [],
            options: this.chartBar ? this.chartBar.options : []
        });
    }

    /**
     * Initiate pie chart of home component
     */
    initiatePieChart = () => {
        new Chart('pie-chart', {
            type: 'pie',
            data: this.pieChart.data,
            options: this.pieChart.options
        })
    }

    /**
     * Initiate radar chart of home component
     */
    initiateRadarChart = () => {
        new Chart('radar-chart', {
            type: 'radar',
            data: this.radarChart,
            options: {
                scale: {
                    display: false
                }
            }
        });
    }

    /**
     * Initiate chart for bubble chart of home component
     */
    initiateScatterChart = () => {
        new Chart('scatter', {
            type: 'scatter',
            data: {
                datasets: this.scattorSet ? this.scattorSet : []
            },
            options: {
                scales: {
                    xAxes: [{
                        type: 'linear',
                        position: 'bottom'
                    }]
                }
            }
        });
    }

    /**
     * Show status message of home component
     */
    showStatusMessage = () => {
        this.toastrService.error('error loading data', 'Error', { closeButton: true, progressBar: true })
    }
}
