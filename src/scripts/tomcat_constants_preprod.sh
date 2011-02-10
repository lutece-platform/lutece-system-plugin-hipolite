#!/bin/bash -u

###################
# tomcat_constantes_preprod.sh
# Version 2.0
# 11/06/2008
###################

#######
# WebApp
#######

# Chemin du r�pertoire "webapps" de Tomcat
webapps_path=$TOMCAT_HOME/webapps

# Nom de la webapp
#webapp_name=intraparis
webapp_name=site

#######
# Hipolite
#######

# Chemin du r�pertoire de donn�es d'Hipolite (d�but)
hipolite_repository=datas/hipolite

# Chemin du r�pertoire o� sont envoy�es les archives
archives_repository=$hipolite_repository/archives

# Chemin du r�pertoire de donn�es d'Hipolite (fin)
hipolite_repository=$HOME/$hipolite_repository

# Chemin du r�pertoire temporaire (archives)
temp_repository=$hipolite_repository/tmp

###########
# Flags (t�moins)
###########

# Chemin du r�pertoire contenant les flags
flags_repository=$hipolite_repository/flags

# Nom du flag de synchronisation en cours
flag_active=$webapp_name"_active.flag"

# Nom du flag indiquant que le site est valid� (plugin hipolite)
flag_validation=$webapp_name"_validation.flag"

# Nom du flag indiquant que la synchronisation du site a �t� effectu�e
flag_finished=$webapp_name"_finished.flag"

#####
# Logs
#####

# Chemin du r�pertoire contenant les logs
logs_repository=$hipolite_repository/logs

# Nom du log d'erreur
log_error=$webapp_name"_error.log"

# Nom du log de bon d�roulement
log_ok=$webapp_name"_ok.log"
