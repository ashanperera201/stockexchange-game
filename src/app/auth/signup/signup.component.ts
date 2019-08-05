import { Component, OnInit, EventEmitter, Output } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { BlockUI, NgBlockUI } from 'ng-block-ui';
import { AuthService } from '../../services/auth.service';
import { ToastrService } from 'ngx-toastr';
@Component({
    selector: 'app-signup',
    templateUrl: './signup.component.html',
    styleUrls: ['./signup.component.scss']
})

export class SignUpComponent implements OnInit {
    @BlockUI() blockUI: NgBlockUI;
    @Output() backToLogin: any = new EventEmitter<any>();
    userRegisterFormGroup: FormGroup;

    /**
     * Creates an instance of sign up component.
     * @param authService 
     * @param toastrService 
     */
    constructor(private authService: AuthService, private toastrService: ToastrService) { }

    /**
     * on init
     */
    ngOnInit() {
        this.initFormGroup();
    }

    /**
     * Init form group of sign up component
     */
    initFormGroup = () => {
        this.userRegisterFormGroup = new FormGroup({
            email: new FormControl(null, [Validators.required, Validators.email]),
            userName: new FormControl(null, Validators.required),
            dob: new FormControl(null, Validators.required),
            nic: new FormControl(null, Validators.required),
            contactNo: new FormControl(null),
            address: new FormControl(null)
        });
    }

    /**
     * Trigger back of sign up component
     */
    triggerBack = () => {
        this.backToLogin.emit(false);
    }

    /**
     * Register user of sign up component
     */
    registerUser = () => {
        Object.keys(this.userRegisterFormGroup.controls).forEach(field => {
            const control = this.userRegisterFormGroup.get(field)
            control.markAsTouched({ onlySelf: true })
            control.updateValueAndValidity({ onlySelf: true })
        });

        if (this.userRegisterFormGroup.valid) {
            this.blockUI.start('Processing, please wait....')
            this.authService.registerUser(this.userRegisterFormGroup.value).subscribe((user: any) => {
                if (user) {
                    this.toastrService.success("Successfully user saved", "Success", { closeButton: true })
                    this.backToLogin.emit(false)
                    this.blockUI.stop()
                } else {
                    this.toastrService.error("User saving process is failed, please try again", "Error", { closeButton: true })
                    this.blockUI.stop()
                }
            }, error => {
                this.toastrService.error("User saving process is failed, please try again", "Error", { closeButton: true })
                this.blockUI.stop()
            });
        }
    }

    /**
     * Get error message for email of sign up component
     */
    getErrorMessageForEmail = (): string => {
        return this.userRegisterFormGroup.controls.email.hasError('email') ? 'Please check email address' :
            this.userRegisterFormGroup.controls.email.hasError('required') ? 'This field is required' : '';
    }
}
