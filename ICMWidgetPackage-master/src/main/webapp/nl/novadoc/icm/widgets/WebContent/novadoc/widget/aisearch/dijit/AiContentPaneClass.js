define(["dojo/_base/declare"], function(declare){
    return declare("novadoc.widget.aisearch.dijit.AiContentPaneClass", {
      constructor: function(name, age, residence){
        console.log(`${name}, ${age}, ${residence}`);
        this.name = name;
        this.age = age;
        this.residence = residence;
        // console.log(`Name is: ${this.name} and he is ${this.age} old. He lives in ${this.residence}`);
      }
    });
  });