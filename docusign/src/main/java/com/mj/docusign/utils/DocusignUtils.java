package com.mj.docusign.utils;

import java.util.Arrays;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.mj.docusign.models.AccessTokenRequest;
import com.mj.docusign.models.AccessTokenResponse;

@Component
public class DocusignUtils {

	private static final Logger LOG = LoggerFactory.getLogger(DocusignUtils.class);

	// client id or integgrator id
	private final String CLIENT_ID = "3912093e-318a-42c6-83dc-cb336a159643";
	private final String CLINET_SECRET = "1d159207-1ebd-4329-8641-8ff3ce0d1531";
	private final String TEMPLATE_ID = "3789be57-8a6e-4fe8-97d0-dbc09dac7dd0";
	String AuthServerUrl = "https://account-d.docusign.com";
	String RestApiUrl = "https://demo.docusign.net/restapi";
	private final String DOCUSIGN_GET_AUTHCODE = "https://account-d.docusign.com/oauth/auth";
	private final String DOCUSIGN_GET_ACCESSTOKEN = "https://account-d.docusign.com/oauth/token";
	private final String SCOPE = "signature";
	private final String NGROK_URL = "https://8d270c44.ngrok.io";
	private final String CALLBCK_URL = NGROK_URL + "/api/v1/docusign/callback";
	private final String BASIC_AUTH = "Basic";

	public void getAuthorizationCode() {
		/*
		 * 
		 * ?response_type=code&scope=signature&
		 * client_id=7c2b8d7e-83c3-4940-af5e-cda8a50dd73f&state=a39fh23hnf23&
		 * redirect_uri=http://example.com/callback/
		 */
		LOG.info("BEFORE SENDING REQUEST FOR GETTING AUTHORIZATION CODE..");
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(DOCUSIGN_GET_AUTHCODE)
				.queryParam("response_type", "code").queryParam("scope", "signature").queryParam("client_id", CLIENT_ID)
				.queryParam("redirect_uri", CALLBCK_URL);
		HttpEntity<?> entity = new HttpEntity<>(headers);
		HttpEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
				String.class);
		LOG.info("REQUEST SENT IS.." + builder.toUriString());
		LOG.info("AFTER SENDING REQUEST FOR GETTING AUTHORIZATION CODE..");

	}

	public AccessTokenResponse getAccessToken(String authCode) {
		LOG.info("BEFORE SENDING REQUEST FOR GETTING AUTHORIZATION CODE..");
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		String combo = CLIENT_ID + ":" + CLINET_SECRET;
		String encodeBase64 = new String(Base64.encodeBase64(combo.getBytes()));
		headers.add("Authorization", BASIC_AUTH + encodeBase64);
		 headers.add("Content-Type","application/x-www-form-URIencoded");
		 FormHttpMessageConverter converter = new FormHttpMessageConverter();
		 restTemplate.setMessageConverters(Arrays.asList(converter));
		AccessTokenRequest request = new AccessTokenRequest();
		request.setCode(authCode);
		request.setGrant_type("authorization_code");
		request.setRedirect_uri("");
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("code", authCode);
		map.add("grant_type", "authorization_code");
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		HttpEntity<AccessTokenResponse> response = restTemplate.postForEntity(DOCUSIGN_GET_ACCESSTOKEN, entity,
				AccessTokenResponse.class);
		LOG.info("AFTER SENDING REQUEST FOR GETTING AUTHORIZATION CODE..");
		return response.getBody();
	}
}
