<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp" %>

<h1>Search for Cases</h1>

<hr/>

<form method="get" action="Search">
<div>
	<input type="hidden" name="filter" value="description" />
	Search By Case Name:
	<input type="text" name="description" />
	<input type="submit" value="Search" title="Search by Description" />
</div>
</form>

<hr/>

<form method="get" action="Search">
<div>
	<input type="hidden" name="filter" value="poiname" />
	Search By Person Name:<br />(Convicts / Suspects)
	<input type="text" name="poiname" />
	<input type="submit" value="Search" title="Search by Person Name" />
</div>
</form>

<hr/>

<form method="get" action="Search">
<div>
	<input type="hidden" name="filter" value="category" />
	Search By Category:
	<select name="category">
	  <option>Koerperverletzung</option>
	  <option>Mord</option>
	  <option>Diebstahl</option>
	  <option>Einbruch</option>
	  <option>Bankraub</option>
	</select>
	<input type="submit" value="Search" title="Search by Category" />
</div>
</form>

<hr/>

<form method="get" action="Search">
<div>
	<input type="hidden" name="filter" value="status" />
	Search By Status:
	<select name="status">
	  <option>open</option>
	  <option>closed</option>
	</select>
	<input type="submit" value="Search" title="Search by Status" />
</div>
</form>

<hr/>
<%=  
	//TODO Display search results 
	session.getAttribute("results")
%>

<hr/>

<%@ include file="Footer.jsp" %>