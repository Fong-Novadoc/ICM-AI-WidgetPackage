define([
    "dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/dom-class",

	"icm/base/Constants",
    "icm/base/BasePageWidget",
    "icm/util/Coordination",

    "novadoc/widget/titlebox/dijit/TitleBoxContentPane",
 ], function(declare, lang, domClass, Constants, BasePageWidget, Coordination, TitleBoxContentPane) {
    return declare("novadoc.widget.titlebox.TitleBox", [TitleBoxContentPane, BasePageWidget], {
    	postCreate: function() {
			this.inherited(arguments);
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