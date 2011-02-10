#!/bin/bash -u

###################
# tomcat_log_error_preprod.sh
# Version 2.0
# 03/06/2008
##############################
# Codes de retour :
#	255	=>	Fichier de constante non trouv�
#			(interruption avant traitement)
#	0	=> 	Aucune erreur
##############################

# Inclusion du fichier de fonctions
. $HIPOLITE_SH/tomcat_functions_preprod.sh

# Inclusion des constantes
IncludeConstants

###############################
# Script d'insertion d'un message dans le log d'erreur
###############################

# Initialisation avec la date courante
current_date=`GetDateTime`

# Teste le nombre de param�tres
if [ $# -eq 1 ] ; then
	# Suppression du log de bon d�roulement
	#if [ -f $logs_repository/$log_ok ] ; then
		#rm -f $logs_repository/$log_ok
	#fi

	# Message plac� dans le log d'erreur
	message_error="$current_date ($webapp_name) : $1"
	echo $message_error >> $logs_repository/$log_error
fi

# Fin du script
exit 0
