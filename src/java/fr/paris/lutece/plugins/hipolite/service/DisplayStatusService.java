package fr.paris.lutece.plugins.hipolite.service;

import java.io.File;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.html.HtmlTemplate;

public class DisplayStatusService 
{
	// -----------
    //  Constants
    // -----------
    public static final String RIGHT_MANAGE_HIPOLITE = "HIPOLITE_MANAGEMENT";
    private static final String DEFAULT_MODE_DISPLAY_STATUS = "1";

    // Templates
    private static final String TEMPLATE_DISPLAY_STATUS = "/admin/plugins/hipolite/display_status.html";

    // Properties : flags
    private static final String PROPERTY_FLAG_PATH = "hipolite.flag.path";
    private static final String PROPERTY_VALIDATION_FLAG_FILE = "hipolite.validation.flag.file";
    
    // Properties : display status
    private static final String PROPERTY_MODE_DISPLAY_STATUS = "hipolite.display.status";

    // Markers
    private static final String MARK_MODE_DISPLAY_STATUS = "mode_display_status";
    private static final String MARK_STATUS = "status";

    /**
     * Display the site validation status
     * @param request The http request
     * @return The html code of the status validation page
     */
    public static String displayStatusValidation(  HttpServletRequest request )
    {
    	// Flag path
        String strFlagPath = AppPropertiesService.getProperty( PROPERTY_FLAG_PATH );
        
        String strModeDisplayStatus = AppPropertiesService.getProperty( PROPERTY_MODE_DISPLAY_STATUS, DEFAULT_MODE_DISPLAY_STATUS );

        // Flag file : validation
        String strValidationFlagFile = AppPropertiesService.getProperty( PROPERTY_VALIDATION_FLAG_FILE );
        File validationFile = new File( strFlagPath + "/" + strValidationFlagFile );

        HashMap<Object, Object> model = new HashMap<Object, Object>(  );
        
        model.put( MARK_MODE_DISPLAY_STATUS , strModeDisplayStatus );
        
        // Controls if the validation has already be done
        if ( validationFile.exists(  ) )
        {
        	model.put( MARK_STATUS, true );
        	
        }
        else
        {
        	model.put( MARK_STATUS, false );
        }
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_DISPLAY_STATUS, request.getLocale(), model );

        return template.getHtml(  );
    }

}
