package nl.novadoc.icm.widgets.services;
import nl.novadoc.icm.widgets.services.PokemonGetter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.filenet.api.constants.AutoClassify;
import com.filenet.api.constants.AutoUniqueName;
import com.filenet.api.constants.CheckinType;
import com.filenet.api.constants.DefineSecurityParentage;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Connection;
import com.filenet.api.core.Document;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.ReferentialContainmentRelationship;
import com.filenet.api.util.UserContext;
import com.ibm.ecm.extension.PluginResponseUtil;
import com.ibm.ecm.extension.PluginService;
import com.ibm.ecm.extension.PluginServiceCallbacks;
import com.ibm.ecm.json.JSONResponse;
import com.ibm.json.java.JSONObject;


public class InsertPokemonService extends PluginService {
	private String serverURI = "https://win-8p654dqeoe6.demo.local:9443/wsi/FNCEWS40MTOM/";
    private String username = "p8admin";
    private String password = "Password1";
    private String objectStoreName = "TOS";
    
	@Override
	public String getId() {
		return "InsertPokemonService";
	}

	@Override
	public void execute(PluginServiceCallbacks callbacks, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// Build response object (JSON)
		JSONResponse result = new JSONResponse();

		PokemonGetter pokemonGetter = new PokemonGetter();	
        String name = request.getParameter("name");
        JSONObject pokemonData = pokemonGetter.getPokemonData(name);

	
        String location = request.getParameter("location");
        String primaryType = request.getParameter("primaryType");
		
		Connection conn = Factory.Connection.getConnection(serverURI);
    	UserContext uc = UserContext.get();
        
        try {
            // Connect to the Content Engine
            uc.pushSubject(uc.createSubject(conn, username, password, "FileNetP8WSI"));

            // Fetch the domain and object store
            Domain domain = Factory.Domain.fetchInstance(conn, null, null);
            ObjectStore objectStore = Factory.ObjectStore.fetchInstance(domain, objectStoreName, null);

            // Create a document with the "Pokemon" document class
            Document document = Factory.Document.createInstance(objectStore, "Pokemon");
            document.getProperties().putValue("DocumentTitle", name);
            document.getProperties().putValue("PrimaryType", primaryType);
            document.checkin(AutoClassify.AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
            // Save the document
            document.save(RefreshMode.REFRESH);
            
            // Add document to folder
            Folder folder = Factory.Folder.fetchInstance(objectStore, location, null);
            ReferentialContainmentRelationship rcr = folder.file(document, AutoUniqueName.AUTO_UNIQUE, null, null);
            rcr.save(RefreshMode.NO_REFRESH);
            
            // Add name to response
            result.put("ID", document.get_Id().toString());
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            uc.popSubject();
        }
		
		// Return
		PluginResponseUtil.writeJSONResponse(request, response, result, callbacks, getId());
	}
}