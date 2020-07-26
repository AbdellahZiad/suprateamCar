import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {AuthGaurdService} from "./guard/auth-gaurd.service";

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: '/welcome',canActivate:[AuthGaurdService] },
  { path: 'welcome', loadChildren: () => import('./pages/welcome/welcome.module').then(m => m.WelcomeModule)}
];



@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
