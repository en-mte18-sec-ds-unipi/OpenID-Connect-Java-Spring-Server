/*******************************************************************************
 * Copyright 2017 The MITRE Corporation
 *   and the MIT Internet Trust Consortium
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

package org.mitre.fidouaf.service;

import java.util.List;

import org.mitre.fidouaf.exception.FidoUafCreationException;
import org.mitre.fidouaf.model.FidoUaf;

/**
 * @author jricher
 *
 */
public interface FidoUafService {

	/**
	 * @param preferredUsername
	 * @return
	 */
	public FidoUaf createNewFidoOpt(String preferredUsername) throws FidoUafCreationException;

	/**
	 * @param preferredUsername
	 * @return
	 */
	public FidoUaf createFidoAuthID() throws FidoUafCreationException;

	/**
	 * @param fidoOtp
	 * @return
	 */
	public FidoUaf lookUpByFidoOtp(String fidoOtp);

	/**
	 * @return
	 */
	public List<FidoUaf> lookUpAllFidoUaf();


	/**
	 * @param preferredUsername
	 * @return
	 */
	public FidoUaf lookUpByPreferredUsername(String preferredUsername);

	/**
	 * @param dc
	 */
	public void approveFidoUafUser(FidoUaf dc,String aaid, String keyid);

	/**
	 * @param otp
	 * @param fidoUaf
	 * @return
	 */
	public FidoUaf findFidoOtp(String otp, FidoUaf fidoUaf);

	/**
	 * @param preferredUsername
	 * @param fidoUaf
	 * @return
	 */
	public FidoUaf findFidoPreferredUsername(String preferredUsername, FidoUaf fidoUaf);

	/**
	 * 
	 * @param otp
	 * @param fidoUaf
	 */
	public void clearDeviceCode(String otp, FidoUaf fidoUaf);

	/**
	 * 
	 * @param fidoUaf
	 */
	public void removeFidoUafModel(FidoUaf fidoUaf);

}