#!/bin/bash -u

# $SERVER_TOMCAT_PREPROD	Serveur tomcat (pr�-prod)
# $USER_TOMCAT_PREPROD	Nom utilisateur tomcat (pr�-prod)
# $SERVER_TOMCAT_PROD		Serveur tomcat (prod)
# $USER_TOMCAT_PROD		Nom utilisateur tomcat (prod)
# $PORT_TOMCAT_PROD		Port tomcat (prod)

# $HIPOLITE_SH			R�pertoire contenant les scripts Hipolite
# $TOMCAT_HOME			R�pertoire racine d'Apache Tomcat

function ReturnCode {
    echo -n "RC = $1"
    if [ $1 -eq 0 ]; then
        echo "  [OK]"
    else
        echo "  [ERREUR]"
		exit 1
    fi
}

# Initialisation des flags (t�moins) de pr�-production
ssh $USER_TOMCAT_PREPROD@$SERVER_TOMCAT_PREPROD ". \$HOME/.bash_profile ; \$HIPOLITE_SH/tomcat_init_flags_preprod.sh"
ReturnCode $?

# Copie des donn�es du site de pr�-production (�l�ments File System)
ssh $USER_TOMCAT_PREPROD@$SERVER_TOMCAT_PREPROD ". \$HOME/.bash_profile ; \$HIPOLITE_SH/tomcat_copy_data_preprod.sh"
ReturnCode $?

# D�ploiement des donn�es sur le site de production (�l�ments File System)
ssh $USER_TOMCAT_PROD@$SERVER_TOMCAT_PROD ". \$HOME/.bash_profile ; \$HIPOLITE_SH/tomcat_deploy_data_prod.sh"
ReturnCode $?

# Lancement de la r�plication MySQL
ssh $USER_TOMCAT_PREPROD@$SERVER_TOMCAT_PREPROD ". \$HOME/.bash_profile ; \$HIPOLITE_SH/mysql_replication_preprod.sh"
ReturnCode $?

# Finalisation des flags (t�moins) de pr�-production
ssh $USER_TOMCAT_PREPROD@$SERVER_TOMCAT_PREPROD ". \$HOME/.bash_profile ; \$HIPOLITE_SH/tomcat_final_flags_preprod.sh"
ReturnCode $?
