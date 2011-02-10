<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:useBean id="system" scope="session" class="fr.paris.lutece.portal.web.system.SystemJspBean" />

<%
    response.sendRedirect( system.doResetCaches());
%>
