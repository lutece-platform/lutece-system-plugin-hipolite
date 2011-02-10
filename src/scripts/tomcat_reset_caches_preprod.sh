#!/bin/bash -u

#####################
# tomcat_reset_caches_preprod.sh
# Version 2.0
# 11/06/2008
##############################
# Codes de retour :
#	255	=>	Fichier de constante non trouvé
#			(interruption avant traitement)
#	0	=> 	Aucune erreur
#	1	=> 	Erreur : flag (site non synchro)
##############################

# Inclusion du fichier de fonctions
. $HIPOLITE_SH/tomcat_functions_preprod.sh

# Inclusion des constantes
IncludeConstants

###############################################
# Script de lancement de la page JSP responsable du vidage des caches de Lutece
###############################################

# Teste la présence du fichier flag indiquant que la synchronisation du site a été effectuée
if [ ! -f $flags_repository/$flag_finished ] ; then
	# Message placé dans le log d'erreur
	InsertMessageError "le site n a pas ete synchronise (pas de vidage des caches)"
	
	# Fin du script
    exit 1
fi

# Lancement de la page JSP responsable du vidage des caches de Lutece
wget http://$SERVER_TOMCAT_PROD:$PORT_TOMCAT_PROD/$webapp_name/jsp/site/DoResetCaches.jsp

# Suppression du flag indiquant que la synchronisation du site a été effectuée
if [ -f $flags_repository/$flag_finished ] ; then
	rm -f $flags_repository/$flag_finished
fi

# Message placé dans le log de bon déroulement
InsertMessageOk "le vidage des caches de Lutece s est deroule avec succes"

# Fin du script
exit 0
