<%@page import="ch.ethz.inf.dbproject.forms.CommentForm"%>
<%@page import="java.lang.*"%>
<%@page import="java.util.HashMap"%>
<%@page import="ch.ethz.inf.dbproject.forms.PersonOfInterestForm"%>
<%@page import="ch.ethz.inf.dbproject.model.Case"%>
<%@page import="ch.ethz.inf.dbproject.util.UserManagement"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp" %>

<h1>Edit Person of Interest ${poiName}</h1>

<% HashMap<String,String> initialValues = (HashMap<String,String>)request.getAttribute("initialValues"); %>
<%= new PersonOfInterestForm().generateEditForm(initialValues) %>

<%@ include file="Footer.jsp"%>