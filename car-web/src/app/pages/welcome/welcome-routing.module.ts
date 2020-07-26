import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
// import {WelcomeComponent} from './welcome.component';
import {UsersManagementComponent} from '../users-management/users-management.component';
// import {SurveyMonitoringComponent} from '../survey-monitoring/survey-monitoring.component';
// import {SurveySearchComponent} from '../survey-search/survey-search.component';
import {VehicleMonitoringComponent} from "../vehicle-monitoring/vehicle-monitoring.component";
import {VehicleManagementComponent} from "../vehicle-management/vehicle-management.component";
import {SearchAdvancedComponent} from "../search-advanced/search-advanced.component";
import {ClientManagementComponent} from "../client-management/client-management.component";
import {ClientMonitoringComponent} from "../client-monitoring/client-monitoring.component";
// import {AuthGaurdService} from "../../guard/auth-gaurd.service";

const routes: Routes = [
  {path: 'client', component: ClientManagementComponent },
  {path: 'users', component: UsersManagementComponent },
  {path: 'monitoring', component: ClientMonitoringComponent },
  // {path: 'search', component: SurveySearchComponent },
  {path: 'v-monitoring', component: VehicleMonitoringComponent },
  {path: 'v-management', component: VehicleManagementComponent },
  {path: 'search', component: SearchAdvancedComponent },


];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class WelcomeRoutingModule {
}
