import { Component, OnInit, Inject } from '@angular/core';
import {MAT_BOTTOM_SHEET_DATA} from '@angular/material';
import {MatBottomSheet, MatBottomSheetRef} from '@angular/material';

@Component({
  selector: 'app-choosing-measurement-type-dialog',
  templateUrl: './choosing-measurement-type-dialog.component.html',
  styleUrls: ['./choosing-measurement-type-dialog.component.css']
})
export class ChoosingMeasurementTypeDialogComponent implements OnInit {

  constructor(@Inject(MAT_BOTTOM_SHEET_DATA) public data: any,
    private bottomSheetRef: MatBottomSheetRef<ChoosingMeasurementTypeDialogComponent>) {
    console.log(this.data);
   }

  ngOnInit() {
  }

  openLink(event: MouseEvent): void {
    this.bottomSheetRef.dismiss();
    //event.preventDefault();
  }

}
