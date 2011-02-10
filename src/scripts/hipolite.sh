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

# Initialisation des flags (témoins) de pré-production
ssh $USER_TOMCAT_PREPROD@$SERVER_TOMCAT_PREPROD ". \$HOME/.bash_profile ; \$HIPOLITE_SH/tomcat_init_flags_preprod.sh"
ReturnCode $?

# Copie des données du site de pré-production (éléments File System)
ssh $USER_TOMCAT_PREPROD@$SERVER_TOMCAT_PREPROD ". \$HOME/.bash_profile ; \$HIPOLITE_SH/tomcat_copy_data_preprod.sh"
ReturnCode $?

# Déploiement des données sur le site de production (éléments File System)
ssh $USER_TOMCAT_PROD@$SERVER_TOMCAT_PROD ". \$HOME/.bash_profile ; \$HIPOLITE_SH/tomcat_deploy_data_prod.sh"
ReturnCode $?

# Lancement de la réplication MySQL
ssh $USER_TOMCAT_PREPROD@$SERVER_TOMCAT_PREPROD ". \$HOME/.bash_profile ; \$HIPOLITE_SH/mysql_replication_preprod.sh"
ReturnCode $?

# Finalisation des flags (témoins) de pré-production
ssh $USER_TOMCAT_PREPROD@$SERVER_TOMCAT_PREPROD ". \$HOME/.bash_profile ; \$HIPOLITE_SH/tomcat_final_flags_preprod.sh"
ReturnCode $?
