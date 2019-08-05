import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { CreateGroupService } from '../../../services/create-group.service';
import { NgBlockUI, BlockUI } from 'ng-block-ui';
import { UserService } from '../../../services/user.service';
import { Router } from '@angular/router';

@Component({
    templateUrl: './create-group.component.html',
    styleUrls: ['./create-group.component.scss']
})

export class CreateGroupComponent implements OnInit {
    @BlockUI() blockUI: NgBlockUI;
    createGroup: FormGroup;

    /**
     * Creates an instance of create group component.
     * @param toastrService 
     * @param CreateGroupService 
     * @param userService 
     * @param router 
     */
    constructor(private toastrService: ToastrService, private CreateGroupService: CreateGroupService, private userService: UserService, private router: Router) { }

    /**
     * on init
     */
    ngOnInit() {
        this.initFormGroup();
    }

    /**
     * Init form group of create group component
     */
    initFormGroup = () => {
        this.createGroup = new FormGroup({
            groupName: new FormControl(null, Validators.required),
            players: new FormControl(null, Validators.required)
        });
    }

    /**
     * Trigger create group of create group component
     */
    triggerCreateGroup = () => {
        Object.keys(this.createGroup.controls).forEach(field => {
            const control = this.createGroup.get(field)
            control.markAsTouched({ onlySelf: true })
            control.updateValueAndValidity({ onlySelf: true })
        });

        if (this.createGroup.valid) {
            let teamCount = this.createGroup.get('players').value;
            if (teamCount > 1) {
                if (teamCount < 5) {
                    this.blockUI.start('Please wait, processing ........');
                    this.CreateGroupService.createGroup(this.buildObject()).subscribe((serviceResponse: any) => {
                        if (serviceResponse) {
                            this.toastrService.success('Group successfully created.', 'Success', { closeButton: true });
                            this.blockUI.stop();
                            this.router.navigate(['/dashboard/home']);
                        }
                        else {
                            this.toastrService.error('Group creation failed.', 'Failed', { closeButton: true });
                            this.blockUI.stop();
                        }
                    }, error => {
                        this.blockUI.stop();
                    })
                }
                else {
                    this.toastrService.error('One group can only play 4 members.', 'Failed', { closeButton: true });
                    this.blockUI.stop();
                }
            }else{
                this.toastrService.error('A team should have atleast 2 members.', 'Failed', { closeButton: true });
                this.blockUI.stop();
            }
           
        }
    }

    /**
     * Build object of create group component
     */
    buildObject = () => {
        let payLoad = this.createGroup.value;
        payLoad['userId'] = this.userService.getUserId();
        return payLoad ? payLoad : null;
    }
}
