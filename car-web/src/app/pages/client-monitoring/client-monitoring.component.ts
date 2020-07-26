import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {SurveyModel} from "../../model/SurveyModel";
import {NzMessageService, NzModalService} from "ng-zorro-antd";
import {SurveyService} from "../welcome/services/survey.service";
import {MediaService} from "../welcome/services/media.service";
import {AuthenticationService} from "../../services/authentication.service";
import {SurveyDetalsModel} from "../../model/SurveyDetalsModel";
import {MediaModel} from "../../model/MediaModel";

@Component({
  selector: 'app-client-monitoring',
  templateUrl: './client-monitoring.component.html',
  styleUrls: ['./client-monitoring.component.scss']
})
export class ClientMonitoringComponent implements OnInit {
  listOfData = [];
  listOfData2 = [];
  myFormGroup:FormGroup;
  // formTemplate:any = form_template;
  mediaImage: any;
  mediaVideos: any;
  mediaAudios: any;
  mediaOthers: any;
  // = [
  // {"brand": "mobapp.png", "year": 2012, "color": "Orange", "vin": "dsad231ff"},
  // {"brand": "ui.png", "year": 2011, "color": "Black", "vin": "gwregre345"},
  // {"brand": "ui.png", "year": 2005, "color": "Gray", "vin": "h354htr"},
  // {"brand": "mobapp.png", "year": 2005, "color": "Gray", "vin": "h354htr"},
  // {"brand": "mobapp.png", "year": 2003, "color": "Blue", "vin": "j6w54qgh"},
  // {"brand": "mobapp.png", "year": 1995, "color": "Orange", "vin": "hrtwy34"},
  // {"brand": "mobapp.png", "year": 2005, "color": "Black", "vin": "jejtyj"},
  // {"brand": "mobapp.png", "year": 2012, "color": "Yellow", "vin": "g43gr"},
  // {"brand": "mobapp.png", "year": 2013, "color": "Orange", "vin": "greg34"},
  // {"brand": "mobapp.png", "year": 2000, "color": "Black", "vin": "h54hw5"},
  // {"brand": "mobapp.png", "year": 2013, "color": "Red", "vin": "245t2s"}

  listOfMedia : any;
  panels = [
    {
      active: true,
      name: 'Pending Survey Referral',
      disabled: false
    },
    {
      active: false,
      disabled: false,
      name: 'Processed Survey'
    }
  ];
  isVisible: boolean = false;
  isVisibleVideo: boolean = false;
  validateForm: FormGroup;
  private survey: SurveyModel;
  saveLoading: boolean = true;
  validateForm2: FormGroup;

  listOfData3: any;
  current: any = 0;
  index: number = 0;
  decision="";
  saveDecision: boolean = false;
  private id: any;
  showDecisionSelect: boolean = true;
  isVisiblePicture: boolean = false;

  isVisibleAudio: boolean = false;
  mediaContent: any;


  constructor(private msg: NzMessageService,
              private modal: NzModalService,
              private fb: FormBuilder,
              private surveyService: SurveyService,
              private mediaService: MediaService,
              public dialog: NzModalService,
              private loginservice:AuthenticationService) {

    this.validateForm = this.fb.group({
      id: '',
      surveyID: null,
      dateCreate: null,
      agent: null,
      qualityScore: '',
      decision : '',
      referralReason : '',
      declinedReason : '',
    });
    this.validateForm2 = this.fb.group({
      id: '',
      email: [null, [Validators.email, Validators.required]],
      pw: [null, [Validators.required]],
      checkPassword: null,
      fullName: [null, [Validators.required]],
      role: [null],
      companyName: [null, [Validators.required]],
      validUntil: [null, [Validators.required]],
      createDate: '',
      active: '',
    });

  }

  ngOnInit() {
    this.getSurveyList();
  }

  getSurveyList() {
    this.surveyService.getSurveyUserList().subscribe(data => {
      console.log('data =>', data);
      this.listOfData = Object.assign(data);
      console.log("LIST OF DATA",this.listOfData);
    });
  }

