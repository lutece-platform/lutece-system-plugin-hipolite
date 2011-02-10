#!/bin/bash -u

###################
# tomcat_constantes_preprod.sh
# Version 2.0
# 11/06/2008
###################

#######
# WebApp
#######

# Chemin du répertoire "webapps" de Tomcat
webapps_path=$TOMCAT_HOME/webapps

# Nom de la webapp
#webapp_name=intraparis
webapp_name=site

#######
# Hipolite
#######

# Chemin du répertoire de données d'Hipolite (début)
hipolite_repository=datas/hipolite

# Chemin du répertoire où sont envoyées les archives
archives_repository=$hipolite_repository/archives

# Chemin du répertoire de données d'Hipolite (fin)
hipolite_repository=$HOME/$hipolite_repository

# Chemin du répertoire temporaire (archives)
temp_repository=$hipolite_repository/tmp

###########
# Flags (témoins)
###########

# Chemin du répertoire contenant les flags
flags_repository=$hipolite_repository/flags

# Nom du flag de synchronisation en cours
flag_active=$webapp_name"_active.flag"

# Nom du flag indiquant que le site est validé (plugin hipolite)
flag_validation=$webapp_name"_validation.flag"

# Nom du flag indiquant que la synchronisation du site a été effectuée
flag_finished=$webapp_name"_finished.flag"

#####
# Logs
#####

# Chemin du répertoire contenant les logs
logs_repository=$hipolite_repository/logs

# Nom du log d'erreur
log_error=$webapp_name"_error.log"

# Nom du log de bon déroulement
log_ok=$webapp_name"_ok.log"
