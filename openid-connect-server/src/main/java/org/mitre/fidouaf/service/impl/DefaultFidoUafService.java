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

package org.mitre.fidouaf.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.mitre.openid.connect.config.ConfigurationPropertiesBean;
import org.mitre.fidouaf.model.FidoUaf;
import org.mitre.fidouaf.repository.impl.FidoUafRepository;
import org.mitre.fidouaf.service.FidoUafService;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.oauth2.common.util.RandomValueStringGenerator;
import org.springframework.stereotype.Service;

/**
 * @author jricher
 *
 */
@Service("defaultFidoUafService")
public class DefaultFidoUafService implements FidoUafService {

	@Autowired
	private FidoUafRepository repository;

	@Autowired
	private ConfigurationPropertiesBean config;

	// private RandomValueStringGenerator randomGenerator = new RandomValueStringGenerator();

	/* (non-Javadoc)
	 * @see org.mitre.oauth2.service.FidoUafService#save(org.mitre.oauth2.model.FidoUaf)
	 */
	@Override
	public FidoUaf createNewFidoOpt(String preferredUsername) {

		// create a device code, should be big and random
		String fidoOtp = UUID.randomUUID().toString();

		// create a fido otp, should be random but small and typable
		// String fidoOtp = randomGenerator.generate();

		FidoUaf dc = new FidoUaf(fidoOtp, preferredUsername);

		dc.setExpiration(new Date(System.currentTimeMillis() + config.getFidoExpiration() * 1000L));

		dc.setApproved(false);

		return repository.save(dc);
	}

	/* (non-Javadoc)
	 * @see org.mitre.oauth2.service.FidoUafService#save(org.mitre.oauth2.model.FidoUaf)
	 */
	@Override
	public FidoUaf createFidoAuthID() {

		// create a device code, should be big and random
		String fidoOtp = UUID.randomUUID().toString();

		// create a fido otp, should be random but small and typable
		// String fidoOtp = randomGenerator.generate();

		FidoUaf dc = new FidoUaf();

		dc.setFidoOtp(fidoOtp);

		dc.setExpiration(new Date(System.currentTimeMillis() + config.getFidoExpiration() * 1000L));

		return repository.save(dc);
	}

	/* (non-Javadoc)
	 * @see org.mitre.oauth2.service.FidoUafService#lookUpByFidoOtp(java.lang.String)
	 */
	@Override
	public FidoUaf lookUpByFidoOtp(String fidoOtp) {
		return repository.getByFidoOtp(fidoOtp);
	}

	/* (non-Javadoc)
	 * @see org.mitre.oauth2.service.FidoUafService#lookUpAllFidoUaf(java.lang.String)
	 */
	@Override
	public List<FidoUaf> lookUpAllFidoUaf() {
		return repository.getAllFidoUaf();
	}

	/* (non-Javadoc)
	 * @see org.mitre.oauth2.service.FidoUafService#lookUpByPreferredUsername(java.lang.String)
	 */
	@Override
	public FidoUaf lookUpByPreferredUsername(String preferredUsername) {
		return repository.getByPreferredUsername(preferredUsername);
	}

	/* (non-Javadoc)
	 * @see org.mitre.oauth2.service.FidoUafService#approveFidoUafUser(org.mitre.oauth2.model.FidoUaf)
	 */
	@Override
	public void approveFidoUafUser(FidoUaf dc,String aaid, String keyid) {
		FidoUaf found = repository.getById(dc.getId());

		found.setApproved(true);
		found.setAaid(aaid);
		found.setKeyid(keyid);

		repository.save(found);
	}

	/* (non-Javadoc)
	 * @see org.mitre.oauth2.service.FidoUafService#findFidoPreferredUsername()
	 */
	@Override
	public FidoUaf findFidoPreferredUsername(String preferredUsername, FidoUaf fidoUaf) {
		FidoUaf found = repository.getByPreferredUsername(preferredUsername);

		if (found != null) {
			if (found.getFidoOtp().equals(fidoUaf.getFidoOtp())) {
				// make sure the client matches, if so, we're good
				return found;
			} else {
				// if the clients don't match, pretend the code wasn't found
				return null;
			}
		} else {
			// didn't find the code, return null
			return null;
		}

	}

	/* (non-Javadoc)
	 * @see org.mitre.oauth2.service.FidoUafService#findFidoOtp()
	 */
	@Override
	public FidoUaf findFidoOtp(String otp, FidoUaf fidoUaf) {
		FidoUaf found = repository.getByFidoOtp(otp);

		if (found != null) {
			if (found.getFidoOtp().equals(fidoUaf.getFidoOtp())) {
				// make sure the client matches, if so, we're good
				return found;
			} else {
				// if the clients don't match, pretend the code wasn't found
				return null;
			}
		} else {
			// didn't find the code, return null
			return null;
		}

	}

	/* (non-Javadoc)
	 * @see org.mitre.oauth2.service.FidoUafService#clearDeviceCode()
	 */
	@Override
	public void clearDeviceCode(String otp, FidoUaf fidoUaf) {
		FidoUaf found = findFidoOtp(otp, fidoUaf);
		
		if (found != null) {
			// make sure it's not used twice
			repository.remove(found);
		}

	}
	/* (non-Javadoc)
	 * @see org.mitre.oauth2.service.FidoUafService#removeFidoUafModel()
	 */
	@Override
	public void removeFidoUafModel(FidoUaf fidoUaf) {
		repository.remove(fidoUaf);
	}

}