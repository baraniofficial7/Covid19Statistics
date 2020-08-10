import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { StateInfoComponent } from './components/state-info/state-info.component';
import { DistrictInfoComponent } from './components/district-info/district-info.component';


const routes: Routes = [
  { path:'',redirectTo:'state', pathMatch:'full'},
  { path: 'state', component: StateInfoComponent },
  { path: 'districts/:statecode', component: DistrictInfoComponent },
  { path:'**',component: StateInfoComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
