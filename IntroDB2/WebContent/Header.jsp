<%@page import="ch.ethz.inf.dbproject.model.DatastoreInterfaceSimpleDatabase"%>
<%@page import="ch.ethz.inf.dbproject.UserServlet"%>
<%@page import="ch.ethz.inf.dbproject.model.Crime"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
	
	<head>
	    <link href="style.css" rel="stylesheet" type="text/css">
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Law Enforcement Project</title>
	</head>

	<body>

		<!-- Header -->
		<% 
		Boolean loggedIn = (Boolean) session.getAttribute(UserServlet.SESSION_USER_LOGGED_IN);
		if (loggedIn != null && loggedIn) {
			// User is logged in. Display logout:
		%>
			<div id="logout"><a href="Logout" >Logout</a></div>
		<% } %>			
		
		<table id="masterTable" cellpadding="0" cellspacing="0">
			<tr>
				<th id="masterHeader" colspan="2">
					<h1>Law Enforcement Project</h1>
					Project by Christian, Lorin &amp; Nina
				</th>
			</tr>
			<tr id="masterContent">
			
				<td id="masterContentMenu">
					
					<div class="menuDiv1"></div>
					<div class="menuDiv1"><a href="Home">Home</a></div>
					<div class="menuDiv1"><a href="Cases">All cases</a></div>
					<div class="menuDiv2"><a href="Cases?filter=open">Open</a></div>
					<div class="menuDiv2"><a href="Cases?filter=closed">Closed</a></div>
					<div class="menuDiv2"><a href="Cases?filter=recent">Recent</a></div>
					<div class="menuDiv2"><a href="Cases?filter=oldest">Oldest Unsolved</a></div>
					<div class="menuDiv1">Categories</div>
					<%
					DatastoreInterfaceSimpleDatabase dbInterface = new DatastoreInterfaceSimpleDatabase();
					for (Crime crime : dbInterface.getAll(Crime.class)) {
						%><div class="menuDiv2"><a href="Cases?crimeId=<%=crime.getId()%>"><%=crime.getName() %>
						</a></div>
					<%
					}
					%>

					<!-- 
					<div class="menuDiv3"><a href="Cases?category=assault">Assault</a></div>
					<div class="menuDiv3"><a href="Cases?category=murder">Murder</a></div>
					<div class="menuDiv2"><a href="Cases?category=property">Property Crimes</a></div>
					<div class="menuDiv3"><a href="Cases?category=theft">Theft</a></div>
					<div class="menuDiv3"><a href="Cases?category=burglary">Burglary</a></div>
					<div class="menuDiv3"><a href="Cases?category=robbery">Bank robbery</a></div>
					<div class="menuDiv2"><a href="Cases?category=other">Other</a></div>
					 -->
					<div class="menuDiv1"><a href="PoI">Persons of Interest</a></div>
					<div class="menuDiv1"><a href="Search">Search</a></div>
					<div class="menuDiv1"><a href="User">User Profile</a></div>
					
				</td>
				
				<td id="masterContentPlaceholder">
				
		