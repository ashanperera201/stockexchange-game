import { Component, OnInit } from '@angular/core';
import { CompanyService } from '../../services/company.service';
import { ToastrService } from 'ngx-toastr';
import { NgBlockUI, BlockUI } from 'ng-block-ui';


@Component({
  templateUrl: './companies.component.html',
  styleUrls: ['./companies.component.scss']
})

export class CompanyComponent implements OnInit {
  companyData: any[];
  @BlockUI() blockUI: NgBlockUI;

  /**
   * Creates an instance of company component.
   * @param companyService 
   * @param toastrService 
   */
  constructor(private companyService: CompanyService, private toastrService: ToastrService) {
  }

  /**
   * on init
   */
  ngOnInit() {
  }

  /**
   * Get company of company component
   */
  getCompany = (sectorId) => {
    this.blockUI.start("Processing...........")
    this.companyService.getCompanyData(sectorId)
      .subscribe((response: any[]) => {
        if (response && response.length != 0) {
          this.companyData = response;
          this.blockUI.stop();
        } else {
          this.blockUI.stop();
        }
      }, error => { this.blockUI.stop(); });
  }
}