import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {endpoint} from '../../../../environments/environment';
import {saveAs} from 'file-saver';


@Injectable()
export class SurveyService {
  constructor(private http: HttpClient) {
  }

  uploadFile(file) {

    let formData = new FormData();
    formData.append("file", file, file.name);
    const uploadBy = "Abdellah ZIAD";
    return this.http.post(endpoint + 'api/upload/'+uploadBy, formData, {responseType:"text"});

    // const uploadHttpOptions = {
    //   headers: new HttpHeaders({'Accept': 'application/json'})
    // };
    // const formData = new FormData();
    // formData.append('file', x.file.originFileObj);
    // const uploadBy = "Abdellah ZIAD";
    // return this.http.post(endpoint + 'api/upload/'+uploadBy, formData, uploadHttpOptions);

  }

  getSurveyList() {
    return this.http.get(endpoint + 'api/survey/template');
  }

  getSurveyUserList() {
    return this.http.get(endpoint + 'api/survey/surveys');
  }

  deleteSurvey(id: any) {
    console.log("id from service = ",id);
    return this.http.delete(endpoint + "api/survey/delete/"+id,{responseType: 'text'});

  }

  reload(id) {

    console.log("id from service = ",id);
    return this.http.get(endpoint + "api/survey/reload/"+id,{responseType: 'text'});
  }

  downloadFileExcel(survey) {
    // return this.http.get(endpoint + "api/export/"+id).subscribe(
    //   (res) => {
    //     return new Blob([res.blob()], { type: 'application/vnd.ms-excel' });
    //   });
    let url = endpoint + "api/export/"+survey.id;
    this.http.get(url, {responseType: 'blob'}).subscribe((x: Blob) => {
      saveAs(x, survey.templateName);
    });

  }

  updateStatusSurvey(decision,id) {
    console.log("ID = ",id);
    console.log("decision = ",decision);
    let param = decision+"/"+id;
    console.log("---param ",param);

    return this.http.get(endpoint + 'api/survey/update/'+param, {responseType:"text"});
  }

  getSurveyDetails(id: any) {
    return this.http.get(endpoint + "api/details/"+id,{responseType: 'json'});

  }

  searchSurvey(validateForm: any) {
    return this.http.post(endpoint + "api/details/",validateForm);

  }
}
