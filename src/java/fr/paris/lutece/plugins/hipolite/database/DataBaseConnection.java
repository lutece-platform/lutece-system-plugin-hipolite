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
package fr.paris.lutece.plugins.hipolite.database;

import fr.paris.lutece.plugins.hipolite.exceptions.HipoliteException;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * This class is used to manage the database connection
 */
public class DataBaseConnection
{
    // Declares and creates the Logger 
    private static final Logger _logger = Logger.getLogger( DataBaseConnection.class );

    // Database information
    private String _driver;
    private String _url;
    private String _user;
    private String _pwd;

    /**
     * Constructs the DataBaseConnection object
     *
     * @param driver String
     * @param url String
     * @param user String
     * @param pwd String
     */
    public DataBaseConnection( String driver, String url, String user, String pwd )
    {
        this._driver = driver;
        this._url = url;
        this._user = user;
        this._pwd = pwd;
    }

    /**
     * Gets the database connection
     *
     * @throws HipoliteException If an Hipolite exception occurred
     * @return The database connection
     */
    public Connection getConnection(  ) throws HipoliteException
    {
        Connection connection = null;

        try
        {
            Class.forName( this._driver );
            connection = DriverManager.getConnection( this._url, this._user, this._pwd );
        }
        catch ( ClassNotFoundException cnfe )
        {
            _logger.error( cnfe.getMessage(  ) );
            throw new HipoliteException( cnfe.getMessage(  ) );
        }
        catch ( SQLException sqle )
        {
            _logger.error( sqle.getMessage(  ) );
            throw new HipoliteException( sqle.getMessage(  ) );
        }

        return connection;
    }

    /**
     * Closes the database connection
     *
     * @param connection Connection
     * @throws HipoliteException If an Hipolite exception occurred
     */
    public void closeConnection( Connection connection )
        throws HipoliteException
    {
        try
        {
            if ( connection != null )
            {
                connection.close(  );
            }
        }
        catch ( SQLException sqle )
        {
            _logger.error( sqle.getMessage(  ) );
            throw new HipoliteException( sqle.getMessage(  ) );
        }
    }
}
