package com.mj.docusign.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mj.docusign.models.AccessTokenResponse;
import com.mj.docusign.models.AuthorizationCode;
import com.mj.docusign.utils.DocusignUtils;

@RestController
@RequestMapping("/api/v1/docusign")
public class DocusignActivityController {
	
	
	private static final Logger LOG = LoggerFactory.getLogger(DocusignActivityController.class);
	@Autowired
	private transient DocusignUtils docusignUtils;

	@GetMapping("/request/auth-code")
	public void requestAuthorizarionCode(){
		LOG.info("api for getting authorization code called");
		docusignUtils.getAuthorizationCode();
	}
	
	
	@GetMapping("/callback")
	public AccessTokenResponse getAccessToken(@RequestParam("code")String authCode){
		LOG.info("received authorizaton code.."+authCode);
		AccessTokenResponse response = docusignUtils.getAccessToken(authCode);
		return response;
	}
	
	
	
}
