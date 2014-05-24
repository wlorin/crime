<%@page import="ch.ethz.inf.dbproject.util.UserManagement"%>
<%@page import="ch.ethz.inf.dbproject.forms.ConvictForm"%>
<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp" %>

<h1>Convict 
<%= session.getAttribute("customTitle") %></h1>

<hr/>

<% if (UserManagement.isUserLoggedIn(session)) {
	Integer caseid = Integer.parseInt(request.getParameter("CaseId"));
	Integer poiid = Integer.parseInt(request.getParameter("PoIId"));
	HashMap<String, String> initialValues = new HashMap<String, String>();
	initialValues.put(ConvictForm.REFERENCE_CASEID, caseid.toString());
	initialValues.put(ConvictForm.REFERENCE_POIID, poiid.toString());
	%>
	<%= new ConvictForm().generateNewForm(initialValues, "Convict") %>
	<%
}

%>

<hr/>

<%@ include file="Footer.jsp" %>