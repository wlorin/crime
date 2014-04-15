<%@page import="ch.ethz.inf.dbproject.forms.CommentForm"%>
<%@page import="java.lang.*"%>
<%@page import="java.util.HashMap"%>
<%@page import="ch.ethz.inf.dbproject.forms.CaseNoteForm"%>
<%@page import="ch.ethz.inf.dbproject.model.Case"%>
<%@page import="ch.ethz.inf.dbproject.util.UserManagement"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp" %>

<h1>Case Details</h1>

<%=session.getAttribute("caseTable") %>
<% if ((Boolean)session.getAttribute("isOpen") && UserManagement.isUserLoggedIn(session)) { %>
	<a href="EditCase?id=${caseId}">edit case</a>
<% } %>

${openCloseButton}

<h2>Notes</h2>
<%=session.getAttribute("caseNoteTable") %>

${newCaseNote}


<%@ include file="Footer.jsp"%>