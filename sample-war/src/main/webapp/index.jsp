<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%><%@page
	import="com.farmafene.cas.integration.sample.Invoker"%>

<h2>Hola!</h2>
<%
	Invoker i = new Invoker();
	i.callService();
%>
<p>Nombre:<%=i.name%></p>
<p>Token:<%=i.proxyTicket%></p>
<p>uuidReq:<%=i.uuidReq%></p>
<p>uuidRes:<%=i.uuidRes%></p>
