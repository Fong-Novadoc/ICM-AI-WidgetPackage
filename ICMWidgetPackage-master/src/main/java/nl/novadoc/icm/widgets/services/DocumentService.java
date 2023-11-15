package nl.novadoc.icm.widgets.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.filenet.api.core.Connection;
import com.filenet.api.core.Document;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.util.UserContext;
import com.ibm.ecm.extension.PluginResponseUtil;
import com.ibm.ecm.extension.PluginService;
import com.ibm.ecm.extension.PluginServiceCallbacks;
import com.ibm.ecm.json.JSONResponse;


public class DocumentService extends PluginService {
	private String serverURI = "https://win-8p654dqeoe6.demo.local:9443/wsi/FNCEWS40MTOM/";
    private String username = "p8admin";
    private String password = "Password1";
    private String objectStoreName = "TOS";
    
	@Override
	public String getId() {
		return "DocumentService";
	}

	@Override
	public void execute(PluginServiceCallbacks callbacks, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// Build response object (JSON)
		JSONResponse result = new JSONResponse();
		
		String docID = request.getParameter("docID");
        result.put("docID", docID);
		
		Connection conn = Factory.Connection.getConnection(serverURI);
    	UserContext uc = UserContext.get();
        
        try {
            // Connect to the Content Engine
            uc.pushSubject(uc.createSubject(conn, username, password, "FileNetP8WSI"));

            // Fetch the domain and object store
            Domain domain = Factory.Domain.fetchInstance(conn, null, null);
            ObjectStore objectStore = Factory.ObjectStore.fetchInstance(domain, objectStoreName, null);

            // Retrieve the document by its ID
            Document document = Factory.Document.fetchInstance(objectStore, docID, null);

            // Add name to response
            result.put("docName", document.get_Name());
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            uc.popSubject();
        }
		
		// Return
		PluginResponseUtil.writeJSONResponse(request, response, result, callbacks, getId());
	}
}