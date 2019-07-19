export class SchemaResource {
  contractName: string;
  providerName: string;
  version: string;
  schema: any;

  constructor(contractName: string, providerName: string, version: string, schema: any) {
    this.contractName = contractName;
    this.providerName = providerName;
    this.version = version;
    this.schema = schema;
  }
}
