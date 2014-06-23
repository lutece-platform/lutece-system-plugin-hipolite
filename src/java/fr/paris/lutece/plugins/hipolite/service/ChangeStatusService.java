package fr.paris.lutece.plugins.hipolite.service;

import fr.paris.lutece.plugins.hipolite.enumeration.ReplicationStatutEnum;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.dashboard.DashboardComponent;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;


/**
 * Methode pour afficher l'état de la validation du site
 */
public class ChangeStatusService extends DashboardComponent
{
    // -----------
    //  Constants
    // -----------
    public static final String RIGHT_MANAGE_HIPOLITE = "HIPOLITE_MANAGEMENT";
    private static final String DEFAULT_MODE_DISPLAY_STATUS = "1";

    // Templates
    private static final String TEMPLATE_DISPLAY_STATUS = "/admin/plugins/hipolite/change_status.html";

    // Properties : flags
    private static final String PROPERTY_SYNC_STATUS = "hipolite.sync_statut";

    // Properties : display status
    private static final String PROPERTY_MODE_DISPLAY_STATUS = "hipolite.display.status";

    // Markers
    private static final String MARK_MODE_DISPLAY_STATUS = "mode_display_status";
    private static final String MARK_STATUS = "status";

    /**
     * Display the site validation status
     * @param user The current user
     * @param request HttpServletRequest
     * @return The dashboard data
     */
    public String getDashboardData( AdminUser user, HttpServletRequest request )
    {
        // Flag path
        String strModeDisplayStatus = AppPropertiesService.getProperty( PROPERTY_MODE_DISPLAY_STATUS,
                DEFAULT_MODE_DISPLAY_STATUS );

        // Flag file : validation
        String strSyncStatut = DatastoreService.getDataValue( PROPERTY_SYNC_STATUS,
                ReplicationStatutEnum.NO_REPLICATION.getStatut( ) );

        HashMap<Object, Object> model = new HashMap<Object, Object>( );

        model.put( MARK_MODE_DISPLAY_STATUS, strModeDisplayStatus );

        // Controls if the validation has already be done
        model.put( MARK_STATUS, strSyncStatut );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_DISPLAY_STATUS, request.getLocale( ), model );

        return template.getHtml( );
    }

}
