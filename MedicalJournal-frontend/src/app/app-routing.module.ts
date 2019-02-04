import { NgModule } from '@angular/core';

import { RouterModule, Routes } from '@angular/router';

import { TodayComponent } from './today/today.component';

const routes: Routes = [
  { path: '', component: TodayComponent}
];

@NgModule({
  declarations: [],
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [
    RouterModule
  ]
})

export class AppRoutingModule { }
