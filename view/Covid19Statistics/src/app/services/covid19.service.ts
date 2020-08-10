import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class Covid19Service {

  constructor(private http: HttpClient) { }

  getDistrict(statecode) {
    return this.http.get<any[]>('/covid19india/districts?statecode='+statecode);
  }
  getState(){
    return this.http.get<any[]>('/covid19india/states');
  }

  getStateAlone(){
    return this.http.get<any[]>('/covid19india/statesAlone');
  }

  sort(states, sortReverse, columnName) {
    return states.sort((state1, state2) => {
      if(!sortReverse){
        return state2[columnName] - state1[columnName];
      } else {
        return state1[columnName] - state2[columnName];
      }
    });
   }

}
