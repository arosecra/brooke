import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import {Collection} from './brooke.model';


interface AppVM {
}

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  constructor(
  ) {}

    
  ngOnInit(): void {
  }
}
