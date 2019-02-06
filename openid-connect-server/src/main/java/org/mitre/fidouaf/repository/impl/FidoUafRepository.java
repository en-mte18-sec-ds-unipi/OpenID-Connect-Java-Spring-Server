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

package org.mitre.fidouaf.repository.impl;

import java.util.Collection;
import java.util.List;

import org.mitre.fidouaf.model.FidoUaf;

/**
 * @author jricher
 *
 */
public interface FidoUafRepository {

	/**
	 * @param id
	 * @return
	 */
	public FidoUaf getById(Long id);

	/**
	 * @param scope
	 */
	public void remove(FidoUaf scope);

	/**
	 * @param scope
	 * @return
	 */
	public FidoUaf save(FidoUaf scope);

	/**
	 * @param fidoOtp
	 * @return
	 */
	public FidoUaf getByFidoOtp(String fidoOtp);
	

	/**
	 * @param preferredUsername
	 * @return
	 */
	public FidoUaf getByPreferredUsername(String preferredUsername);

	/**
	 * @return
	 */
	public List<FidoUaf> getAllFidoUaf();

	/**
	 * @return
	 */
	public Collection<FidoUaf> getExpiredCodes();

}