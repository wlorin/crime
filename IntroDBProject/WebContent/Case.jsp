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

${openCloseButton}

<h2>Notes</h2>
<%=session.getAttribute("caseNoteTable")%>

${newCaseNote}


<%@ include file="Footer.jsp"%>