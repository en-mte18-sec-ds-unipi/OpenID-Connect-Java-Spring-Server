<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="org.springframework.security.core.AuthenticationException"%>
<%@ page import="org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException"%>
<%@ page import="org.springframework.security.web.WebAttributes"%>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="o" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:message code="fido_uaf.request_code.title" var="title"/>
<o:header title="${title}"/>
<o:topbar pageName="Approve" />
<div class="container main">

	<div class="well" style="text-align: center">

		<c:if test="${ approved != null }">
			<c:choose>
				<c:when test="${ approved == false }">
					<h1><spring:message code="fido_uaf.request_code.header.register"/>&nbsp;</h1>
				</c:when>
				<c:otherwise>
					<h1><spring:message code="fido_uaf.request_code.header.deregister"/>&nbsp;</h1>
				</c:otherwise>
			</c:choose>				
		</c:if>

	<c:if test="${ error != null }">
		<div class="alert alert-error"><spring:message code="fido_uaf.error.error"/></div>	
	</c:if>
	<c:if test="${ approved != null }">
		<c:choose>
			<c:when test="${ approved == false }">
				<script type="text/javascript">
					var data = '{"baseUrl":"${ config.fidoUafServer }","otp":"${ fido_otp }","operation":"Reg","username":"${ fido_username }"}';
				</script>
			</c:when>
			<c:otherwise>
				<script type="text/javascript">
					var data = '{"baseUrl":"${ config.fidoUafServer }","operation":"Dereg","username":"${ fido_username }"}';
				</script>
			</c:otherwise>
		</c:choose>				
	</c:if>


		<div>
			<div id="image"></div>
		</div>

	</div>
	<script type="text/javascript">
		$("#image").append("<img src='http://chart.apis.google.com/chart?cht=qr&chl=" + encodeURIComponent(data) + "&chs=250x250&chld=H|4' alt='qr' />");
	</script>
</div>
<o:footer/>