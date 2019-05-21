import { Component, OnInit } from '@angular/core';
import { ReminderItemsService } from '../../services/reminder-items.service';
import { PillReminder } from '../../models/PillReminder';

@Component({
  selector: 'app-pills',
  templateUrl: './pills.component.html',
  styleUrls: ['./pills.component.css']
})
export class PillsComponent implements OnInit {

  pillReminderItems: PillReminder[];

  constructor(private reminderItemsService: ReminderItemsService) { }

  ngOnInit() {
    this.reminderItemsService.getAllPillReminders()
      .subscribe(pillReminderItems => {this.pillReminderItems = pillReminderItems;
      console.log(pillReminderItems);
      });
  }

  ngAfterViewInit() {
    let numberSpan = document.querySelector('.bar-title');
      numberSpan.textContent = "Медикаменты";
  }

  /*public addPillReminder(event: any) {
    console.log(event);
  }*/
}
