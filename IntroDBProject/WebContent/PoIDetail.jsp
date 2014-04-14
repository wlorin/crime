<%@page import="ch.ethz.inf.dbproject.forms.CommentForm"%>
<%@page import="ch.ethz.inf.dbproject.util.UserManagement"%>
<%@page import="ch.ethz.inf.dbproject.model.User"%>
<%@page import="java.util.HashMap"%>
<%@page import="ch.ethz.inf.dbproject.forms.PoINoteForm"%>
<%@page import="ch.ethz.inf.dbproject.forms.PersonOfInterestForm"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp" %>
<%
	final User user = (User) session.getAttribute(UserManagement.SESSION_USER);
%>
<h1>Persons of Interest</h1>

<hr/>

<%= session.getAttribute("poi") %>
<h2>Notes</h2>
${poiNotes }
<hr/>

<%
if (user != null) {
	HashMap<String, String> initialValues = new HashMap<String, String>();
	initialValues.put(CommentForm.REFERENCE_ID, "" + request.getParameter("id"));
%>

	<%= new PoINoteForm().generateNewForm(initialValues) %>

<% } %>
<%@ include file="Footer.jsp" %>