/*
 * Copyright (c) 2002-2011, Mairie de Paris
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

import fr.paris.lutece.plugins.hipolite.database.DataBaseConnection;
import fr.paris.lutece.plugins.hipolite.database.ReplicationDAO;
import fr.paris.lutece.plugins.hipolite.exceptions.HipoliteException;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.ResourceBundle;


/**
 * This class contains all the replication management methods
 */
public class Replication
{
    // -----------
    //  Constants
    // -----------
    // Master database information
    public static final String PROPERTY_JDBC_MASTER_DRIVER = "hipolite.jdbc.master.driver";
    public static final String PROPERTY_JDBC_MASTER_URL = "hipolite.jdbc.master.url";
    public static final String PROPERTY_JDBC_MASTER_USER = "hipolite.jdbc.master.user";
    public static final String PROPERTY_JDBC_MASTER_PWD = "hipolite.jdbc.master.pwd";

    // Slave database information
    public static final String PROPERTY_JDBC_SLAVE_DRIVER = "hipolite.jdbc.slave.driver";
    public static final String PROPERTY_JDBC_SLAVE_URL = "hipolite.jdbc.slave.url";
    public static final String PROPERTY_JDBC_SLAVE_USER = "hipolite.jdbc.slave.user";
    public static final String PROPERTY_JDBC_SLAVE_PWD = "hipolite.jdbc.slave.pwd";

    // Replication properties
    public static final String PROPERTY_REPLICATION_MAXTIMEOUT = "hipolite.replication.maxtimeout";
    public static final String PROPERTY_REPLICATION_MAXDELAY = "hipolite.replication.maxdelay";

    // Declares and creates the Logger 
    private static final Logger _logger = Logger.getLogger( Replication.class );

    // -----------
    //  Variables
    // -----------
    // Master database information
    private String _strMasterDriver;
    private String _strMasterUrl;
    private String _strMasterUser;
    private String _strMasterPwd;

    // Slave database information
    private String _strSlaveDriver;
    private String _strSlaveUrl;
    private String _strSlaveUser;
    private String _strSlavePwd;

    // Replication properties
    private String _strMaxTimeout;
    private String _strMaxDelay;

    // Database connections
    private DataBaseConnection _dbConnection;
    private Connection _masterConnection;
    private Connection _slaveConnection;
    private ReplicationDAO _replicationDao;

    // Others
    private ResourceBundle _resourceBundle;
    private boolean _bStarted;

    /**
    * Constructs the replication object
    */
    public Replication(  )
    {
        // Resource bundle
        _resourceBundle = ResourceBundle.getBundle( "hipolite" );

        // Gets the master database information
        _strMasterDriver = getProperty( PROPERTY_JDBC_MASTER_DRIVER );
        _strMasterUrl = getProperty( PROPERTY_JDBC_MASTER_URL );
        _strMasterUser = getProperty( PROPERTY_JDBC_MASTER_USER );
        _strMasterPwd = getProperty( PROPERTY_JDBC_MASTER_PWD );

        // Gets the slave database information
        _strSlaveDriver = getProperty( PROPERTY_JDBC_SLAVE_DRIVER );
        _strSlaveUrl = getProperty( PROPERTY_JDBC_SLAVE_URL );
        _strSlaveUser = getProperty( PROPERTY_JDBC_SLAVE_USER );
        _strSlavePwd = getProperty( PROPERTY_JDBC_SLAVE_PWD );

        // Gets the replication properties
        _strMaxTimeout = getProperty( PROPERTY_REPLICATION_MAXTIMEOUT );
        _strMaxDelay = getProperty( PROPERTY_REPLICATION_MAXDELAY );
    }

