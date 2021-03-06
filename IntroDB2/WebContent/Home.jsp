<%@page import="ch.ethz.inf.dbproject.model.User"%>
<%@page import="ch.ethz.inf.dbproject.HomeServlet"%>
<%@page import="ch.ethz.inf.dbproject.util.UserManagement"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp" %>

<%
final User user = (User) session.getAttribute(UserManagement.SESSION_USER);

if (user != null) {
	// There is a user logged in! Display a greeting!
%>
	Welcome back <%=user.getName()%>
<%
} else {
	// No user logged in.%>
	Welcome! 
	<br />
	Currently, you are not logged in. To log in go to <a href="User">user profile</a>. 
<%
}
%>

<br /><br />
See all available <a href="Cases">cases</a> and <a href="PoI">persons of interest</a>.

<br /><br />
In case you screwed something up, you can always reset the database to the demo data <a href="?action=reset">here</a>

<%@ include file="Footer.jsp" %>