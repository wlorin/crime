<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp" %>

<h1>Convicted PoI</h1>

<hr/>

<%= session.getAttribute("convicts") %>

<hr/>
<h1>Suspected PoI</h1>

<hr/>

<%= session.getAttribute("suspect") %>

<hr/>
<h1>Persons of Interest</h1>
<hr />
<%= session.getAttribute("poisnotlinkted") %>

<%@ include file="Footer.jsp" %>