    /**
    * Inits the replication
    *
    * @throws HipoliteException If an Hipolite exception occurred
    */
    public void init(  ) throws HipoliteException
    {
        // Creates the master database connection
        _dbConnection = new DataBaseConnection( _strMasterDriver, _strMasterUrl, _strMasterUser, _strMasterPwd );
        _masterConnection = _dbConnection.getConnection(  );

        // Creates the slave database connection
        _dbConnection = new DataBaseConnection( _strSlaveDriver, _strSlaveUrl, _strSlaveUser, _strSlavePwd );
        _slaveConnection = _dbConnection.getConnection(  );

        // Creates the replication DAO (used to execute requests)
        _replicationDao = new ReplicationDAO(  );
    }

    /**
    * Starts the replication process
    *
    * @throws HipoliteException If an Hipolite exception occurred
    */
    public void start(  ) throws HipoliteException
    {
        // If the connections are active and the replication is not already started
        if ( !_bStarted && ( _masterConnection != null ) && ( _slaveConnection != null ) )
        {
            // Updates the boolean state
            _bStarted = true;

            // Locks the master tables
            lockMasterTables(  );

            // Replicates the master to the slave
            replicate(  );

            // Unlocks the master tables
            unlockMasterTables(  );

            // Resets the master
            resetMaster(  );

            // Resets the slave
            resetSlave(  );

            // Updates the boolean state
            _bStarted = false;
        }
    }

    /**
    * Closes the replication
    *
    * @throws HipoliteException If an Hipolite exception occurred
    */
    public void close(  ) throws HipoliteException
    {
        // Closes the master database connection
        _dbConnection.closeConnection( _masterConnection );

        // Closes the slave database connection
        _dbConnection.closeConnection( _slaveConnection );
    }

    /**
    * Replicates the master to the slave
    *
    * @throws HipoliteException If an Hipolite exception occurred
    */
    private void replicate(  ) throws HipoliteException
    {
        // Tries to start the replication with START SLAVE request on slave database
        try
        {
            _replicationDao.startSlave( _slaveConnection );
        }
        catch ( SQLException sqle1 )
        {
            // Maybe START SLAVE didn't work because slave processing is
            // already running : tries to stop it first (second attempt)
            try
            {
                _replicationDao.stopSlave( _slaveConnection );
                _replicationDao.startSlave( _slaveConnection );
            }
            catch ( SQLException sqle2 )
            {
                // Second attempt failed : tries to stop the replication
                // with STOP SLAVE request on slave database
                try
                {
                    _replicationDao.stopSlave( _slaveConnection );
                }
                catch ( SQLException sqle3 )
                {
                    _logger.error( sqle3.getMessage(  ) );
                    throw new HipoliteException( sqle3.getMessage(  ) );
                }
            }
        }

        // Checks the master log position
        int nMasterPos = getMasterLogPosition(  );

        if ( _logger.isDebugEnabled(  ) )
        {
            _logger.debug( "Master position : " + nMasterPos );
        }

        // Checks the slave log position
        int nSlavePos = getSlaveLogPosition(  );

        if ( _logger.isDebugEnabled(  ) )
        {
            _logger.debug( "Slave position : " + nSlavePos );
        }

        // Initialization
        int nSlavePreviousPos = -1;
        int nIndex = 0;

        int nCounter = 0;

        if ( ( _strMaxTimeout != null ) && ( !_strMaxTimeout.equals( "" ) ) && ( !_strMaxTimeout.equals( "0" ) ) &&
                ( _strMaxDelay != null ) && ( !_strMaxDelay.equals( "" ) ) && ( !_strMaxDelay.equals( "0" ) ) )
        {
            nCounter = Integer.parseInt( _strMaxTimeout ) / Integer.parseInt( _strMaxDelay );
        }

        // While the replication is not finished
        while ( ( nIndex < nCounter ) && ( nSlavePos != nMasterPos ) )
        {
            // Wait and load          
            addDelay( _strMaxDelay );

            // Updates the slave log position
            nSlavePos = getSlaveLogPosition(  );

            if ( _logger.isDebugEnabled(  ) )
            {
                _logger.debug( "Slave position : " + nSlavePos );
            }

            if ( nSlavePos == nSlavePreviousPos )
            {
                nIndex++;
            }
            else
            {
                nIndex = 0;
                nSlavePreviousPos = nSlavePos;
            }
        }

        // Tries to stop the replication with STOP SLAVE request on slave database
        try
        {
            _replicationDao.stopSlave( _slaveConnection );
        }
        catch ( SQLException sqle )
        {
            _logger.error( sqle.getMessage(  ) );
            throw new HipoliteException( sqle.getMessage(  ) );
        }
    }

