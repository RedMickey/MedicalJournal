import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UtilsService {

  constructor() { }

  createHavingMealsTypePretext(havingMealsType: number): string{
    switch(havingMealsType){
      case 1:
        return "до"
      case 2:
        return "с"
      case 3:
        return "после"
      default:
        return "";
    }
  }
  
}
