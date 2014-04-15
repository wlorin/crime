<%@page import="ch.ethz.inf.dbproject.util.UserManagement"%>
<%@page import="ch.ethz.inf.dbproject.forms.PersonOfInterestForm"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp" %>

<h1>Persons of Interest</h1>

<hr/>

<%= session.getAttribute("poI") %>

<hr/>

<% 
if (UserManagement.isUserLoggedIn(session)) {
	// User is logged in. Display the details:
%>

	<%= new PersonOfInterestForm().generateNewForm() %>

<% } %>
<%@ include file="Footer.jsp" %>