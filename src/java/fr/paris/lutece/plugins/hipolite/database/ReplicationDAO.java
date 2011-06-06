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
package fr.paris.lutece.plugins.hipolite.database;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * This class is used to execute the database requests
 */
public class ReplicationDAO
{
    // Master requests
    private static final String MASTER_LOCK_TABLES_REQUEST = "FLUSH TABLES WITH READ LOCK";
    private static final String MASTER_UNLOCK_TABLES_REQUEST = "UNLOCK TABLES";
    private static final String MASTER_SHOW_STATUS_REQUEST = "SHOW MASTER STATUS";
    private static final String MASTER_RESET_REQUEST = "RESET MASTER";

    // Slave requests
    private static final String SLAVE_START_REPLICATION_REQUEST = "START SLAVE";
    private static final String SLAVE_STOP_REPLICATION_REQUEST = "STOP SLAVE";
    private static final String SLAVE_SHOW_STATUS_REQUEST = "SHOW SLAVE STATUS";
    private static final String SLAVE_RESET_REQUEST = "RESET SLAVE";

    // Columns titles
    private static final String MASTER_LOG_POSITION = "Position";
    private static final String SLAVE_LOG_POSITION = "Exec_master_log_pos";

    // String
    private static final String STR_REQUEST = "Request : ";

    // Declares and creates the Logger 
    private static final Logger _logger = Logger.getLogger( ReplicationDAO.class );

    /**
     * Resets the master
     *
     * @param connection Connection
     * @throws SQLException If a SQL exception occurred
     */
    public void resetMaster( Connection connection ) throws SQLException
    {
        Statement st = connection.createStatement(  );

        if ( _logger.isDebugEnabled(  ) )
        {
            _logger.debug( STR_REQUEST + MASTER_RESET_REQUEST );
        }

        st.execute( MASTER_RESET_REQUEST );
    }

    /**
     * Resets the slave
     *
     * @param connection Connection
     * @throws SQLException If a SQL exception occurred
     */
    public void resetSlave( Connection connection ) throws SQLException
    {
        Statement st = connection.createStatement(  );

        if ( _logger.isDebugEnabled(  ) )
        {
            _logger.debug( STR_REQUEST + SLAVE_RESET_REQUEST );
        }

        st.execute( SLAVE_RESET_REQUEST );
    }

    /**
     * Locks the master tables
     *
     * @param connection Connection
     * @throws SQLException If a SQL exception occurred
     */
    public void lockMasterTables( Connection connection )
        throws SQLException
    {
        Statement st = connection.createStatement(  );

        if ( _logger.isDebugEnabled(  ) )
        {
            _logger.debug( STR_REQUEST + MASTER_LOCK_TABLES_REQUEST );
        }

        st.execute( MASTER_LOCK_TABLES_REQUEST );
    }

    /**
     * Unlocks the master tables
     *
     * @param connection Connection
     * @throws SQLException If a SQL exception occurred
     */
    public void unlockMasterTables( Connection connection )
        throws SQLException
    {
        Statement st = connection.createStatement(  );

        if ( _logger.isDebugEnabled(  ) )
        {
            _logger.debug( STR_REQUEST + MASTER_UNLOCK_TABLES_REQUEST );
        }

        st.execute( MASTER_UNLOCK_TABLES_REQUEST );
    }

    /**
     * Starts the slave
     *
     * @param connection Connection
     * @throws SQLException If a SQL exception occurred
     */
    public void startSlave( Connection connection ) throws SQLException
    {
        Statement st = connection.createStatement(  );

        if ( _logger.isDebugEnabled(  ) )
        {
            _logger.debug( STR_REQUEST + SLAVE_START_REPLICATION_REQUEST );
        }

        st.execute( SLAVE_START_REPLICATION_REQUEST );
    }

    /**
     * Stops the slave
     *
     * @param connection Connection
     * @throws SQLException If a SQL exception occurred
     */
    public void stopSlave( Connection connection ) throws SQLException
    {
        Statement st = connection.createStatement(  );

        if ( _logger.isDebugEnabled(  ) )
        {
            _logger.debug( STR_REQUEST + SLAVE_STOP_REPLICATION_REQUEST );
        }

        st.execute( SLAVE_STOP_REPLICATION_REQUEST );
    }

    /**
     * Shows the master status
     *
     * @param connection Connection
     * @return The master log position
     * @throws SQLException If a SQL exception occurred
     */
    public int masterShowStatus( Connection connection )
        throws SQLException
    {
        Statement st = connection.createStatement(  );

        if ( _logger.isDebugEnabled(  ) )
        {
            _logger.debug( STR_REQUEST + MASTER_SHOW_STATUS_REQUEST );
        }

        ResultSet rs = st.executeQuery( MASTER_SHOW_STATUS_REQUEST );
        int status = 0;

        if ( !rs.wasNull(  ) && rs.next(  ) )
        {
            status = rs.getInt( MASTER_LOG_POSITION );
        }

        return status;
    }

    /**
     * Shows the slave status
     *
     * @param connection Connection
     * @return The slave log position
     * @throws SQLException If a SQL exception occurred
     */
    public int slaveShowStatus( Connection connection )
        throws SQLException
    {
        Statement st = connection.createStatement(  );

        if ( _logger.isDebugEnabled(  ) )
        {
            _logger.debug( STR_REQUEST + SLAVE_SHOW_STATUS_REQUEST );
        }

        ResultSet rs = st.executeQuery( SLAVE_SHOW_STATUS_REQUEST );
        int status = 0;

        if ( !rs.wasNull(  ) && rs.next(  ) )
        {
            status = rs.getInt( SLAVE_LOG_POSITION );
        }

        return status;
    }
}
