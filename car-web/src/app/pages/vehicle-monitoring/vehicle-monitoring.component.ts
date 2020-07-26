import { Component, OnInit } from '@angular/core';
import {Car} from "../../services/car";
import {SelectItem} from "primeng";
import {CarService} from "../../services/car.service";

@Component({
  selector: 'app-vehicle-monitoring',
  templateUrl: './vehicle-monitoring.component.html',
  styleUrls: ['./vehicle-monitoring.component.scss']
})
export class VehicleMonitoringComponent implements OnInit {

  cars: Car[];

  selectedCar: Car;

  displayDialog: boolean;

  sortOptions: SelectItem[];

  sortKey: string;

  sortField: string;

  sortOrder: number;

  constructor(private carService: CarService) { }

  ngOnInit() {
    this.carService.getCarsLarge().then(cars => this.cars = cars);

    this.sortOptions = [
      {label: 'Newest First', value: '!year'},
      {label: 'Oldest First', value: 'year'},
      {label: 'Brand', value: 'brand'}
    ];
  }

  selectCar(event: Event, car: Car) {
    this.selectedCar = car;
    this.displayDialog = true;
    event.preventDefault();
  }

  onSortChange(event) {
    let value = event.value;

    if (value.indexOf('!') === 0) {
      this.sortOrder = -1;
      this.sortField = value.substring(1, value.length);
    }
    else {
      this.sortOrder = 1;
      this.sortField = value;
    }
  }

  onDialogHide() {
    this.selectedCar = null;
  }
}
