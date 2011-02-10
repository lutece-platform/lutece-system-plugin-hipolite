#!/bin/bash -u

# $SERVER_TOMCAT_PREPROD	Serveur tomcat (pré-prod)
# $USER_TOMCAT_PREPROD	Nom utilisateur tomcat (pré-prod)
# $SERVER_TOMCAT_PROD		Serveur tomcat (prod)
# $USER_TOMCAT_PROD		Nom utilisateur tomcat (prod)
# $PORT_TOMCAT_PROD		Port tomcat (prod)

# $HIPOLITE_SH			Répertoire contenant les scripts Hipolite
# $TOMCAT_HOME			Répertoire racine d'Apache Tomcat

function ReturnCode {
    echo -n "RC = $1"
    if [ $1 -eq 0 ]; then
        echo "  [OK]"
    else
        echo "  [ERREUR]"
		exit 1
    fi
}

# Lancement de la page JSP responsable du vidage des caches de Lutece
ssh $USER_TOMCAT_PREPROD@$SERVER_TOMCAT_PREPROD ". \$HOME/.bash_profile ; \$HIPOLITE_SH/tomcat_reset_caches_preprod.sh"
ReturnCode $?
