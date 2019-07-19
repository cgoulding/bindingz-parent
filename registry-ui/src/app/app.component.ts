import {Component, OnInit} from '@angular/core';
import {SchemaResource} from './models/SchemaResource';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  schemas: SchemaResource[] = [];

  constructor(
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    // this.schemas = JSON.parse(
    //   '[{"contractName":"SampleDto","providerName":"example1","version":"v1",' +
    //   '"schema":{"type":"object","id":"urn:jsonschema:com:monadiccloud:bindingz:contract:plugin:example:sbt:SampleDto","properties":{"one":{"type":"string"}}}},' +
    //   '{"contractName":"SampleDto","providerName":"example2","version":"v2",' +
    //   '"schema":{"type":"object","id":"urn:jsonschema:com:monadiccloud:bindingz:contract:plugin:example:sbt:SampleDto","properties":{"one":{"type":"string"}}}},' +
    //   '{"contractName":"SampleDto","providerName":"example1","version":"v2",' +
    //   '"schema":{"type":"object","id":"urn:jsonschema:com:monadiccloud:bindingz:contract:plugin:example:sbt:SampleDto","properties":{"one":{"type":"string"}}}}]');
    this.loadSchemas().subscribe(next => this.schemas = next);
  }

  loadSchemas(): Observable<SchemaResource[]> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
        'Access-Control-Allow-Origin': 'http://localhost:4200',
        'Access-Control-Allow-Methods': 'GET, POST, OPTIONS, PUT, PATCH, DELETE'
      })
    };

    return this.http.get<SchemaResource[]>('http://localhost:8080/api/v1/schemas', httpOptions);
  }
}