  editSurveyUser(survey,show) {
    console.log('test');
    this.surveyService.getSurveyDetails(survey.id)
      .subscribe(
        (surveyDetails :SurveyDetalsModel) => {
          console.log("----------> Survey Details :",surveyDetails);
          this.listOfData3 = surveyDetails;
          this.id = survey.id;
          this.decision = survey.decision;
          console.log("----------> this.listOfData3 :",this.listOfData3);

        }
      );


    this.mediaService.getAllMedia(survey.id).subscribe(
      (medias:MediaModel[])=> {
        console.log("-------> MEDIA",medias);
        this.mediaImage = this.getMediaImageFromAllMedia(medias);
        console.log("-------> this.mediaImage ",this.mediaImage );

        this.mediaVideos = this.getMediaVideosFromAllMedia(medias);
        console.log("-------> this.mediaVideos ",this.mediaVideos );

        this.mediaAudios = this.getMediaAudiosFromAllMedia(medias);
        console.log("-------> this.mediaAudios ",this.mediaAudios );

        this.mediaOthers = this.getMediaOthersFromAllMedia(medias);
        console.log("------> listOfMedia",this.mediaImage);
      }
    );

    console.log("-----> SHOW",show);
    console.log("-----> showDecisionSelect",this.showDecisionSelect);
    console.log("----->  this.loginservice.isAdmin()", this.loginservice.isAdmin());
    if (!show)
      this.showDecisionSelect = false;

    console.log("-----> showDecisionSelect After",this.showDecisionSelect);

    this.isVisible = true;
  }

  handleCancel(): void {
    console.log('Button cancel clicked!');
    this.isVisible = false;
  }

  handleOk() {

    this.survey = this.validateForm.value;

    console.log("-------> this.decision",this.decision);

    this.surveyService.updateStatusSurvey(this.decision,this.id).subscribe(
      data=> {
        console.log("REEEEE ",data);
        this.getSurveyList();
      }
    );
    this.isVisible = false;

  }

  delete(id: any) {

  }


  updateSurvey() {
    console.log("-------> this.decision",this.decision);

    this.surveyService.updateStatusSurvey(this.decision,this.id).subscribe(
      data=> {
        console.log("REEEEE ",data);
        this.getSurveyList();
      }
    );
    this.isVisible = false;
  }

  showVideo(id) {
    this.mediaService.getMediaById(id).subscribe(
      (data:MediaModel)=> {
        console.log("-----------------> MediaContent ",data);
        this.mediaContent = data.mediaContent;
        // console.log("-------> Video Media",this.mediaContent);
      }
    );
    this.isVisibleVideo = true;
  }


  private getMediaImageFromAllMedia(medias: MediaModel[]) {
    return medias.filter(media => media.mediaType.toLowerCase() == 'images');
  }
  private getMediaVideosFromAllMedia(medias: MediaModel[]) {
    return medias.filter(media=> media.mediaType.toLowerCase() == 'videos');
  }
  private getMediaAudiosFromAllMedia(medias: MediaModel[]) {
    return medias.filter(media=> media.mediaType.toLowerCase() == 'audio');
  }
  private getMediaOthersFromAllMedia(medias: MediaModel[]) {
    return medias.filter(media=> media.mediaType.toLowerCase() == 'others');
  }

  showPicture(id) {
    this.mediaService.getMediaById(id).subscribe(
      (data:MediaModel) => {
        console.log("-----------------> MediaContent ",data);
        this.mediaContent = data.mediaContent;
      }
    );
    this.isVisiblePicture = true;
  }


  showAudio(id) {
    console.log("SHOW AUDIOS");
    this.mediaService.getMediaById(id).subscribe(
      (data:MediaModel)=> {
        console.log("-----------------> MediaContent ",data);
        this.mediaContent = data.mediaContent;
      }
    );
    this.isVisibleAudio = true;
  }
}
