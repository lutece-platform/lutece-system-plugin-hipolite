#!/bin/bash -u

###################
# tomcat_deploy_data_prod.sh
# Version 2.0
# 03/06/2008
###################################
# Codes de retour :
#	255	=>	Fichier de constante non trouv�
#			(interruption avant traitement)
#	0	=> 	Aucune erreur
#	1	=> 	Erreur : aucune archive trouv�e
#	2	=> 	Erreur : r�cup�ration du nom de l'archive
#	3	=> 	Erreur : d�compression de l'archive
###################################

# Inclusion du fichier de fonctions
. $HIPOLITE_SH/tomcat_functions_prod.sh

# Inclusion des constantes
IncludeConstants

#################################################
# Script de d�ploiement des donn�es sur le site de production (�l�ments File System)
#################################################

# Compte le nombre d'archives (.tar.gz) pr�sentes sur le site de production (r�pertoire archives)
nb_files=`ls $archives_repository/*.tar.gz | wc -w`

# Erreur sur le nombre d'archives : aucune archive
if [ $nb_files -lt 1 ] ; then
	# Message plac� dans le log d'erreur
	InsertMessageError "aucune archive n a ete trouvee sur le site de production"
	
	# Suppression des flags
	DeleteFlags
	
	# Fin du script
    exit 1
fi

#################################
# D�ploiement de l'archive en attente dans le repository
#################################

# Se place dans le r�pertoire contenant les archives
cd $archives_repository

# R�cup�re le nom des archives (.tar.gz)
list_of_archives=`ls *.tar.gz`

# Prend le premier �l�ment de la liste
for archive in $list_of_archives
do
	archive_name=$archive
done

# Erreur lors de la r�cup�ration du nom de l'archive
if [ $? -gt 0 ] ; then
	# Message plac� dans le log d'erreur
	InsertMessageError "une erreur est survenue lors de la recuperation du nom de l archive"
	
	# Suppression des flags
	DeleteFlags
	
	# Fin du script
    exit 2
fi

# D�placement de l'archive dans le r�pertoire racine (/) de la webapp
mv $archive_name $webapps_path/$webapp_name

# Suppression des archives (temporaires)
rm -f *.tar.gz

# Se place dans le r�pertoire racine (/) de la webapp
cd $webapps_path/$webapp_name

# D�compression de l'archive
tar xfz $archive_name

# Erreur lors de la d�compression de l'archive
if [ $? -gt 0 ] ; then
	# Message plac� dans le log d'erreur
	InsertMessageError "une erreur est survenue lors de la decompression de l archive"
	
	# Suppression des flags
	DeleteFlags
	
	# Fin du script
    exit 3
fi

# Suppression de l'archive
rm -f $archive_name

# Message plac� dans le log de bon d�roulement
InsertMessageOk "le deploiement des donnees du site de production s est deroule avec succes"

# Fin du script
exit 0
