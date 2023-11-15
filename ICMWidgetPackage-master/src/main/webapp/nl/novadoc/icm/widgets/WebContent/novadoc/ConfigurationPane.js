define([
	"dojo/_base/declare",
	"dijit/_TemplatedMixin",
	"dijit/_WidgetsInTemplateMixin",
	"ecm/widget/admin/PluginConfigurationPane",
	"dojo/text!./ConfigurationPane.html"
],
function(declare, _TemplatedMixin, _WidgetsInTemplateMixin, PluginConfigurationPane, template) {
	return declare("catalog.ConfigurationPane", [ PluginConfigurationPane, _TemplatedMixin, _WidgetsInTemplateMixin], {
		templateString: template,
		widgetsInTemplate: true,
		
		load: function(callback) {
			if(this.configurationString){
				var jsonConfig = JSON.parse(this.configurationString);
			}			
		},
		
		_onParamChange: function() {
			var configArray = new Array();

			var configJson = {"configuration" : configArray};
			this.configurationString = JSON.stringify(configJson);
			
			this.onSaveNeeded(true);
		},
		
		validate: function() {			
			return true;
		}
	});
});
