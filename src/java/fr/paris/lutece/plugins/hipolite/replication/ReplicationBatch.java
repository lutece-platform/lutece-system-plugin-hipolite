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
package fr.paris.lutece.plugins.hipolite.replication;

import fr.paris.lutece.plugins.hipolite.exceptions.HipoliteException;

import org.apache.log4j.Logger;


/**
 * This class is used to start the replication process
 */
public final class ReplicationBatch
{
    // Declares and creates the Logger 
    private static final Logger logger = Logger.getLogger( ReplicationBatch.class );

    /**
     * Empty constructor (private)
     */
    private ReplicationBatch(  )
    {
        // Empty constructor
    }

    /**
    * Main (starts the replication process)
    *
    * @param args String[]
    */
    public static void main( String[] args )
    {
        // Initialization
        int status = 0;

        // Time before
        long timeBefore = System.currentTimeMillis(  );

        // Launches the replication
        Replication replication = new Replication(  );

        try
        {
            replication.init(  );
            replication.start(  );
            replication.close(  );
        }
        catch ( HipoliteException he )
        {
            status = 1;
        }

        // Time after
        long timeAfter = System.currentTimeMillis(  );
        long time = timeAfter - timeBefore;

        if ( logger.isDebugEnabled(  ) )
        {
            logger.debug( "Time : " + time + " ms" );
        }

        // Exits
        System.exit( status );
    }
}
