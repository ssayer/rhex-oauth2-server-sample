package org.mitre.rhex.jwt;

import java.security.interfaces.RSAPublicKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nimbusds.jwt.Base64URL;
import com.nimbusds.jwt.JWK.Use;
import com.nimbusds.jwt.RSAKey;

@Controller
@RequestMapping("/key")
public class KeyEndpoint {
	
	@Autowired
	RSAPublicKey key;
	
	@RequestMapping(method=RequestMethod.GET, value="/jwk")
	@ResponseBody
	public String jwk() {
		byte[] mod = key.getModulus().toByteArray();
		byte[] exp = key.getPublicExponent().toByteArray();
		
		RSAKey jwk = new RSAKey(Base64URL.encode(mod), Base64URL.encode(exp), Use.ENCRYPTION, "rhex-enc-oauth2");
		return jwk.toJSONObject().toJSONString();
	}

}
