#!/bin/bash -u

###################
# mysql_replication_preprod.sh
# Version 2.0
# 30/05/2008
#############################
# Codes de retour :
#	255	=>	Fichier de constante non trouv�
#			(interruption avant traitement)
#	0	=> 	Aucune erreur
#############################

# Inclusion du fichier de fonctions
. $HIPOLITE_SH/tomcat_functions_preprod.sh

# Inclusion des constantes
IncludeConstants

############################
# Script de lancement de la r�plication MySQL
############################

# R�pertoire contenant le fichier hipolite.properties (resource bundle)
conf_path=$webapps_path/$webapp_name/WEB-INF/conf/plugins

# R�pertoire contenant tous les .jar (y compris hipolite)
lib_path=$webapps_path/$webapp_name/WEB-INF/lib

# Initialisation du classpath avec le r�pertoire conf
tmp_cp=$conf_path

# Ajoute au classpath tous les .jar du r�pertoire lib
for f in $lib_path/*.jar
do
	tmp_cp=$tmp_cp:$f
done

#######################
# Lancement de la r�plication MySQL
#######################

# Lance la classe Java (ReplicationBatch) en utilisant le classpath pr�c�demment d�fini
java -cp $tmp_cp fr.paris.lutece.plugins.hipolite.replication.ReplicationBatch

#  Erreur lors de la r�plication MySQL
if [ $? -gt 0 ] ; then
	# Message plac� dans le log d'erreur
	InsertMessageError "une erreur est survenue lors de la replication MySQL"
	
	# Suppression des flags
	DeleteFlags
	
	# Fin du script
	exit 1
fi

# Message plac� dans le log de bon d�roulement
InsertMessageOk "la replication MySQL s est deroulee avec succes"

# Fin du script
exit 0
