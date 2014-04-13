<%@page import="ch.ethz.inf.dbproject.forms.CommentForm"%>
<%@page import="java.lang.*"%>
<%@page import="java.util.HashMap"%>
<%@page import="ch.ethz.inf.dbproject.forms.CaseNoteForm"%>
<%@page import="ch.ethz.inf.dbproject.model.User"%>
<%@page import="ch.ethz.inf.dbproject.util.UserManagement"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp" %>
<%
	final User user = (User) session.getAttribute(UserManagement.SESSION_USER);
%>

<h1>Case Details</h1>

<%=session.getAttribute("caseTable")%>

<h2>Notes</h2>
<%=session.getAttribute("caseNoteTable")%>
<%
	//TODO close or reopen the case
%>


<%
	if (user != null) {
	HashMap<String, String> initialValues = new HashMap<String, String>();
	initialValues.put(CommentForm.REFERENCE_ID, "" + request.getParameter("id"));
%>
	<%=new CaseNoteForm().generateNewForm(initialValues)%>
<%
}
%>

<%
	//TODO Display existing comments
	//session.getAttribute("commentTable")
%>

<%@ include file="Footer.jsp"%>