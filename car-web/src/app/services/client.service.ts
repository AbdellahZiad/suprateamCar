import { Injectable } from '@angular/core';
import {endpoint} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  constructor(private http: HttpClient) { }

  saveClient(clinet) {
    console.log("ADD or SAVE client ",clinet);
    return this.http.post(endpoint + "api/client/add", clinet);
  }

  deleteClient(id: any) {
    console.log("id from service = ",id);
    return this.http.delete(endpoint + "api/client/delete/"+id,
      {responseType: 'text'});
  }

  getAllClient() {
    return this.http.get(endpoint + "api/client");
  }

  getAllVoiture() {
    return this.http.get(endpoint + "api/voiture");

  }

  search(search: any) {
    return this.http.get(endpoint + "api/client/search?filter="+search);

  }
}
