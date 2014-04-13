<%@page import="ch.ethz.inf.dbproject.forms.Form"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp" %>

<span style="color: red; font-weight: bold"><%= (String) session.getAttribute(Form.SESSION_FORM_FAIL_MSG) %> </span>
<%= (String) session.getAttribute(Form.SESSION_FORM_FAIL) %> </h2>
	
<%@ include file="Footer.jsp" %>