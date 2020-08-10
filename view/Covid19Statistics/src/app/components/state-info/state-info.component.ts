import { Component, OnInit } from '@angular/core';
import {Covid19Service} from 'src/app/services/covid19.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-state-info',
  templateUrl: './state-info.component.html',
  styleUrls: ['./state-info.component.css']
})
export class StateInfoComponent implements OnInit {
  countryName:String='India';
  states: any = [];
  count:any = 0;
  showLoader:Boolean=false;
  activeTotal:any=0;
  recoveredTotal:any=0;
  deathTotal:any=0;
  sortConfirm:Boolean = false;
  sortAct:Boolean = false;
  sortRecover:Boolean = false;
  sortDead:Boolean = false;
  columns = ['confirmed', 'active', 'recoverd' ,'deceased'];

  constructor(private covid19Service : Covid19Service,
    private router:Router) { }

  ngOnInit() {
    this.covid19Service.getState().subscribe((states)=>{
      this.states=states['state'];
      this.count=states['total'];
      this.activeTotal=states['activeTotal'];
      this.recoveredTotal=states['recoveredTotal'];
      this.deathTotal=states['deathTotal'];
      this.showLoader=true;
    });

  }
  onSelect(statecode){
      this.router.navigate(['/districts',statecode]);
  }

  sortConfirmed(sortConfirm, index){
    this.sortConfirm = sortConfirm;
    this.covid19Service.sort(this.states, sortConfirm, this.columns[index]);
  }
  sortActive(sortAct, index){
    this.sortAct = sortAct;
    this.covid19Service.sort(this.states, sortAct, this.columns[index]);
  }
  sortRecovered(sortRecover, index){
    this.sortRecover = sortRecover;
    this.covid19Service.sort(this.states, sortRecover, this.columns[index]);
  }
  sortDeath(sortDead, index){
    this.sortDead = sortDead;
    this.covid19Service.sort(this.states, sortDead, this.columns[index]);
  }

}
