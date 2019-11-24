var XMLHttpRequest = require("xmlhttprequest");
var JsonSchemaToTypescript = require("json-schema-to-typescript");
var Fs = require('fs');

const bannerComment = `/* tslint:disable */\n/**\n* This file was automatically generated by the bindingz plugin.\n*/`;

class BindingzPlugin {
    apply(compiler) {
        compiler.hooks.beforeCompile.tap('Process resources', (stats) => {
            var xmlHttp = new XMLHttpRequest.XMLHttpRequest();
        xmlHttp.open("GET", 'http://localhost:8080/api/v1/schemas/asdf/asdf', false); // false for synchronous request
        xmlHttp.send(null);
        var responseText = xmlHttp.responseText;
        Fs.writeFileSync('response.json', responseText, 'utf8')

        var response = JSON.parse(responseText);
        JsonSchemaToTypescript.compile(response.content.schema, 'woopwoop', {bannerComment: bannerComment}).then(r => {
            Fs.writeFileSync('fun.ts', r, 'utf8')
        });
    });
    }
}

module.exports = BindingzPlugin;
