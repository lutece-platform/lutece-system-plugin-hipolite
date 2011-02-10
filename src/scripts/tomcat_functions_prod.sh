#!/bin/bash -u

#################
# tomcat_fonctions_prod.sh
# Version 2.0
# 03/06/2008
#################

# Inclusion des constantes
function IncludeConstants {
	# Teste la pr�sence du fichier de constantes
	if [ -f $HIPOLITE_SH/tomcat_constants_prod.sh ] ; then
		# Inclusion du fichier de constantes
		. $HIPOLITE_SH/tomcat_constants_prod.sh
    else
		# Message plac� dans le log d'erreur
		InsertMessageError "le script contenant les declarations de variables n a pas ete trouve"
		
		# Suppression des flags
		DeleteFlags
		
		# Fin du script
		exit 255
    fi
}

# Retourne la date courante
function GetDateTime {
	echo `date +%Y-%m-%d@%Hh%Mm%Ss`
}

# Suppression des flags
function DeleteFlags {
	# Ex�cution du script de finalisation des flags (t�moins) de pr�-production
	ssh $USER_TOMCAT_PREPROD@$SERVER_TOMCAT_PREPROD ". \$HOME/.bash_profile ; \$HIPOLITE_SH/tomcat_del_flags_preprod.sh"
}

# Insertion d'un message dans le log de bon d�roulement
function InsertMessageOk {
	# Teste le nombre de param�tres
	if [ $# -eq 1 ] ; then
		# Ex�cution du script d'insertion d'un message dans le log de bon d�roulement
		ssh $USER_TOMCAT_PREPROD@$SERVER_TOMCAT_PREPROD ". \$HOME/.bash_profile ; \$HIPOLITE_SH/tomcat_log_ok_preprod.sh '$1'"
	fi
}

# Insertion d'un message dans le log d'erreur
function InsertMessageError {
	# Teste le nombre de param�tres
	if [ $# -eq 1 ] ; then
		# Ex�cution du script d'insertion d'un message dans le log d'erreur
		ssh $USER_TOMCAT_PREPROD@$SERVER_TOMCAT_PREPROD ". \$HOME/.bash_profile ; \$HIPOLITE_SH/tomcat_log_error_preprod.sh '$1'"
	fi
}
