#!/bin/bash -u

###################
# tomcat_init_flags_preprod.sh
# Version 2.0
# 11/06/2008
##############################
# Codes de retour :
#	255	=>	Fichier de constante non trouv�
#			(interruption avant traitement)
#	0	=> 	Aucune erreur
#	1	=> 	Erreur : flag (validation du site)
#	2	=>	Erreur : flag (synchro en cours)
##############################

# Inclusion du fichier de fonctions
. $HIPOLITE_SH/tomcat_functions_preprod.sh

# Inclusion des constantes
IncludeConstants

####################################
# Script d'initialisation des flags (t�moins) de pr�-production
####################################

# Teste la pr�sence du fichier flag indiquant que le site est valid� (plugin hipolite)
if [ ! -f $flags_repository/$flag_validation ] ; then
	# Message plac� dans le log d'erreur
	InsertMessageError "le site n a pas ete valide (pas de mise en production)"
	
	# Suppression des flags
	DeleteFlags
	
	# Fin du script
    exit 1
fi

# Teste la pr�sence du fichier flag de synchronisation en cours
if [ -f $flags_repository/$flag_active ] ; then
	# Message plac� dans le log d'erreur
	InsertMessageError "le site est deja en cours de synchronisation"
	
	# Fin du script
    exit 2
fi

# Cr�ation du flag de synchronisation en cours
touch $flags_repository/$flag_active

# Message plac� dans le log de bon d�roulement
InsertMessageOk "l initialisation des flags (temoins) s est deroulee avec succes"

# Fin du script
exit 0
