define([
        "dojo/_base/declare",
        "dojo/dom-style",
        "dojo/_base/lang",
        "dojo/aspect",
		"icm/dialog/addcommentdialog/dijit/CommentContentPane"
], function(declare, style, lang, aspect, CommentContentPane) {
    return declare("catalog.icmextension.pgwidget.casecomments.CaseComments", [CommentContentPane], {
    	postCreate: function() {
    		if(this.widgetProperties.LoadingText)
    			this.artifactTypeSpan.innerHTML = this.widgetProperties.LoadingText;

    		// Do not show the Content List view button
			style.set(this.historyPanel.topContainer.domNode, "display", "none");
			
			// Enables the textarea to not override the max width because of the padding
			style.set(this.commentText, "-webkit-box-sizing", "border-box");
			style.set(this.commentText, "-moz-box-sizing", "border-box");
			style.set(this.commentText, "box-sizing", "border-box");

			// Initialize comment history panel
			this.historyPanel.setContentListModules(this._getContentListModules());
			// Note: this is required by Nexus contentlist widget. If not, will get a JS err.
			if (this.historyPanel.startup){
				this.historyPanel.startup();
			}
    	},
    	
		handleICM_SendCaseInfoEvent: function(payload) {
			// Remove some components, such as caseTitle and 'comment'
			this.LabelDiv.innerHTML = "";
			this.newCommentDiv.getElementsByClassName("newCommentLabel")[0].innerHTML = "";
			
			// Default config needed for the CommentContentPane
			this.caseModel = payload.caseEditable.getCase();
			this.artifactType = "Case"
			this.artifactLabel = this.caseModel.caseTitle;
			this.commentContext = 102;
			this.commentList.parentFolder = this.caseModel; //Required by ICN ContentList to do sort
			
			// Let the CommentContentPane load the comments
			this.loadComments();
		}
	});
});
