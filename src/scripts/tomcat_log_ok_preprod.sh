#!/bin/bash -u

#################
# tomcat_log_ok_preprod.sh
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

######################################
# Script d'insertion d'un message dans le log de bon d�roulement
######################################

# Initialisation avec la date courante
current_date=`GetDateTime`

# Teste le nombre de param�tres
if [ $# -eq 1 ] ; then
	# Message plac� dans le log de bon d�roulement
	message_ok="$current_date ($webapp_name) : $1"
	echo $message_ok >> $logs_repository/$log_ok
fi

# Fin du script
exit 0