    /**
     * Returns the value of the key parameter contained in the configuration file
     *
     * @param key String
     * @return String value
     */
    private String getProperty( String key )
    {
        String value = null;

        // If the resource bundle is not null
        if ( _resourceBundle != null )
        {
            value = (String) _resourceBundle.getObject( key );
        }

        return value;
    }

    /**
    * Locks the master tables
    *
    * @throws HipoliteException If an Hipolite exception occurred
    */
    private void lockMasterTables(  ) throws HipoliteException
    {
        try
        {
            _replicationDao.lockMasterTables( _masterConnection );
        }
        catch ( SQLException sqle )
        {
            _logger.error( sqle.getMessage(  ) );
            throw new HipoliteException( sqle.getMessage(  ) );
        }
    }

    /**
    * Unlocks the master tables
    *
    * @throws HipoliteException If an Hipolite exception occurred
    */
    private void unlockMasterTables(  ) throws HipoliteException
    {
        try
        {
            _replicationDao.unlockMasterTables( _masterConnection );
        }
        catch ( SQLException sqle )
        {
            _logger.error( sqle.getMessage(  ) );
            throw new HipoliteException( sqle.getMessage(  ) );
        }
    }

    /**
    * Resets the master
    *
    * @throws HipoliteException If an Hipolite exception occurred
    */
    private void resetMaster(  ) throws HipoliteException
    {
        try
        {
            _replicationDao.resetMaster( _masterConnection );
        }
        catch ( SQLException sqle )
        {
            _logger.error( sqle.getMessage(  ) );
            throw new HipoliteException( sqle.getMessage(  ) );
        }
    }

    /**
    * Resets the slave
    *
    * @throws HipoliteException If an Hipolite exception occurred
    */
    private void resetSlave(  ) throws HipoliteException
    {
        try
        {
            _replicationDao.resetSlave( _slaveConnection );
        }
        catch ( SQLException sqle )
        {
            _logger.error( sqle.getMessage(  ) );
            throw new HipoliteException( sqle.getMessage(  ) );
        }
    }

    /**
    * Gets the master log position
    *
    * @throws HipoliteException If an Hipolite exception occurred
    * @return The master log position
    */
    private int getMasterLogPosition(  ) throws HipoliteException
    {
        int position = 0;

        try
        {
            position = _replicationDao.masterShowStatus( _masterConnection );
        }
        catch ( SQLException sqle )
        {
            _logger.error( sqle.getMessage(  ) );
            throw new HipoliteException( sqle.getMessage(  ) );
        }

        return position;
    }

    /**
    * Gets the slave log position
    *
    * @throws HipoliteException If an Hipolite exception occurred
    * @return The slave log position
    */
    private int getSlaveLogPosition(  ) throws HipoliteException
    {
        int position = 0;

        try
        {
            position = _replicationDao.slaveShowStatus( _slaveConnection );
        }
        catch ( SQLException sqle )
        {
            _logger.error( sqle.getMessage(  ) );
            throw new HipoliteException( sqle.getMessage(  ) );
        }

        return position;
    }

    /**
    * Adds a delay for the replication
    *
    * @param delay String
    * @throws HipoliteException If an Hipolite exception occurred
    */
    private void addDelay( String delay ) throws HipoliteException
    {
        try
        {
            Thread.sleep( new Long( delay ).longValue(  ) );
        }
        catch ( NumberFormatException nfe )
        {
            _logger.error( nfe.getMessage(  ) );
            throw new HipoliteException( nfe.getMessage(  ) );
        }
        catch ( InterruptedException ie )
        {
            _logger.error( ie.getMessage(  ) );
            throw new HipoliteException( ie.getMessage(  ) );
        }
    }
}
