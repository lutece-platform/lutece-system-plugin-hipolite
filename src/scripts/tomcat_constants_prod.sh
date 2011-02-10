#!/bin/bash -u

##################
# tomcat_constantes_prod.sh
# Version 2.0
# 03/06/2008
##################

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

# Chemin du r�pertoire de donn�es d'Hipolite
hipolite_repository=$HOME/datas/hipolite

# Chemin du r�pertoire o� sont envoy�es les archives
archives_repository=$hipolite_repository/archives
