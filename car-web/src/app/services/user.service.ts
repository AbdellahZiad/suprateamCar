import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {UserModel} from "../model/UserModel";
import {endpoint} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) {
  }


  getAllUser() {
    return this.http.get(endpoint + "api/user");
  }

  deleteUser(id: any) {
    console.log("id from service = ",id);
    return this.http.delete(endpoint + "api/user/delete/"+id,
      {responseType: 'text'});

  }

  saveUser(user: UserModel) {
    console.log("ADD or SAVE USER ",user);
    return this.http.post(endpoint + "api/user/add", user);

  }
}
