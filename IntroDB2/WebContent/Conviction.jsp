<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp" %>

<h1>Convictions</h1>

<p> 
List of convictions for 
<%= session.getAttribute("poiname") %>
</p>

<hr/>

<%= session.getAttribute("conviction") %>

<hr/>

<%@ include file="Footer.jsp" %>