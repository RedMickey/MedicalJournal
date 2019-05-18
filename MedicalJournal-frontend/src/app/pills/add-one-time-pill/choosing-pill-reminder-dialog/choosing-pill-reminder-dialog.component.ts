import { Component, OnInit } from '@angular/core';
import { ReminderItemsService } from '../../../services/reminder-items.service';
import { PillReminder } from '../../../models/PillReminder';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';

@Component({
  selector: 'app-choosing-pill-reminder-dialog',
  templateUrl: './choosing-pill-reminder-dialog.component.html',
  styleUrls: ['./choosing-pill-reminder-dialog.component.css']
})
export class ChoosingPillReminderDialogComponent implements OnInit {

  pillReminderItems: PillReminder[];

  constructor(private reminderItemsService: ReminderItemsService,
    public dialogRef: MatDialogRef<ChoosingPillReminderDialogComponent>) { }

  ngOnInit() {
    this.reminderItemsService.getAllPillReminders()
      .subscribe(pillReminderItems => {this.pillReminderItems = pillReminderItems;
      console.log(pillReminderItems);
      });
  }

  onItemSelected(index){
    this.onDialogResult(index);
  }

  onNoItemSelected(){
    this.onDialogResult(-1);
  }

  onDialogResult(index: number): void {
    if (index>=0)
      this.dialogRef.close({
        pillReminderItem: this.pillReminderItems[index],
        isItemSelected: true
      });
    else
      this.dialogRef.close({isItemSelected: false});
  }

}
