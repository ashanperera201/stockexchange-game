import { Component, OnInit } from '@angular/core';
import { UserHelperService } from '../../services/user-helper.service';
import { Router } from '@angular/router';

@Component({
    templateUrl: './user-helper.component.html',
    styleUrls: ['./user-helper.component.scss']
})

export class UserHelperComponent implements OnInit {

    step: number = 0;
    panelOpenState: boolean = false;

    /**
     * Config object of user helper component
     */
    configObject: any = {
        whatIs: '',
        rules: '',
        formula: '',
        end: '',
    }

    /**
     * Creates an instance of user helper component.
     * @param userHelperService 
     */
    constructor(private userHelperService: UserHelperService, private router: Router) { }

    /**
     * on init
     */
    ngOnInit() {
        this.getUserHelperData();
    }

    /**
     * Sets step
     * @param index 
     */
    setStep(index: number) {
        this.step = index;
    }

    /**
     * Next step
     */
    nextStep() {
        this.step++;
    }

    /**
     * Prev step
     */
    prevStep() {
        this.step--;
    }

    /**
     * Get user helper data of user helper component
     */
    getUserHelperData = () => {
        this.userHelperService.getUserHelperData().subscribe((response: any) => {
            if (response) {
                this.configObject.whatIs = response.header;
                this.configObject.rules = response.body
                this.configObject.formula = response.formula;
                this.configObject.footer = response.footer;
                this.configObject.end = response.end;
            }
        });
    }

    /**
     * Load window of user helper component
     */
    loadWindow = () => {
        window.open('https://drive.google.com/drive/folders/15qpBaAwczTeCelzv4yLV_R3VmBZ_ZU4U?usp=sharing');
    }

    /**
     * Trigger back of user helper component
     */
    triggerBack = () => {
        this.router.navigate(['/dashboard/home'])
    }

}
