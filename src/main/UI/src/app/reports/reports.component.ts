import { Component } from '@angular/core';
import {ReportService} from "../report.service";
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-reports',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './reports.component.html',
  styleUrl: './reports.component.css'
})
export class ReportsComponent {
  userActivityReport: any[] | undefined;
  petsWithMostRecipesReport: any[] | undefined;
  currentDateTime: string | undefined;
  currentDateTimeTwo: string | undefined;


  constructor(private reportService: ReportService) {
  }

  loadUserActivityReport(): void {
    this.reportService.userEngagementReport().subscribe({
      next: report => {
        this.userActivityReport = report
        this.currentDateTime = new Date().toLocaleDateString() + ' at ' + new Date().toLocaleTimeString();
      }
    })
  }

  loadPetsWithMostRecipesReport():void {
    this.reportService.petsWithMostRecipesReport().subscribe({
      next: report => {
        this.petsWithMostRecipesReport = report
        this.currentDateTimeTwo = new Date().toLocaleDateString() + ' at ' + new Date().toLocaleTimeString();
      }
    })
  }
}
