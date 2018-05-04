/*
 * Copyright (c) 2002-2017, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
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
public class DisplayStatusService extends DashboardComponent
{
    // -----------
    //  Constants
    // -----------
    public static final String RIGHT_MANAGE_HIPOLITE = "HIPOLITE_MANAGEMENT";
    private static final String DEFAULT_MODE_DISPLAY_STATUS = "1";

    // Templates
    private static final String TEMPLATE_DISPLAY_STATUS = "/admin/plugins/hipolite/display_status.html";

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
