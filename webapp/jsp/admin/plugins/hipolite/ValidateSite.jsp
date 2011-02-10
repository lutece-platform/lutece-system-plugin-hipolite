<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />
<jsp:useBean id="hipolite" scope="session" class="fr.paris.lutece.plugins.hipolite.web.HipoliteJspBean" />

<% hipolite.init( request, hipolite.RIGHT_MANAGE_HIPOLITE ); %>
<%= hipolite.getValidateSite ( request ) %>

<%@ include file="../../AdminFooter.jsp" %>   
