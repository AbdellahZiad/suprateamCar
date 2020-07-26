import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {WelcomeComponent} from './welcome.component';
import {UsersManagementComponent} from '../users-management/users-management.component';
import {SurveyMonitoringComponent} from '../survey-monitoring/survey-monitoring.component';
import {SurveySearchComponent} from '../survey-search/survey-search.component';
import {AuthGaurdService} from "../../guard/auth-gaurd.service";

const routes: Routes = [
  {path: 'template', component: WelcomeComponent,canActivate:[AuthGaurdService] },
  {path: 'users', component: UsersManagementComponent,canActivate:[AuthGaurdService] },
  {path: 'monitoring', component: SurveyMonitoringComponent,canActivate:[AuthGaurdService] },
  {path: 'search', component: SurveySearchComponent,canActivate:[AuthGaurdService] },


];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class WelcomeRoutingModule {
}
