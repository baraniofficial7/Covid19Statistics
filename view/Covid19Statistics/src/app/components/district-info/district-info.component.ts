import { Component, OnInit } from '@angular/core';
import {Covid19Service} from 'src/app/services/covid19.service';
import {Router,ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-district-info',
  templateUrl: './district-info.component.html',
  styleUrls: ['./district-info.component.css']
})
export class DistrictInfoComponent implements OnInit {
  states: any = [];
  districts: any = [];
  statecode: String = '';
  selectedState:any;
  showLoader:Boolean=false;
  count:any = 0;
  activeTotal:any=0;
  recoveredTotal:any=0;
  deathTotal:any=0;
  sortConfirm:Boolean = false;
  sortAct:Boolean = false;
  sortRecover:Boolean = false;
  sortDead:Boolean = false;
  columns = ['confirmed', 'active', 'recovered' ,'deceased'];

  constructor(private covid19Service : Covid19Service,
    private activatedRoute:ActivatedRoute,
    private router:Router) { }

  ngOnInit() {
    this.statecode=this.activatedRoute.snapshot.paramMap.get('statecode');
    this.selectedState=this.statecode;
    this.covid19Service.getDistrict(this.statecode).subscribe((district)=>{
      this.districts=district['district'];
      this.count=district['total'];
      this.activeTotal=district['activeTotal'];
      this.recoveredTotal=district['recoveredTotal'];
      this.deathTotal=district['deathTotal'];
    });
    this.covid19Service.getStateAlone().subscribe((states)=>{
      this.states=states['state'];
      this.showLoader=true;
    });
  }
  changeStateName(value){
    this.statecode=value;
    this.selectedState=this.statecode;
    this.router.navigate(['/districts',this.statecode])
    this.covid19Service.getDistrict(this.statecode).subscribe((district)=>{
      this.districts=district['district'];
      this.count=district['total'];
      this.activeTotal=district['activeTotal'];
      this.recoveredTotal=district['recoveredTotal'];
      this.deathTotal=district['deathTotal'];
    });
  }

  sortConfirmed(sortConfirm, index){
    this.sortConfirm = sortConfirm;
    this.covid19Service.sort(this.districts, sortConfirm, this.columns[index]);
  }
  sortActive(sortAct, index){
    this.sortAct = sortAct;
    this.covid19Service.sort(this.districts, sortAct, this.columns[index]);
  }
  sortRecovered(sortRecover, index){
    this.sortRecover = sortRecover;
    this.covid19Service.sort(this.districts, sortRecover, this.columns[index]);
  }
  sortDeath(sortDead, index){
    this.sortDead = sortDead;
    this.covid19Service.sort(this.districts, sortDead, this.columns[index]);
  }

}
