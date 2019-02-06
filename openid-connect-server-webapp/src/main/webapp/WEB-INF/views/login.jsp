<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="o" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<o:header title="Log In" />
<script type="text/javascript">

$(document).ready(function() {
	// select the appropriate field based on context
	$('#<c:out value="${ login_hint != null ? 'j_password' : 'j_username' }" />').focus();
});

</script>
<o:topbar />
<div class="container-fluid main">

	<h1><spring:message code="login.login_with_username_and_password"/></h1>

	<c:if test="${ param.error != null }">
		<div class="alert alert-error"><spring:message code="login.error"/></div>
	</c:if>


	<div class="row-fluid well">
		<div class="span3 offset1">
			<form action="${ config.issuer }${ config.issuer.endsWith('/') ? '' : '/' }j_spring_security_check" method="POST">
				<div>
					<div class="input-prepend input-block-level">
						<span class="add-on"><i class="icon-user"></i></span>
						<input type="text" placeholder="Username" autocorrect="off" autocapitalize="off" autocomplete="off" spellcheck="false" value="<c:out value="${ login_hint }" />" id="j_username" name="j_username">
					</div>
				</div>
				<div>
					<div class="input-prepend input-block-level">
						<span class="add-on"><i class="icon-lock"></i></span>
						<input type="password" placeholder="Password" autocorrect="off" autocapitalize="off" autocomplete="off" spellcheck="false" id="j_password" name="j_password">
					</div>
				</div>
				<div>
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
					<input type="submit" class="btn" value="Login" name="submit">
				</div>
			</form>
		</div>
		<div class="span5 pull-right">
			<div id="image"></div>
		</div>
	</div>
	<script type="text/javascript">
		var data = '{"baseUrl":"${ config.fidoUafServer }", "operation":"Auth", "authId": "${authId}"}';
		$("#image").append("<img src='http://chart.apis.google.com/chart?cht=qr&chl=" + encodeURIComponent(data) + "&chs=250x250&chld=H|4' alt='qr' />");
	</script>
</div>

<o:footer/>
