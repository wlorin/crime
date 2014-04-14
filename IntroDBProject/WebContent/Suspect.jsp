<%@page import="ch.ethz.inf.dbproject.util.UserManagement"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp" %>

<h1>Suspects and Convicts for Case <%= session.getAttribute("casename") %></h1>
<h2>Convicted Persons of Interest</h2>

<hr/>

<%= session.getAttribute("convicts") %>

<hr/>
<% if (!"true".equals(session.getAttribute("caseclosed"))) { %>
<h2>Suspected Persons of Interest</h2>

<hr/>

<%= session.getAttribute("suspect") %>

<hr/>
<% if (UserManagement.isUserLoggedIn(session)) { %>
<h2>Other Persons of Interest</h2>
<hr />
<%= session.getAttribute("poisnotlinkted") %>
<% } } %>

<%@ include file="Footer.jsp" %>