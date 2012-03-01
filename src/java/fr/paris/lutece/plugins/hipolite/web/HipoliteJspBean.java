/*
 * Copyright (c) 2002-2012, Mairie de Paris
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
package fr.paris.lutece.plugins.hipolite.web;

import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the validation interface of the site
 * (prod and preprod management)
 */
public class HipoliteJspBean extends PluginAdminPageJspBean
{
    // -----------
    //  Constants
    // -----------
    public static final String RIGHT_MANAGE_HIPOLITE = "HIPOLITE_MANAGEMENT";
    private static final String STR_NEWLINE = "\r\n";

    // Messages (AdminMessageService)
    private static final String MESSAGE_VALIDATE_SITE_WARNING = "hipolite.message.validate.site.warning";
    private static final String MESSAGE_VALIDATE_SITE_ERROR = "hipolite.message.do.validate.site.error";
    private static final String MESSAGE_CANCEL_VALIDATION_INFO = "hipolite.message.cancel.validation.info";
    private static final String MESSAGE_CANCEL_VALIDATION_ERROR = "hipolite.message.cancel.validation.error";

    // Templates
    private static final String TEMPLATE_VALIDATE_SITE = "admin/plugins/hipolite/validate_site.html";
    private static final String TEMPLATE_VALIDATION_DONE = "admin/plugins/hipolite/validation_done.html";

    // Properties : flags
    private static final String PROPERTY_FLAG_PATH = "hipolite.flag.path";
    private static final String PROPERTY_ACTIVE_FLAG_FILE = "hipolite.active.flag.file";
    private static final String PROPERTY_VALIDATION_FLAG_FILE = "hipolite.validation.flag.file";

    // Properties : titles
    private static final String PROPERTY_VALIDATE_SITE_TITLE = "hipolite.message.validate.site.title";
    private static final String PROPERTY_VALIDATION_DONE_TITLE = "hipolite.message.validation.done.title";
    
    // Markers
    private static final String MARK_ACTIVE_SYNCHRONIZATION = "active_synchronization";

    // JSP definition
    private static final String JSP_VALIDATE_SITE = "jsp/admin/plugins/hipolite/ValidateSite.jsp";

    /**
     * Displays the site validation page
     *
     * @param request The http request
     * @return The html code of the validation page
     */
    public String getValidateSite( HttpServletRequest request )
    {
        // Flag path
        String strFlagPath = AppPropertiesService.getProperty( PROPERTY_FLAG_PATH );

        // Flag file : validation
        String strValidationFlagFile = AppPropertiesService.getProperty( PROPERTY_VALIDATION_FLAG_FILE );
        File validationFile = new File( strFlagPath + "/" + strValidationFlagFile );

        // Flag file : active synchronization
        String strActiveFlagFile = AppPropertiesService.getProperty( PROPERTY_ACTIVE_FLAG_FILE );
        File activeFile = new File( strFlagPath + "/" + strActiveFlagFile );

        // Controls if the validation has already be done
        if ( validationFile.exists(  ) )
        {
            setPageTitleProperty( PROPERTY_VALIDATION_DONE_TITLE );

            HashMap<Object, Object> model = new HashMap<Object, Object>(  );

            if ( activeFile.exists(  ) )
            {
                model.put( MARK_ACTIVE_SYNCHRONIZATION, true );
            }
            else
            {
                model.put( MARK_ACTIVE_SYNCHRONIZATION, false );
            }

            HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_VALIDATION_DONE, getLocale(  ), model );

            return getAdminPage( templateList.getHtml(  ) );
        }
        else
        {
            setPageTitleProperty( PROPERTY_VALIDATE_SITE_TITLE );

            HashMap<Object, Object> model = new HashMap<Object, Object>(  );
            HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_VALIDATE_SITE, getLocale(  ), model );

            return getAdminPage( templateList.getHtml(  ) );
        }
    }

    /**
     * Processes the site validation. Creation of a flag file which indicates that
     * the site has been validated and is ready to be toggled in production.
     *
     * @param request The http request
     * @return the jsp url of the confirm message
     */
    public String doValidateSite( HttpServletRequest request )
    {
        // Flag file of the validation
        String strFlagPath = AppPropertiesService.getProperty( PROPERTY_FLAG_PATH );
        String strFlagFile = AppPropertiesService.getProperty( PROPERTY_VALIDATION_FLAG_FILE );
        File file = new File( strFlagPath + "/" + strFlagFile );

        try
        {
            // Creates the flag file (and parent directories)
            file.getParentFile(  ).mkdirs(  );
            file.createNewFile(  );

            BufferedWriter out = new BufferedWriter( new FileWriter( file.getPath(  ), true ) );
            out.write( "FICHIER TEMOIN DE VALIDATION DU SITE" + STR_NEWLINE );
            out.write( "AppUser : " + getUser(  ).getFirstName(  ) + " " + getUser(  ).getLastName(  ) + STR_NEWLINE );
            out.write( "Date de validation : " + new Date(  ) + STR_NEWLINE );
            out.write( "Adresse IP : " + request.getRemoteAddr(  ) + STR_NEWLINE );
            out.write( "Host : " + request.getRemoteHost(  ) + STR_NEWLINE );
            out.close(  );

            return AdminMessageService.getMessageUrl( request, MESSAGE_VALIDATE_SITE_WARNING, JSP_VALIDATE_SITE,
                AdminMessage.TYPE_CONFIRMATION );
        }
        catch ( IOException ioe )
        {
            AppLogService.error( ioe.getMessage(  ), ioe );

            return AdminMessageService.getMessageUrl( request, MESSAGE_VALIDATE_SITE_ERROR, AdminMessage.TYPE_ERROR );
        }
    }

    /**
     * Cancels the validation of the site
     *
     * @param request The http request
     * @return the jsp url of the confirm message
     */
    public String doCancelValidation( HttpServletRequest request )
    {
        // Flag path
        String strFlagPath = AppPropertiesService.getProperty( PROPERTY_FLAG_PATH );

        // Flag file : validation
        String strValidationFlagFile = AppPropertiesService.getProperty( PROPERTY_VALIDATION_FLAG_FILE );
        File validationFile = new File( strFlagPath + "/" + strValidationFlagFile );

        // Flag file : active synchronization
        String strActiveFlagFile = AppPropertiesService.getProperty( PROPERTY_ACTIVE_FLAG_FILE );
        File activeFile = new File( strFlagPath + "/" + strActiveFlagFile );

        // Controls if the synchronization is not active
        if ( !activeFile.exists(  ) )
        {
            // Deletes the flag file
            validationFile.delete(  );

            return AdminMessageService.getMessageUrl( request, MESSAGE_CANCEL_VALIDATION_INFO, JSP_VALIDATE_SITE,
                AdminMessage.TYPE_INFO );
        }
        else
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_CANCEL_VALIDATION_ERROR, JSP_VALIDATE_SITE,
                AdminMessage.TYPE_ERROR );
        }
    }
}
