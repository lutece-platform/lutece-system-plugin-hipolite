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
package fr.paris.lutece.plugins.hipolite.replication;

import fr.paris.lutece.plugins.hipolite.enumeration.ReplicationStatutEnum;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.net.HttpURLConnection;
import java.net.URL;


public class ReplicationDaemon implements Runnable
{
    private static final String PROPERTY_URL_PROD = "hipolite.url.prod";
    private static final String PROPERTY_SYNC_STATUS = "hipolite.sync_statut";

    @Override
    public void run( )
    {
        DatastoreService.setDataValue( PROPERTY_SYNC_STATUS, ReplicationStatutEnum.IN_PROGRESS.getStatut( ) );
        int status = ReplicationBatch.launchReplication( false );

        if ( status == 1 )
        {
            // Error
            DatastoreService.setDataValue( PROPERTY_SYNC_STATUS, ReplicationStatutEnum.ON_ERROR.getStatut( ) );
        }
        else
        {
            // Let clean remote caches and indexation
            try
            {
                URL url = new URL( AppPropertiesService.getProperty( PROPERTY_URL_PROD ) + "jsp/site/DoResetCaches.jsp" );
                HttpURLConnection connection = (HttpURLConnection) url.openConnection( );
                connection.setRequestMethod( "GET" );
                connection.connect( );
                int code = connection.getResponseCode( );

                if ( code != 200 )
                {
                    AppLogService.error( "Unable to contact page for remote cache cleaning." );
                }
            }
            catch ( Exception e )
            {
                AppLogService.error( "Error during remote cache cleaning." );
            }

            try
            {
                URL url = new URL( AppPropertiesService.getProperty( PROPERTY_URL_PROD ) + "jsp/site/DoIndexing.jsp" );
                HttpURLConnection connection = (HttpURLConnection) url.openConnection( );
                connection.setRequestMethod( "GET" );
                connection.connect( );
                int code = connection.getResponseCode( );

                if ( code != 200 )
                {
                    AppLogService.error( "Unable to contact page for remote indexation." );
                }
            }
            catch ( Exception e )
            {
                AppLogService.error( "Error during remote indexation." );
            }

            // Success
            DatastoreService.setDataValue( PROPERTY_SYNC_STATUS, ReplicationStatutEnum.ON_SUCCESS.getStatut( ) );
        }
    }

}
