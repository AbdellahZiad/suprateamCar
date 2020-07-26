import {NgModule} from '@angular/core';
import {WelcomeRoutingModule} from './welcome-routing.module';
import {NzTableModule} from 'ng-zorro-antd/table';
// import {WelcomeComponent} from './welcome.component';
import {NzDividerModule} from 'ng-zorro-antd/divider';
import {CommonModule} from '@angular/common';
import {NzModalModule} from 'ng-zorro-antd/modal';
import {NzUploadModule} from 'ng-zorro-antd/upload';
import {NzIconModule, NzInputModule, NzMessageService, NzSpinModule} from 'ng-zorro-antd';
import {SurveyService} from './services/survey.service';
import {NzButtonModule} from 'ng-zorro-antd/button';
import {UsersManagementComponent} from '../users-management/users-management.component';
// import {SurveyMonitoringComponent} from '../survey-monitoring/survey-monitoring.component';
// import {SurveySearchComponent} from '../survey-search/survey-search.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {NzCollapseModule} from 'ng-zorro-antd/collapse';
import {NzFormModule} from 'ng-zorro-antd/form';
import {NzSelectModule} from 'ng-zorro-antd/select';
import {NzDatePickerModule} from 'ng-zorro-antd/date-picker';
import {NzInputNumberModule} from 'ng-zorro-antd/input-number';
import {TableModule} from 'primeng/table';
import {NzTabsModule} from 'ng-zorro-antd/tabs';
import {
  ButtonModule,
  CarouselModule,
  DataViewModule,
  DialogModule,
  DropdownModule, PanelModule,
  ScrollPanelModule,
  TabViewModule
} from "primeng";
import { VehicleManagementComponent } from '../vehicle-management/vehicle-management.component';
import { VehicleMonitoringComponent } from '../vehicle-monitoring/vehicle-monitoring.component';
import { SearchAdvancedComponent } from '../search-advanced/search-advanced.component';
import { ClientManagementComponent } from '../client-management/client-management.component';
import { ClientMonitoringComponent } from '../client-monitoring/client-monitoring.component';

@NgModule({
  imports: [WelcomeRoutingModule, NzTableModule, NzDividerModule, CommonModule, NzModalModule, NzUploadModule,
    NzButtonModule, FormsModule, ReactiveFormsModule, NzCollapseModule, NzFormModule, NzInputNumberModule, NzTabsModule,
    NzIconModule, NzSelectModule, NzDatePickerModule, TableModule, NzInputModule, NzSpinModule, TabViewModule, DialogModule, ButtonModule, CarouselModule, ScrollPanelModule, DataViewModule, DropdownModule, PanelModule
  ],
  declarations: [ UsersManagementComponent, VehicleManagementComponent, VehicleMonitoringComponent, SearchAdvancedComponent, ClientManagementComponent, ClientMonitoringComponent],
  exports: [ClientManagementComponent],
  providers: [NzMessageService, SurveyService]
})
export class WelcomeModule {
}
