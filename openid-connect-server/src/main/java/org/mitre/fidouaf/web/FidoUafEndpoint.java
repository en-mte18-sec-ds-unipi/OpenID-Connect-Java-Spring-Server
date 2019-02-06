/*******************************************************************************
 * Copyright 2018 The MIT Internet Trust Consortium
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package org.mitre.fidouaf.web;

import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.mitre.fidouaf.exception.FidoUafCreationException;
import org.mitre.fidouaf.model.FidoUaf;
import org.mitre.fidouaf.model.FidoUafNotification;
import org.mitre.fidouaf.service.FidoUafService;
import org.mitre.openid.connect.model.UserInfo;
import org.mitre.openid.connect.service.UserInfoService;
import org.mitre.openid.connect.view.HttpCodeView;
import org.mitre.openid.connect.view.JsonErrorView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.google.gson.Gson;

@Controller
public class FidoUafEndpoint {

	public static final String URL = "fido_uaf";
	
	public static final String FINALIZE_URL = "finalize";

	public static final Logger logger = LoggerFactory.getLogger(FidoUafEndpoint.class);

	public static final String ENTITY = "entity";

	public static final String VIEWNAME = "jsonEntityView";

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private FidoUafService fidoUafService;

	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value = "/" + URL, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String requestOtpCode(@RequestParam(value = "username", required = false) String prefUsername, ModelMap model, Authentication auth) {

		if (auth == null) {
			logger.error("getInfo failed; no principal. Requester is not authorized.");
			model.addAttribute(HttpCodeView.CODE, HttpStatus.FORBIDDEN);
			return HttpCodeView.VIEWNAME;
		}

		String username = auth.getName();
		UserInfo user = userInfoService.getByUsername(username);

		if (user == null) {
			logger.error("getInfo failed; user not found: " + prefUsername);
			model.addAttribute(HttpCodeView.CODE, HttpStatus.NOT_FOUND);
			return HttpCodeView.VIEWNAME;
		}

		// FidoUaf dc = fidoUafService.createNewFidoOpt(user.getPreferredUsername());

		// Map<String, Object> response = new HashMap<>();
		// response.put("username", username);
		// response.put("fido_otp", dc.getFidoOtp());
		// response.put("fidoOperation", "Reg");

		// model.addAttribute(JsonEntityView.ENTITY, response);
		// return JsonEntityView.VIEWNAME;

		FidoUaf fidoModel = findFidoUafModelByUsername(user.getPreferredUsername());

		if (fidoModel == null) {
			try {
				fidoModel = fidoUafService.createNewFidoOpt(user.getPreferredUsername());

				model.addAttribute("fido_username", fidoModel.getPreferredUsername());
				model.addAttribute("fido_otp", fidoModel.getFidoOtp());
				model.addAttribute("expires_in", fidoModel.getExpiration());
				model.addAttribute("approved", fidoModel.isApproved());
				
				return "requestFidoOtp";

			} catch (FidoUafCreationException fuce) {
				
				model.put(HttpCodeView.CODE, HttpStatus.BAD_REQUEST);
				model.put(JsonErrorView.ERROR, fuce.getError());
				model.put(JsonErrorView.ERROR_MESSAGE, fuce.getMessage());
				
				return JsonErrorView.VIEWNAME;
			}
		} else {
			model.addAttribute("fido_username", fidoModel.getPreferredUsername());
			model.addAttribute("fido_otp", fidoModel.getFidoOtp());
			model.addAttribute("expires_in", fidoModel.getExpiration());
			model.addAttribute("approved", fidoModel.isApproved());
			if (fidoModel.getKeyid() != null) {
				model.addAttribute("keyid", fidoModel.getKeyid());
			}

			return "requestFidoOtp";
		}
		
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String requestAuthId(ModelMap model, Authentication auth) {

		FidoUaf fidoModel;
		try {
			fidoModel = fidoUafService.createNewFidoOpt("AuthID");

			model.addAttribute("authId", fidoModel.getFidoOtp());
			model.addAttribute("expires_in", fidoModel.getExpiration());
			
			return "login";

		} catch (FidoUafCreationException fuce) {
			
			model.put(HttpCodeView.CODE, HttpStatus.BAD_REQUEST);
			model.put(JsonErrorView.ERROR, fuce.getError());
			model.put(JsonErrorView.ERROR_MESSAGE, fuce.getMessage());
			
			return JsonErrorView.VIEWNAME;
		}
		
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value = "/" + URL + "/approved", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String isFidoOtpApproved(@RequestParam(value = "username", required = false) String prefUsername, ModelMap model, Authentication auth) {
		
		if (auth == null) {
			logger.error("isFidoOtpApproved failed; no principal. Requester is not authorized.");
			model.addAttribute(HttpCodeView.CODE, HttpStatus.FORBIDDEN);
			return HttpCodeView.VIEWNAME;
		}
		
		String username = auth.getName();
		UserInfo user = userInfoService.getByUsername(username);
		
		if (user == null) {
			logger.error("isFidoOtpApproved failed; user not found: " + prefUsername);
			model.addAttribute(HttpCodeView.CODE, HttpStatus.NOT_FOUND);
			return HttpCodeView.VIEWNAME;
		}
		
		logger.warn("NIKOSEV: FidoUafEndpoint: isFidoOtpApproved: username: " + user.getPreferredUsername());
		
		FidoUaf fidoUafModel = findFidoUafModelByUsername(user.getPreferredUsername());
		
		boolean approved;
		

		if (fidoUafModel == null) {
			logger.error("isFidoOtpApproved failed; FidoUaf not found: " + fidoUafModel);
			model.addAttribute(HttpCodeView.CODE, HttpStatus.NOT_FOUND);
			return HttpCodeView.VIEWNAME;
		}

		if (fidoUafModel.isApproved()) {
			approved = true;
		} else {
			approved = false;
		}
		
		model.addAttribute("approved", approved);

		return "requestFidoOtp";
	}

	@RequestMapping(value = "/" + URL + "/finalize", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String finalizeFlow (@RequestBody String jsonString, ModelMap model, Authentication auth, HttpServletRequest request) {
		logger.warn("NIKOSEV: FidoUafEndpoint: finalizeFLow: data: " + jsonString);

		Gson gson = new Gson();
		FidoUafNotification mitreNotification = gson.fromJson(jsonString, FidoUafNotification.class);

		String authId= mitreNotification.getAuthId();
		String fidoOtp= mitreNotification.getFidoOtp();
		String username= mitreNotification.getUsername();
		String aaid= mitreNotification.getAaid();
		String keyid= mitreNotification.getKeyId();
		String fidoOp= mitreNotification.getFidoOperation();
		logger.warn("NIKOSEV: FidoUafEndpoint: finalizeFlow: authId: " + authId);
		logger.warn("NIKOSEV: FidoUafEndpoint: finalizeFlow: fidoOtp: " + fidoOtp);
		logger.warn("NIKOSEV: FidoUafEndpoint: finalizeFlow: username: " + username);
		logger.warn("NIKOSEV: FidoUafEndpoint: finalizeFlow: aaid: " + aaid);
		logger.warn("NIKOSEV: FidoUafEndpoint: finalizeFlow: keyId: " + keyid);
		logger.warn("NIKOSEV: FidoUafEndpoint: finalizeFlow: fidoOperation: " + fidoOp);

		FidoUaf fidoUafModel = findFidoUafModelByUsername(username);

		logger.warn("NIKOSEV: FidoUafEndpoint: finalizeFlow: username: " + fidoUafModel.getPreferredUsername());
		
		if (fidoUafModel != null) {
			logger.warn("NIKOSEV: FidoUafEndpoint: finalizeFlow: fidoUafModel != null");
			// make sure the form that was submitted is the one that we were expecting
			if (!fidoUafModel.getFidoOtp().equals(fidoOtp)) {
				model.addAttribute("error", "userCodeMismatch");
				return "requestUserCode";
			}

			// make sure the code hasn't expired yet
			if (fidoUafModel.getExpiration() != null && fidoUafModel.getExpiration().before(new Date())) {
				model.addAttribute("error", "expiredUserCode");
				return "requestUserCode";
			}

			if (fidoOp.equals("Reg")) {
				logger.warn("NIKOSEV: FidoUafEndpoint: finalizeFlow: fidoOp == Reg");
				fidoUafService.approveFidoUafUser(fidoUafModel, aaid, keyid);
				logger.warn("NIKOSEV: FidoUafEndpoint: finalizeFlow: isApproved " + fidoUafModel.isApproved());
				model.addAttribute("approved", true);

			} else if (fidoOp.equals("Auth")) {
				logger.warn("NIKOSEV: FidoUafEndpoint: finalizeFlow: fidoOp == Auth");
				FidoUaf authFidoUafModel = findFidoUafModelByAuthId(authId);
				if (!authFidoUafModel.getFidoOtp().equals(authId)) {
					model.addAttribute("error", "userCodeMismatch");
					return "requestUserCode";
				}
				FidoUaf userFidoUafModel = findFidoUafModelByKeyId(keyid);
				if (!userFidoUafModel.getKeyid().equals(keyid)) {
					model.addAttribute("error", "userCodeMismatch");
					return "requestUserCode";
				}

				UserInfo user = userInfoService.getByUsername(userFidoUafModel.getPreferredUsername());

				Boolean result = authenticateUserAndInitializeSessionByUsername(user, request);

				model.addAttribute("username", userFidoUafModel.getPreferredUsername());

				return "home";

			} else if (fidoOp.equals("Dereg")) {
				logger.warn("NIKOSEV: FidoUafEndpoint: finalizeFlow: fidoOp == Dereg");
				fidoUafService.removeFidoUafModel(fidoUafModel);
				model.addAttribute("approved", true);

			}
		} else {
			model.addAttribute("error", "userCodeMismatch");
		}

		return "requestFidoOtp";
	}

	private FidoUaf findFidoUafModelByUsername(String preferredUsername) {
		Collection<FidoUaf> dcss = fidoUafService.lookUpAllFidoUaf();
		FidoUaf found = null;
		for (FidoUaf dc : dcss) {
			if (dc.getPreferredUsername().equals(preferredUsername)) {
				return dc;
			}
		}
		return found;
	}

	private FidoUaf findFidoUafModelByAuthId(String authId) {
		Collection<FidoUaf> dcss = fidoUafService.lookUpAllFidoUaf();
		FidoUaf found = null;
		for (FidoUaf dc : dcss) {
			if (dc.getFidoOtp().equals(authId)) {
				return dc;
			}
		}
		return found;
	}

	private FidoUaf findFidoUafModelByKeyId(String keyId) {
		Collection<FidoUaf> dcss = fidoUafService.lookUpAllFidoUaf();
		FidoUaf found = null;
		for (FidoUaf dc : dcss) {
			if (dc.getKeyid().equals(keyId)) {
				return dc;
			}
		}
		return found;
	}

	public boolean authenticateUserAndInitializeSessionByUsername(UserInfo userDetails, HttpServletRequest request) {
		boolean result = true;

		try
		{
			// generate session if one doesn't exist
			request.getSession();

			//UserInfo user = userInfoService.getByUsername(username);
			Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null);
			SecurityContextHolder.getContext().setAuthentication(auth);
			logger.warn("NIKOSEV: FidoUafEndpoint: authenticateUserAndInitializeSessionByUsername: user: " + auth.isAuthenticated());

		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			logger.warn("NIKOSEV: FidoUafEndpoint: authenticateUserAndInitializeSessionByUsername: user: " + e.getMessage());

			result = false;
		}

		return result;
	}
}