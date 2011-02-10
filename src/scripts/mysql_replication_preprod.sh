#!/bin/bash -u

###################
# mysql_replication_preprod.sh
# Version 2.0
# 30/05/2008
#############################
# Codes de retour :
#	255	=>	Fichier de constante non trouvé
#			(interruption avant traitement)
#	0	=> 	Aucune erreur
#############################

# Inclusion du fichier de fonctions
. $HIPOLITE_SH/tomcat_functions_preprod.sh

# Inclusion des constantes
IncludeConstants

############################
# Script de lancement de la réplication MySQL
############################

# Répertoire contenant le fichier hipolite.properties (resource bundle)
conf_path=$webapps_path/$webapp_name/WEB-INF/conf/plugins

# Répertoire contenant tous les .jar (y compris hipolite)
lib_path=$webapps_path/$webapp_name/WEB-INF/lib

# Initialisation du classpath avec le répertoire conf
tmp_cp=$conf_path

# Ajoute au classpath tous les .jar du répertoire lib
for f in $lib_path/*.jar
do
	tmp_cp=$tmp_cp:$f
done

#######################
# Lancement de la réplication MySQL
#######################

# Lance la classe Java (ReplicationBatch) en utilisant le classpath précédemment défini
java -cp $tmp_cp fr.paris.lutece.plugins.hipolite.replication.ReplicationBatch

#  Erreur lors de la réplication MySQL
if [ $? -gt 0 ] ; then
	# Message placé dans le log d'erreur
	InsertMessageError "une erreur est survenue lors de la replication MySQL"
	
	# Suppression des flags
	DeleteFlags
	
	# Fin du script
	exit 1
fi

# Message placé dans le log de bon déroulement
InsertMessageOk "la replication MySQL s est deroulee avec succes"

# Fin du script
exit 0
