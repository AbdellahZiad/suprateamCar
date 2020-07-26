import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {SurveyService} from "../welcome/services/survey.service";
import {SurveyModel} from "../../model/SurveyModel";

@Component({
  selector: 'app-survey-search',
  templateUrl: './survey-search.component.html',
  styleUrls: ['./survey-search.component.scss']
})
export class SurveySearchComponent implements OnInit {
  dateFormat = 'dd/MM/yyyy';
  listOfData: SurveyModel[];
  validateForm!: FormGroup;
  controlArray: Array<{ index: number; show: boolean }> = [];
  isCollapse = false;
  isFormCollapsed: boolean;
  listSurveyFilter: SurveyModel;


  constructor(private fb: FormBuilder, private surveyService: SurveyService) {
  }

  toggleCollapse(): void {
    this.isCollapse = !this.isCollapse;
    // this.controlArray.forEach((c, index) => {
    //   c.show = this.isCollapse ? index < 6 : true;
    // });
  }


  ngOnInit(): void {
    this.validateForm = this.fb.group({
      surveyID: null,
      submittedOn: null,
      agent: null,
      by: null,
      scoreMin: null,
      scoreMax: null,
      statusSystem: null,
    });
    this.getSurveyList();
  }

  resetForm(): void {
    this.validateForm.reset();
  }


  getSurveyList() {
    this.surveyService.getSurveyUserList()
      .subscribe(data => {
        console.log('data =>', data);
        this.listOfData = Object.assign(data);
        console.log("--> getSurveyList LIST OF DATA", this.listOfData);
      });
  }

  // searchData() {
  //   this.surveyService
  //       .searchSurvey(this.validateForm).subscribe(
  //           survey=> {
  //             console.log("-------> Search survey :",survey);
  //             this.listOfData = survey;
  //           }
  //   )
  // }

  searchData() {
    this.surveyService.getSurveyUserList()
      .subscribe((data: SurveyModel[]) => {
        this.listSurveyFilter = this.validateForm.value;
        console.log("------------> FILTER = ", this.listSurveyFilter);
        console.log("------------> DATA = ", data);

        this.listOfData = data;
        // if (this.listOfData[0].dateCreate < this.listSurveyFilter.submittedOn[0])
        console.log("------------>      this.listOfData[0].dateCreate = ", this.listOfData[0].dateCreate);
        console.log("------------>      this.listSurveyFilter.submittedOn[0] = ", this.listSurveyFilter.submittedOn[0]);
        console.log("------------>      this.listSurveyFilter.submittedOn[1] = ", this.listSurveyFilter.submittedOn[1]);
        console.log("------------>      this.listOfData = ", this.listOfData);
        // if (this.listOfData[0].dateCreate.getTime() <= this.listSurveyFilter.submittedOn[0].getTime())
        //   console.log("this.listOfData[0].dateCreate <= this.listSurveyFilter.submittedOn[0]");
        // if (this.listOfData[0].dateCreate.getTime() >= this.listSurveyFilter.submittedOn[1].getTime())
        //   console.log("this.listOfData[0].dateCreate >= this.listSurveyFilter.submittedOn[1] ");

        this.listOfData = this.listOfData.filter(
          survey =>
            (this.listSurveyFilter.surveyID ? this.listSurveyFilter.surveyID.toLowerCase() === survey.surveyID.toLowerCase() : true)
            &&
            (this.listSurveyFilter.by ? this.listSurveyFilter.by.toLowerCase() === survey.by.toLowerCase() : true)
            &&
            (this.listSurveyFilter.agent ? this.listSurveyFilter.agent.toLowerCase() === survey.agent.toLowerCase() : true)
            &&
            (this.listSurveyFilter.statusSystem ? this.listSurveyFilter.statusSystem.toLowerCase() === survey.statusSystem.toLowerCase() : true)
            &&
            ((this.listSurveyFilter.scoreMax && this.listSurveyFilter.scoreMin) ?
              (survey.qualityScore <= this.listSurveyFilter.scoreMax && survey.qualityScore >= this.listSurveyFilter.scoreMin) : true)
            && ((this.listSurveyFilter.submittedOn[0] && this.listSurveyFilter.submittedOn[1]) ?
            this.listSurveyFilter.submittedOn[0].getTime() <= new Date(survey.dateCreate).getTime()
            && this.listSurveyFilter.submittedOn[1].getTime() >= new Date(survey.dateCreate).getTime()
            : true)
        );
      });
    // this.getSurveyList();


    console.log('this.listDataSurvey After = ', this.listOfData);

  }
}
