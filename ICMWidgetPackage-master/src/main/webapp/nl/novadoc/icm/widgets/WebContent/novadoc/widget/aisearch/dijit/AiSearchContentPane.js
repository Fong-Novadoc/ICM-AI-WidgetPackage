define([
    "dojo/_base/declare",
    "dojo/_base/lang",
    
    "icm/base/_BaseWidget",

    "dojo/text!novadoc/widget/aisearch/dijit/templates/AiSearchContentPane.html",
], function(declare, lang, BaseWidget, template) {
    return declare("novadoc.widget.aisearch.dijit.AiSearchContentPane", [BaseWidget] /* mixin modules, can be null */ , {
        templateString: template,

        /**
        *   Arguments are mixed into this module
        *   all passed properties will be overwritten for that instance
        */
        constructor: function (args) {
            lang.mixin(this, args);
        },
        
        // language: function(){
        //     const fs = require('fs');
        //     const jsonFilePath = 'src/main/webapp/nl/Fong.Suen/icm.widgets/WebContent/novadoc/languages/English.JSON';
            
        //     // Read the JSON file
        //     fs.readFile(jsonFilePath, 'utf8', (err, data) => {
        //         if (err) {
        //             console.error('Error reading JSON file:', err);
        //             return;
        //         }
            
        //         // Parse the JSON data
        //         try {
        //             const jsonData = JSON.parse(data);
        //             console.log(jsonData);
            
        //             // Now you can work with the JSON data as a JavaScript object
        //             // For example, you can access values like jsonData.someKey
        //         } catch (parseError) {
        //             console.error('Error parsing JSON:', parseError);
        //         }
        //     });
		// },

        load: function(caseEditable, complete) {
            setTimeout(complete, 500);
        },

        /**
        *   Invoked just before the DOM is created
        */
        postMixInProperties: function () {
            this.inherited(arguments);
        },

        /**
        *   For creating nodes and connecting events if dijit._Template is not used
        */
        buildRendering: function () {
            this.inherited(arguments);
        },

        /**
        *   Called after the module is created
        */
        postCreate: function () {
            // Calling the postCreate method of the mixin modules > _BaseWidget.postCreate
            this.inherited(arguments);

            // Access the widget html
            if(this.widgetProperties.showTitle)
                this.title.innerHTML = "Novadoc";
        },

        /**
        *   Calls all startup method of child widgets which ensures they are created
        */
        startup: function () {
            this.inherited(arguments);
        },

        /**
        *   Only needed if there are resources which do not clean up otherwise
        */
        destroy: function () {
            this.inherited(arguments);
        }
    });
})