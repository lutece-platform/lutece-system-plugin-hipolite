#!/bin/bash -u

####################
# tomcat_copy_data_preprod.sh
# Version 2.0
# 30/05/2008
#####################################
# Codes de retour :
#	255	=>	Fichier de constante non trouvé
#			(interruption avant traitement)
#	0	=> 	Aucune erreur
#	1	=> 	Erreur : fichiers TO_TAKE et TO_IGNORE
#	2	=> 	Erreur : création de l'archive (tar)
#	3	=> 	Erreur : compression de l'archive (gzip)
#	4	=> 	Erreur : transfert des données (scp)
#####################################

# Inclusion du fichier de fonctions
. $HIPOLITE_SH/tomcat_functions_preprod.sh

# Inclusion des constantes
IncludeConstants

#####################################################
# Script de création et d'envoi des données du site de pré-production (éléments File System)
#####################################################

# Initialisation avec la date courante
current_date=`GetDateTime`

# Teste la présence des fichiers TO_TAKE (liste des éléments File System à copier)  et TO_IGNORE (liste d'exclusion)
if [ ! -f $HIPOLITE_SH/TO_TAKE ] || [ ! -f $HIPOLITE_SH/TO_IGNORE ] ; then
	# Message placé dans le log d'erreur
	InsertMessageError "une erreur est survenue lors de la recherche des fichiers TO_TAKE et TO_IGNORE"
	
	# Suppression des flags
	DeleteFlags
	
	# Fin du script
    exit 1
fi

# Se place dans le répertoire racine (/)  de la webapp
cd $webapps_path/$webapp_name

# Création de l'archive (.tar) en utilisant la liste des éléments File System à copier (TO_TAKE)  et la liste d'exclusion (TO_IGNORE)
tar -cpf  $temp_repository/$webapp_name"_"$current_date.tar -T $HIPOLITE_SH/TO_TAKE --exclude-from $HIPOLITE_SH/TO_IGNORE

# Erreur lors de la création de l'archive ou archive non présente
#if [ $? -gt 0 ] || [ ! -f $temp_repository/$webapp_name"_"$current_date.tar ] ; then
if [ $? -eq 1 ] || [ ! -f $temp_repository/$webapp_name"_"$current_date.tar ] ; then
	# Message placé dans le log d'erreur
	InsertMessageError "une erreur est survenue lors de la creation de l archive ou celle-ci n a pas ete trouvee"
	
	# Suppression des flags
	DeleteFlags
	
	# Fin du script
    exit 2
fi

# Compression de l'archive
gzip -f $temp_repository/$webapp_name"_"$current_date.tar

# Erreur lors de la compression de l'archive ou archive non présente
if [ $? -gt 0 ] || [ ! -f $temp_repository/$webapp_name"_"$current_date.tar.gz ] ; then
	# Message placé dans le log d'erreur
	InsertMessageError "une erreur est survenue lors de la compression de l archive ou celle-ci n a pas ete trouvee"
	
	# Suppression des flags
	DeleteFlags
	
	# Fin du script
    exit 3
fi

#########################################
# Envoi par SCP de l'archive compressée vers le serveur de production 
#########################################

# Envoi de l'archive (.tar.gz) vers le serveur de production ($SERVER_TOM_PROD) dans le répertoire contenant les archives
scp -Bp $temp_repository/$webapp_name"_"$current_date.tar.gz $USER_TOMCAT_PROD@$SERVER_TOMCAT_PROD:$archives_repository

# Erreur lors de l'envoi de l'archive
if [ $? -gt 0 ] ; then
	# Message placé dans le log d'erreur
	InsertMessageError "une erreur est survenue lors de l envoi de l archive par SCP"
	
	# Suppression des flags
	DeleteFlags
	
	# Fin du script
    exit 4
fi

# Nettoyage du contenu du répertoire temp
rm -rf $temp_repository/*

# Message placé dans le log de bon déroulement
InsertMessageOk "la copie des donnees du site de pre-production s est deroulee avec succes"

# Fin du script
exit 0
