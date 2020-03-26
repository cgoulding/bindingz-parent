import {Component, OnInit} from '@angular/core';
import {SchemaResource} from '../models/SchemaResource';
import {BackendService} from '../backend.service'
import {Observable} from 'rxjs';

@Component({
  selector: 'app-schemas',
  templateUrl: './schemas.component.html',
  styleUrls: ['./schemas.component.css']
})
export class SchemasComponent implements OnInit {

  schemas: SchemaResource[] = [];

    constructor(
      private backendService: BackendService
    ) {}

    ngOnInit(): void {
      this.loadSchemas().subscribe(next => this.schemas = next);
    }

    loadSchemas(): Observable<SchemaResource[]> {
      return this.backendService.get<SchemaResource[]>('/backend/v1/schemas');
    }
}
