#!/bin/bash -u

##################
# tomcat_constantes_prod.sh
# Version 2.0
# 03/06/2008
##################

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

# Chemin du répertoire de données d'Hipolite
hipolite_repository=$HOME/datas/hipolite

# Chemin du répertoire où sont envoyées les archives
archives_repository=$hipolite_repository/archives
