import { Injectable } from '@angular/core';
import {endpoint} from "../../../../environments/environment";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class MediaService {

  constructor(private http: HttpClient) { }

  getAllMedia(id: any) {
    return this.http.get(endpoint + 'api/media/'+id);

  }

  getMediaById(id: any) {
    return this.http.get(endpoint + 'api/media/m/'+id);

  }
}
