export class SchemaResource {
  namespace: string;
  contractName: string;
  owner: string;
  version: string;
  schema: any;

  constructor(namespace: string, contractName: string, owner: string, version: string, schema: any) {
    this.namespace = namespace;
    this.contractName = contractName;
    this.owner = owner;
    this.version = version;
    this.schema = schema;
  }
}
