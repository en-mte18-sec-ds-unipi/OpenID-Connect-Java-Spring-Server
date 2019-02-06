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

package org.mitre.fidouaf.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;

import org.mitre.oauth2.model.AuthenticationHolderEntity;

/**
 * @author jricher
 *
 */
@Entity
@Table(name = "fido_uaf")
@NamedQueries({
	@NamedQuery(name = FidoUaf.QUERY_ALL, query = "SELECT d FROM FidoUaf d"),
	@NamedQuery(name = FidoUaf.QUERY_BY_FIDO_OTP, query = "select d from FidoUaf d where d.fidoOtp = :" + FidoUaf.PARAM_FIDO_OTP),
	@NamedQuery(name = FidoUaf.QUERY_BY_PREFERRED_USERNAME, query = "select d from FidoUaf d where d.fidoOtp = :" + FidoUaf.PARAM_PREFERRED_USERNAME),
	@NamedQuery(name = FidoUaf.QUERY_EXPIRED_BY_DATE, query = "select d from FidoUaf d where d.expiration <= :" + FidoUaf.PARAM_DATE)
})
public class FidoUaf {

	public static final String QUERY_ALL = "FidoUaf.findAll";
	public static final String QUERY_BY_FIDO_OTP = "FidoUaf.queryByFidoOtp";
	public static final String QUERY_BY_PREFERRED_USERNAME = "FidoUaf.queryByPreferredUsername";
	public static final String QUERY_EXPIRED_BY_DATE = "FidoUaf.queryExpiredByDate";
	
	public static final String PARAM_FIDO_OTP = "fidoOtp";
	public static final String PARAM_PREFERRED_USERNAME = "preferredUsername";
	public static final String PARAM_DATE = "date";

	private Long id;
	private String fidoOtp;
	private Date expiration;
	private String preferredUsername;
	private String aaid;
	private String keyid;
	private boolean approved= false;
	private AuthenticationHolderEntity authenticationHolder;
	
	public FidoUaf() {
		
	}
	
	public FidoUaf(String fidoOtp, String preferredUsername) {
		this.fidoOtp = fidoOtp;
		this.preferredUsername = preferredUsername;
	}

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the fidoOtp
	 */
	@Basic
	@Column(name = "fido_otp")
	public String getFidoOtp() {
		return fidoOtp;
	}

	/**
	 * @param fidoOtp the fidoOtp to set
	 */
	public void setFidoOtp(String fidoOtp) {
		this.fidoOtp = fidoOtp;
	}

	@Basic
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	@Column(name = "expiration")
	public Date getExpiration() {
		return expiration;
	}

	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}

	/**
	 * @return the preferredUsername
	 */
	@Basic
	@Column(name = "preferred_username")
	public String getPreferredUsername() {
		return preferredUsername;
	}

	/**
	 * @param preferredUsername the preferredUsername to set
	 */
	public void setPreferredUsername(String preferredUsername) {
		this.preferredUsername = preferredUsername;
	}
	
	/**
	 * @return the aaid
	 */
	@Basic
	@Column(name = "aaid")
	public String getAaid() {
		return aaid;
	}

	/**
	 * @param aaid the aaid to set
	 */
	public void setAaid(String aaid) {
		this.aaid = aaid;
	}


	/**
	 * @return the keyid
	 */
	@Basic
	@Column(name = "keyid")
	public String getKeyid() {
		return keyid;
	}

	/**
	 * @param keyid the keyid to set
	 */
	public void setKeyid(String keyid) {
		this.keyid = keyid;
	}

	/**
	 * @return the approved
	 */
	@Basic
	@Column(name = "approved")
	public boolean isApproved() {
		return approved;
	}

	/**
	 * @param approved the approved to set
	 */
	public void setApproved(boolean approved) {
		this.approved = approved;
	}
	
	/**
	 * The authentication in place when this token was created.
	 * @return the authentication
	 */
	@ManyToOne
	@JoinColumn(name = "auth_holder_id")
	public AuthenticationHolderEntity getAuthenticationHolder() {
		return authenticationHolder;
	}

	/**
	 * @param authentication the authentication to set
	 */
	public void setAuthenticationHolder(AuthenticationHolderEntity authenticationHolder) {
		this.authenticationHolder = authenticationHolder;
	}

	
}