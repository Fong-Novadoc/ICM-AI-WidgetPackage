define([
    "dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/dom-class",

	"icm/base/Constants",
    "icm/base/BasePageWidget",
    "icm/util/Coordination",

    "novadoc/widget/aisearch/dijit/AiSearchContentPane",
	"novadoc/widget/aisearch/WebContent/ApiTest",
 ], function(declare, lang, domClass, Constants, BasePageWidget, Coordination, TitleBoxContentPane,ApiTest) {
    return declare("novadoc.widget.aisearch.AiSearch", [TitleBoxContentPane, BasePageWidget], {
    	postCreate: async function() {
			console.log("log");

			let api = new ApiTest();
			api.run("y");

			setTimeout(
				lang.hitch(this,function() {
					console.log("Aisearch");
					this.onBroadcastEvent("icm.AiSearch", {})
				}),
			3000);
			setTimeout(
				lang.hitch(this,function() {
					console.log("SelectCase");
					this.onBroadcastEvent("icm.SelectCase", {})
				}),
			3000);
			setTimeout(()=>{
					document.getElementById('greeting').innerHTML = "Greeting";
				},
			3000);
			setTimeout(()=>{
					document.getElementById('pokemon-button').addEventListener("click", function(){
						console.log("pokemon button clicked");
						let doc = document.getElementById('pokemon-input')
						let query =  doc.value;
						console.log(query);
			
						let api = new ApiTest();
						api.run(query);
					});
				},
			3000);
		},

		searchForPokemon: function(){
			query = document.getElementById('pokemon-input').innerText;
			
			let api = new ApiTest();
			api.run(query);
		},

		handleICM_SendCaseInfoEvent: function(payload) {
			if(!payload.caseEditable || !payload.coordination)
				console.error("payload.caseEditable or payload.coordination is empty.");
			else 
				this._configureCoordination(payload.coordination, payload.caseEditable);			
		},

		/**
		*	This ensures that the any asynchronous actions have been finished before completing the Coordtopic.LOADWIDGET topic
		*/ 
		_configureCoordination: function(coordination, caseEditable) {
            coordination.participate(Constants.CoordTopic.LOADWIDGET, lang.hitch(this, function(context, complete, abort) {
            	// Execute ajax calls and wait for result before finishing this topic.
            	this.load(caseEditable, complete);
            }));
		}
	});
});