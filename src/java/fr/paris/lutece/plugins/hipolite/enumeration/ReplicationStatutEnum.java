package fr.paris.lutece.plugins.hipolite.enumeration;

public enum ReplicationStatutEnum
{
    NO_REPLICATION( "0" ), IN_PROGRESS( "1" ), ON_ERROR( "2" ), ON_SUCCESS( "3" );

    private String _strStatut;

    private ReplicationStatutEnum( String strStatut )
    {
        _strStatut = strStatut;
    }

    /**
     * @return the _nStatut
     */
    public String getStatut( )
    {
        return _strStatut;
    }
}
