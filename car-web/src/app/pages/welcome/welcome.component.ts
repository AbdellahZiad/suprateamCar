import {Component, OnInit, ViewChild} from '@angular/core';
import {NzMessageService, NzModalService, UploadChangeParam} from 'ng-zorro-antd';
import {SurveyService} from './services/survey.service';
import {ActivationStart, Router, RouterOutlet} from "@angular/router";

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.scss']
})
export class WelcomeComponent implements OnInit {
  isVisible = false;
  listOfData = [];
  listOfDataFake = [{
    templateName: 'templateSurvey3.xls',
    loadedOn: '07/05/2020 at 10:00:30',
    loadedBy: 'John SMITH',
    status: '',
    reloadTmp: '',
    download: ''
  }];
  private fileExcel:any;
  status: string;
  private id: any;

  @ViewChild(RouterOutlet) outlet: RouterOutlet;

  constructor(private msg: NzMessageService,
              private modal: NzModalService,
              private surveyService: SurveyService,
              private router: Router) {

    this.router.events.subscribe(e => {
      if (e instanceof ActivationStart && e.snapshot.outlet === "root")
        this.outlet.deactivate();
    });
  }

  ngOnInit() {
    this.getSurveyList();
  }

  showModal(): void {
    this.uploadMsg = "Click or drag file to this area to upload";
    this.isVisible = true;
  }

  handleOk(): void {

    console.log("OKey");
    let isValid = (this.fileExcel.type === "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    if (!isValid) {
      this.msg.error('You can only upload Excel file!');
      this.fileExcel.status = 'error'
    } else {

      this.surveyService.uploadFile(this.fileExcel)
        .subscribe(data => {
          this.fileExcel.status = 'done';
          console.log("------------> response ",data);
          if (data.toLowerCase()!=='done')
            this.msg.error(data);
          else
            this.msg.success('Template uploaded successfully');


            this.getSurveyList();
        },
          error =>
          {
            this.fileExcel.status = "error";
            console.log("error",error);
            // this.msg
            //   .error('ERROR : '+this.message()
            //     .replace(new RegExp('\n', 'g'), "<br />"));

            this.modal.error({
              nzTitle: 'Error',
              nzContent: JSON.parse(error.error).message.replace(new RegExp('\n', 'g'), "<br />"),
            });

            // this.msg.create(
            //   'error',
            //   'Erreur: ' + error.message
            // );

          });
      // this.fileExcel.status = 'done'
    }

    this.isVisible = false;
  }

  handleCancel(): void {
    console.log('Button cancel clicked!');
    this.isVisible = false;
  }

  handleChange({file, fileList}: UploadChangeParam): void {
    this.fileExcel = {file, fileList};

    // const status = file.status;
    // if (status !== 'uploading') {
    //   console.log(file, fileList);
    //   this.surveyService.uploadFile({file, fileList});
    // }
    // if (status === 'done') {
    //   this.msg.success(`${file.name} file uploaded successfully.`);
    // } else if (status === 'error') {
    //   this.msg.error(`${file.name} file upload failed.`);
    // }
  }



  fileChange = (file) => {
    this.fileExcel = file;
    let isValid = (file.type === "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    if (!isValid) {
      this.msg.error('You can only upload Excel file!');
      file.status = 'error'
    } else {

      this.surveyService.uploadFile(this.fileExcel)
        .subscribe(data => {
            this.fileExcel.status = 'done';
            console.log("------------> response ",data);
            if (data.toLowerCase()!=='done')
              this.msg.error(data);
            else
              this.msg.success('Template uploaded successfully');


            this.getSurveyList();
          },
          error =>
          {
            this.fileExcel.status = "error";
            console.log("error",error);
            // this.msg
            //   .error('ERROR : '+this.message()
            //     .replace(new RegExp('\n', 'g'), "<br />"));

            this.modal.error({
              nzTitle: 'Error',
              nzContent: JSON.parse(error.error).message.replace(new RegExp('\n', 'g'), "<br />"),
            });

            // this.msg.create(
            //   'error',
            //   'Erreur: ' + error.message
            // );

          });
      // this.fileExcel.status = 'done'
    }

    this.isVisible = false;
  }
  uploadMsg: any;


  getSurveyList() {
    this.surveyService.getSurveyList().subscribe(data => {
      console.log('data =>', data);
      this.listOfData = Object.assign(data);
    });
  }

  download(survey) {
    this.surveyService.downloadFileExcel(survey);
  }

  deleteSurvey(id: any) {
    this.id = id;
    this.modal.confirm({
      nzTitle: 'Confirm Delete ',
      nzContent: "Are you sure you want to delete this survey?",
      nzOkText: 'confirm Delete ',
      nzCancelText: 'Cancel Delete',
      nzOnOk: () => this.confirmDelete(),
      nzOnCancel: () => this.closeSubscription()
    });

  }


  private confirmDelete() {

    this.surveyService.deleteSurvey(this.id)
      .subscribe(
        data => {
          this.msg.success("Template deleted successfully");
          this.getSurveyList()
        });


  }

  private closeSubscription() {
    console.log("Cancel");

  }

  showMsg(data) {
    this.modal.confirm({
      nzTitle: 'Error',
      nzContent: data,
      nzOkText: 'OK ',
      nzOnOk: () => this.showMessage(data),
    });

  }

  private showMessage(data) {

  }

  reload(id) {
    this.surveyService.reload(id).subscribe(
      data=> {
        console.log("Data ",data);
        this.getSurveyList()
      }
    );
  }

  private message() {
    return "Incompatible file, you must upload an Excel file and respect the basic template \n" +
      "Excel template must contain sheets : \n" +
      "- Company            \n" +
      "- Survey             \n" +
      "- Section            \n" +
      "- Question           \n" +
      "- Answers            \n" +
      "- Ocp (for occupancy)\n" +
      "- NatCat inputs      \n" +
      "- Nat Cat rates).    \n" ;
  }
}
