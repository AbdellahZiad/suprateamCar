import {Component, OnInit} from '@angular/core';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {UserService} from "../../services/user.service";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {UserModel} from "../../model/UserModel";


@Component({
  selector: 'app-users-management',
  templateUrl: './users-management.component.html',
  styleUrls: ['./users-management.component.scss']
})
export class UsersManagementComponent implements OnInit {

  isVisible = false;
  listOfData: any;
  id: any;
  mode: Boolean;
  submitted = false;
  pw: boolean = false;
  passwordVisible = false;
  password?: string;

  // [
  // {user: 'Admin', email: 'admin@scor.com', role: 'admin', creationDate: '07/05/2017', validUntil: '', active: 'Y'},
  // {user: 'Admin', email: 'admin@scor.com', role: 'admin', creationDate: '09/11/2019', validUntil: '09/11/2024', active: 'Y'},
  // {user: 'Admin', email: 'admin@scor.com', role: 'admin', creationDate: '07/12/2018', validUntil: '31/12/2020', active: 'Y'}];
  validateForm!: FormGroup;

  selectedUser: UserModel;
  roles: [
    {
      id: 1,
      name: "ADMIN"
    },
    {
      id: 2,
      name: "USER"
    }
  ];
  title: any;


  constructor(private msg: NzMessageService,
              private modal: NzModalService,
              private fb: FormBuilder,
              private userService: UserService) {

    this.validateForm = this.fb.group({
      id: '',
      email: [null, [Validators.email, Validators.required]],
      pw: [null, [Validators.required]],
      checkPassword: [null, [Validators.required, this.confirmationValidator]],
      fullName: [null, [Validators.required]],
      role: [null],
      companyName: [null, [Validators.required]],
      validUntil: [null, [Validators.required]],
      createDate: '',
      active: '',
    });

    this.getAllUsers();

  }

  ngOnInit() {
    this.getAllUsers();
  }

  showModal(user): void {
    console.log("user = ", user);

    if (user == null) {
      this.pw = false;
      this.validateForm.reset();
      this.title = "New User";
    } else {
      this.selectedUser = user;
      this.validateForm.patchValue(user);
      this.title = "Update User";
      this.pw = true;
    }
    this.isVisible = true;

  }

  handleOk(): void {

    console.log("Form data OK = ", this.validateForm.valid);
    this.selectedUser = this.validateForm.value;

    if (this.validateForm.valid) {
      this.userService.saveUser(this.selectedUser)
        .subscribe(
          data =>{
            this.msg.success("User saved successfully");
            this.getAllUsers()
          },error => {
            this.msg.error(error.error.message);
            console.log("--->ERROR", error);
            this.getAllUsers()
          }
        );
      this.isVisible = false;
      this.validateForm.reset();
    } else {
      this.isVisible = true;
      for (const i in this.validateForm.controls) {
        this.validateForm.controls[i].markAsDirty();
        this.validateForm.controls[i].updateValueAndValidity();
      }

    }
    // this.getAllUsers();

  }

  handleCancel(): void {
    console.log('Button cancel clicked!');
    this.isVisible = false;
  }

  deleteUser(id) {
    this.id = id;
    this.modal.confirm({
      nzTitle: 'Confirm Delete ',
      nzContent: "Are you sure you want to delete this user?",
      nzOkText: 'confirm Delete ',
      nzCancelText: 'Cancel Delete',
      nzOnOk: () => this.confirmDelete(),
      nzOnCancel: () => this.closeSubscription()
    });

  }

  private confirmDelete() {

    this.userService.deleteUser(this.id)
      .subscribe(
        data => {
          console.log("---> data after delete", data);
          this.msg.success("User deleted successfully");

          this.getAllUsers()
        },
        error => {

          console.log("--->ERROR", error);
        }
      );


  }

  private closeSubscription() {
    console.log("Cancel");

  }

  private getAllUsers() {
    this.userService.getAllUser()
      .subscribe(
        data => {
          console.log("---data", data);
          this.listOfData = data;

        }
      )
  }


  updateConfirmValidator(): void {
    /** wait for refresh value */
    Promise.resolve().then(() => this.validateForm.controls.checkPassword.updateValueAndValidity());
  }

  confirmationValidator = (control: FormControl): { [s: string]: boolean } => {
    if (!control.value) {
      return {required: true};
    } else if (control.value !== this.validateForm.controls.pw.value) {
      return {confirm: true, error: true};
    }
    return {};
  };

  getCaptcha(e: MouseEvent): void {
    e.preventDefault();
  }


  showOrHidePw() {
  }

}

export function MustMatch(controlName: string, matchingControlName: string) {
  return (formGroup: FormGroup) => {
    const control = formGroup.controls[controlName];
    const matchingControl = formGroup.controls[matchingControlName];

    if (matchingControl.errors && !matchingControl.errors.mustMatch) {
      // return if another validator has already found an error on the matchingControl
      return;
    }

    // set error on matchingControl if validation fails
    if (control.value !== matchingControl.value) {
      matchingControl.setErrors({mustMatch: true});
    } else {
      matchingControl.setErrors(null);
    }
  }


}
