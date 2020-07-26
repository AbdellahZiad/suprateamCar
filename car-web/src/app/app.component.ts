import {Component, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, NgForm, Validators} from "@angular/forms";
import {NzMessageService} from "ng-zorro-antd";
import {AuthenticationService} from "./services/authentication.service";
import {ActivationStart, Router, RouterOutlet} from "@angular/router";
import {LoginUser} from "./model/LoginUser";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit{
  isCollapsed = false;
  isVisible = false;
  passwordVisible = false;
  validateForm!: FormGroup;
  //login
  form: any = {};
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';
  roles: string[] = [];
  loginUser : LoginUser;

  @ViewChild(RouterOutlet) outlet: RouterOutlet;


  constructor(private msg: NzMessageService,
              private fb: FormBuilder,
              private loginservice:AuthenticationService,
              private router: Router) {

    this.validateForm = this.fb.group({
      id: '',
      username: [null, [Validators.email, Validators.required]],
      password: [null, [Validators.required]],
    });


  }

  ngOnInit(): void {
    // this.isAuth = this.loginservice.isNotAgent();
    // this.isAdmin = this.loginservice.isAdmin();
  }

  //login
  isAuth: any = false;
  user: any;
  emailPattern: any;
  error: boolean =true;
  loading: any = false;
  isAdmin: any = true;

a

  reloadPage() {
    window.location.reload();
  }

  checkLogin() {
    // this.loginUser = this.validateForm.value;
    // console.log("----------->this.loginUser",this.loginUser);
    //
    // (this.loginservice.authenticate(this.loginUser).subscribe(
    //     jwt => {
    //       console.log("----------------->DATA",jwt);
    //
    //       if(jwt && jwt.token) {
    //         let jwtData = jwt.token.split('.')[1];
    //         let decodedJwtJsonData = window.atob(jwtData);
    //         let decodedJwtData = JSON.parse(decodedJwtJsonData);
    //         console.log("-------------->jwtData", jwtData);
    //         console.log("-------------->decodedJwtData", decodedJwtData);
    //         console.log("------------>decodedJwtData.role && decodedJwtData.role.toLowerCase()", decodedJwtData.role);
    //
    //         this.isAdmin = decodedJwtData.role && decodedJwtData.role.toLowerCase() == "admin";
    //         console.log("----------/////----> ROLE = ", this.isAdmin);
    //         if(decodedJwtData.role.toLowerCase() != "agent") {
    //           this.router.navigateByUrl('/welcome', {skipLocationChange: true}).then(() => {
    //             this.router.navigate(['/welcome/template']);
    //           });
    //           this.error = false;
    //           this.isAuth = true;
    //           this.msg.success("Connection successfully");
    //         }
    //         else
    //           this.msg.error("ERROR : Agents can't access to User interface, please contact administration  for any information or question")
    //       }
    //     },
    //     error => {
    //       this.error = true;
    //       console.log("-------------> ERROR :Invalid username or password.");
    //       this.msg.error("ERROR :Invalid email or password.");
    //       this.errorMessage = error.message;
    //       this.isAuth = false;
    //
    //
    //     }
    //   )
    // );

    this.isAuth = true;
  }

  logout() {
    console.log("LOG OUT");
    // this.loginservice.logOut();
    this.isAuth = false;
    // this.
    // this.router.navigateByUrl('/RefreshComponent', { skipLocationChange: true }).then(() => {
    //   this.router.navigate(['']);
    // });
  }

}
