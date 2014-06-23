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
