<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp" %>

<h1>Persons of Interest</h1>

<hr/>

<%= session.getAttribute("poI") %>

<hr/>

<%@ include file="Footer.jsp" %>