package nl.novadoc.icm.widgets;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.ibm.ecm.extension.Plugin;
import com.ibm.ecm.extension.PluginAction;
import com.ibm.ecm.extension.PluginResponseFilter;
import com.ibm.ecm.extension.PluginService;
import com.ibm.ecm.extension.PluginServiceCallbacks;

import nl.novadoc.icm.widgets.logger.Logger;

public class ICMWidgetPackage extends Plugin {
	private static final String VERSION = "1.0.0";
    private static final boolean COMPRESSED = false;
    private static Logger logger = null;
	
	public void applicationInit(HttpServletRequest request, PluginServiceCallbacks callbacks) {
		try {
			if(logger == null)
				logger = Logger.init(ICMWidgetPackage.class, "logger.properties");
			
			logger.debug("Initialized " + getId());
		} catch(Exception e) {
			System.err.println(e);
		}
	}

	public String getId() {
		return "ICMWidgetPackage";
	}

	public String getName(Locale locale) {
		return "Novadoc ICM Widget Package";
	}

	public String getVersion() {
		return VERSION;
	}

   public String getScript() {
		return "ICMWidgetPackage.js";
    }

    public String getDebugScript() {
        return "ICMWidgetPackage.js";
    }

    public String getCSSFileName() {
        return "ICMWidgetPackage.css";
    }
    
    public String getDebugCSSFileName() {
		return "ICMWidgetPackage.css";
    }
    
	public String getDojoModule() {
		return "novadoc";
	}
	
	public String getConfigurationDijitClass() {
		return "novadoc.ConfigurationPane";
	}

	public PluginAction[] getActions() {
		return new PluginAction[] {};
	}
	
	public PluginService[] getServices() {
		return new PluginService[] {
			new nl.novadoc.icm.widgets.services.DocumentService(),
			new nl.novadoc.icm.widgets.services.InsertPokemonService()
		};
	}
	
	public PluginResponseFilter[] getResponseFilters() {
		return new PluginResponseFilter[] {};
	}
}