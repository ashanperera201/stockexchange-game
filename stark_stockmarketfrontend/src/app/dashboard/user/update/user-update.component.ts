import { Component, OnInit, EventEmitter, Output } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { BlockUI, NgBlockUI } from 'ng-block-ui';
import { ToastrService } from 'ngx-toastr';
import { UserService } from '../../../services/user.service';
import { Router } from '@angular/router';
@Component({
    templateUrl: './user-update.component.html',
    styleUrls: ['./user-update.component.scss']
})

export class UserUpdateComponent implements OnInit {
    @BlockUI() blockUI: NgBlockUI;
    @Output() backToLogin: any = new EventEmitter<any>()
    userModificationFormGroup: FormGroup;
    userPayload: any = {};

    /**
     * Creates an instance of user profile component.
     * @param userService 
     * @param toastrService 
     * @param router 
     */
    constructor(private userService: UserService, private toastrService: ToastrService, private router: Router) { }

    /**
     * on init
     */
    ngOnInit() {
        this.blockUI.start(' Please wait loading .....');
        this.initFormGroup();
        this.getUser();
        if (this.userPayload) {
            this.letsPatchUser();
        }
        this.blockUI.stop();
    }



    /**
     * Init form group of user profile component
     */
    initFormGroup = () => {
        this.userModificationFormGroup = new FormGroup({
            email: new FormControl(null, [Validators.required, Validators.email]),
            userName: new FormControl(null, Validators.required),
            dob: new FormControl(null, Validators.required),
            nic: new FormControl(null, [Validators.required, Validators.minLength(10)]),
            contactNo: new FormControl(null, Validators.minLength(10)),
            address: new FormControl(null),
            userId: new FormControl(null)
        });
    }

    /**
     * User modifire of user profile component
     */
    userModifire = () => {
        Object.keys(this.userModificationFormGroup.controls).forEach(field => {
            let control = this.userModificationFormGroup.get(field);
            control.markAsTouched({ onlySelf: true });
            control.updateValueAndValidity({ onlySelf: true });
        });

        if (this.userModificationFormGroup.valid) {
            this.blockUI.start('Processing, please wait....');
            this.userModificationFormGroup.controls['email'].enable();
            this.userModificationFormGroup.controls['userId'].enable();
            this.userService.editUser(this.userModificationFormGroup.value).subscribe((user: any) => {
                if (user) {
                    this.userService.updateLocalUser(user)
                    this.toastrService.success('Successfully modified user.', 'Success', { closeButton: true });
                    this.router.navigate(['/dashboard/home'])
                    this.blockUI.stop();
                } else {
                    this.toastrService.error('Update process is failed, please try again', 'Error', { closeButton: true });
                    this.blockUI.stop();
                }
            }, error => {
                this.toastrService.error('Can not modify user profile, please try again', 'Error', { closeButton: true });
                this.blockUI.stop();
            });
        }
    }

    /**
     * Get user of user update component
     */
    getUser = () => {
        this.userPayload = this.userService.getProfile()[0].verifiedUser;
    }

    // i am the patcher
    letsPatchUser = () => {
        let patchLoad = {
            email: this.userPayload.email,
            userName: this.userPayload.userName,
            dob: new Date(this.userPayload.dob),
            nic: this.userPayload.nic,
            contactNo: this.userPayload.contactNo !== 0 ? this.userPayload.contactNo : '',
            address: this.userPayload.address,
            userId: this.userPayload.userId
        };
        this.userModificationFormGroup.controls['email'].disable();
        this.userModificationFormGroup.controls['userId'].disable();
        this.userModificationFormGroup.patchValue(patchLoad);       
    }
}
