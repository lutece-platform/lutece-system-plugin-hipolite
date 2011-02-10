#!/bin/bash -u

####################
# tomcat_final_flags_preprod.sh
# Version 2.0
# 11/06/2008
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

####################################
# Script de finalisation des flags (t�moins) de pr�-production
####################################

# Suppression du flag de synchronisation en cours
if [ -f $flags_repository/$flag_active ] ; then
	rm -f $flags_repository/$flag_active
fi

# Suppression du flag indiquant que le site est valid� (plugin hipolite)
if [ -f $flags_repository/$flag_validation ] ; then
	rm -f $flags_repository/$flag_validation
fi

# Cr�ation du flag indiquant que la synchronisation du site a �t� effectu�e
touch $flags_repository/$flag_finished

# Message plac� dans le log de bon d�roulement
InsertMessageOk "la finalisation des flags (temoins) s est deroulee avec succes"

# Fin du script
exit 0
