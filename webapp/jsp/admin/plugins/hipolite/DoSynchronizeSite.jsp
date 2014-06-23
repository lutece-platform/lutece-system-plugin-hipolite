<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:useBean id="hipolite" scope="session" class="fr.paris.lutece.plugins.hipolite.web.HipoliteJspBean" />

<%
    hipolite.init( request, hipolite.RIGHT_MANAGE_HIPOLITE );
    response.sendRedirect( hipolite.doSynchronizeSite( request ) );
%>
