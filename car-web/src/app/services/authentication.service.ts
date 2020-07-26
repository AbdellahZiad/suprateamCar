import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { map } from "rxjs/operators";
import {UserModel} from "../model/UserModel";
import {LoginUser} from "../model/LoginUser";
import {endpoint} from "../../environments/environment";


export class User {
  constructor(public status: string) {}
}

@Injectable({
  providedIn: "root"
})
export class AuthenticationService {
  constructor(private httpClient: HttpClient) {}
// Provide username and password for authentication, and once authentication is successful,
//store JWT token in session
  authenticate(login:LoginUser) {
    return this.httpClient
      .post<any>(endpoint+"token/generate-token", login)
      .pipe(
        map(userData => {
          sessionStorage.setItem("username",login.username);
          sessionStorage.setItem("role",login.username);
          let tokenStr = "Bearer " + userData.token;
          sessionStorage.setItem("token", tokenStr);
          console.log("-----------> tokenStr",tokenStr);
          console.log("-----------> userData",userData);
          return userData;
        })
      );
  }

  isUserLoggedIn() {
    let user = sessionStorage.getItem("username");
    console.log(!(user === null));
    return !(user === null);
  }

  isAdmin()
  {
    let jwt = sessionStorage.getItem("token");
    console.log("------->>> TOKEN From AuthenticationService",jwt);
    if(jwt) {
      let jwtData = jwt.split('.')[1];
      let decodedJwtJsonData = window.atob(jwtData);
      let decodedJwtData = JSON.parse(decodedJwtJsonData);
      console.log("---------> ROLE From AuthenticationService",decodedJwtData.role)
      if (decodedJwtData.role && decodedJwtData.role.toLowerCase() == "admin")
        return true;
    }
    return false;

  }
  isNotAgent() {
    let jwt = sessionStorage.getItem("token");
    console.log("------->>> TOKEN From AuthenticationService", jwt);
    if (jwt) {
      let jwtData = jwt.split('.')[1];
      let decodedJwtJsonData = window.atob(jwtData);
      let decodedJwtData = JSON.parse(decodedJwtJsonData);
      console.log("---------> ROLE From AuthenticationService", decodedJwtData.role)
      if (decodedJwtData.role.toLowerCase() != "agent") return true;
      else return false;
    }
  }

  logOut() {
    console.log("---------------> Remove Item ",sessionStorage.getItem("username"));

    sessionStorage.removeItem("username");
  }
}
