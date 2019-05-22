import { Component, OnInit } from '@angular/core';
import { PageEvent, MatPaginatorIntl } from '@angular/material';
import { PillAndMeasurementReminderEntries } from '../../models/PillAndMeasurementReminderEntries';
import { HistoryService } from '../../services/history.service';
import { ISelect } from '../../models/ISelect';

export class HistoryMatPaginatorIntl extends MatPaginatorIntl {
  itemsPerPageLabel = 'Период в днях';
  nextPageLabel     = 'Следующая';
  previousPageLabel = 'Предыдущая';

  getRangeLabel = function (page, pageSize, length) {
    if (length === 0 || pageSize === 0) {
      return '0 по ' + length;
    }
    length = Math.max(length, 0);
    const startIndex = page * pageSize;
    // If the start index exceeds the list length, do not try and fix the end index to the end.
    const endIndex = startIndex < length ?
      Math.min(startIndex + pageSize, length) :
      startIndex + pageSize;
    return "с " + startIndex + 1 + ' по ' + endIndex;
  };

}

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.css']
})
export class HistoryComponent implements OnInit {

  pillAndMeasurementReminderEntriesArr: PillAndMeasurementReminderEntries[] ;

  startDate: Date;
  endDate: Date;

  //********************************pagination***********************************
  disNavBefore = true;
  disNavNext = false;

  onNavBefore(){
    let today = new Date();
    this.startDate.setTime(this.startDate.getTime() + this.getMilSecInDays(this.selectedSizeType + 1));
    if (this.compareDateWithoutTime(today, this.startDate) > 0){
      this.startDate = new Date(this.startDate);
    }
    else{
      this.disNavBefore = true;
      this.startDate = new Date(today);
    }
    this.endDate = new Date(this.endDate.setTime(this.startDate.getTime() - this.getMilSecInDays(this.selectedSizeType)));
    this.downloadHistoryData();
  }

  onNavNext(){
    this.startDate = new Date(this.startDate.setTime(this.endDate.getTime() - this.getMilSecInDays(1)));
    this.endDate = new Date(this.endDate.setTime(this.startDate.getTime() - this.getMilSecInDays(this.selectedSizeType)));
    this.downloadHistoryData();
    this.disNavBefore = false;
  }

  compareDateWithoutTime(date1: Date, date2: Date): number{
    let d1 = new Date(date1);
    let d2 = new Date(date2);
    d1.setHours(0, 0, 0, 0);
    d2.setHours(0, 0, 0, 0);
    if (d1.getTime()>d2.getTime())
      return 1;
    else if (d1.getTime()<d2.getTime())
      return -1;
      else
        return 0;
  }

  getMilSecInDays(daysCount: number): number{
    return daysCount * 86400000;
  }
  //************************************************************************** */

  constructor(private historyService: HistoryService) { 
    this.startDate = new Date();
    this.endDate = new Date();
    this.startDate.setHours(12, 0, 0, 0);
    this.endDate.setHours(12, 0, 0, 0);
    this.endDate.setDate(this.startDate.getDate() - this.selectedSizeType);
    //console.log(this.startDate);
    //console.log(this.endDate);
  }

  selectedSizeType = 7;
  sizeTypes: ISelect[] = [
    {value: 7, viewValue: '7'},
    {value: 30, viewValue: '30'}
  ];

  ngOnInit() {
    //let startDate = new Date("2019-05-21T00:01:00.000+0300");
    //let endDate = new Date("2019-05-18T00:01:00.000+0300");
    this.downloadHistoryData();

  }

  downloadHistoryData(){
    this.historyService.getHistoryForPeriod(this.startDate, this.endDate)
      .subscribe(pillAndMeasurementReminderEntriesArr => {
        this.pillAndMeasurementReminderEntriesArr = pillAndMeasurementReminderEntriesArr;
        console.log(pillAndMeasurementReminderEntriesArr);
      });
  }

  ngAfterViewInit() {
    let numberSpan = document.querySelector('.bar-title');
      numberSpan.textContent = "История";
  }

}
