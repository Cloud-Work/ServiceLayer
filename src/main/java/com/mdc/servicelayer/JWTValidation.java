package com.mdc.servicelayer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jose4j.jwk.HttpsJwks;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.resolvers.HttpsJwksVerificationKeyResolver;
import org.jose4j.lang.JoseException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;
import java.util.logging.Level;


@Component
public class JWTValidation {


    //protected final Log logger = LogFactory.getLog(getClass());
    Logger logger = java.util.logging.Logger.getLogger(JWTValidation.class.getName());

    public Boolean validateToken(String jwtToken) {

        Boolean result = false;

        //QA Token settings
        String expectedAudience = "CSS";
        String expectedIssuer = "Cargill CSS";

        if ((jwtToken == null)||(jwtToken.length() == 0)) {

            logger.warning("JWT token is null or invalid");
            return result;
        }


        String[] split_string = jwtToken.split("\\.");

        if (split_string.length != 3) {

            logger.warning("JWT token cannot be split into 3 parts, only has " + split_string.length + " parts");
            return result;
        }

        String base64EncodedHeader = split_string[0];
        String base64EncodedBody = split_string[1];
        String base64EncodedSignature = split_string[2];

        byte[] decodedBytes = Base64.getUrlDecoder().decode(base64EncodedBody);
        String decodedBody = new String(decodedBytes);

        try {

            JSONObject jsonObject = new JSONObject(decodedBody);
            String issuer = jsonObject.get("iss").toString();
            String audience = jsonObject.get("aud").toString();

            logger.info(jsonObject.toString());

            Date exp_time = new Date(jsonObject.getLong("exp") * 1000);
            Date issued_time = new Date(jsonObject.getLong("iat") * 1000);
            Date current_time = new Date(System.currentTimeMillis());

            logger.info("current: " +  current_time.toString());
            logger.info("issued at: " + issued_time.toString());
            logger.info("expiration: " + exp_time.toString());

            if (current_time.after(exp_time)) {
               logger.info("JWT token Expired");
               return false;

            } else {
                logger.info("JWT token not expired");
            }


            if (!expectedAudience.equals(audience) || !expectedIssuer.equals(issuer)) {

                logger.info("JWT token INVALID");

            } else {
                logger.info("JWT token VALID");
                result = true;
            }

        } catch (Exception e) {

            logger.warning("JWT error getting iss and aud");
            result = false;
        }

        return result;
    }


    public Boolean isTokenValid(String idToken) {

            return validateToken(idToken);

    }

    public String generateJWT(String username) {

        //
        // JSON Web Token is a compact URL-safe means of representing claims/attributes to be transferred between two parties.
        // This example demonstrates producing and consuming a signed JWT
        //

        // Generate an RSA key pair, which will be used for signing and verification of the JWT, wrapped in a JWK
        RsaJsonWebKey rsaJsonWebKey = null;
        try {
            rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
        } catch (JoseException e) {
            e.printStackTrace();
        }

        // Give the JWK a Key ID (kid), which is just the polite thing to do
        rsaJsonWebKey.setKeyId("k1");

        // Create the Claims, which will be the content of the JWT
        JwtClaims claims = new JwtClaims();
        claims.setIssuer("Cargill CSS");  // who creates the token and signs it
        claims.setAudience("CSS"); // to whom the token is intended to be sent
        claims.setExpirationTimeMinutesInTheFuture(60); // time when the token will expire
        claims.setGeneratedJwtId(); // a unique identifier for the token
        claims.setIssuedAtToNow();  // when the token was issued/created (now)
        claims.setNotBeforeMinutesInThePast(5); // time before which the token is not yet valid (2 minutes ago)
        claims.setSubject("CSS"); // the subject/principal is whom the token is about
        claims.setClaim("email","css@cargill.com"); // additional claims/attributes about the subject can be added
        claims.setClaim("username",username); // additional claims/attributes about the subject can be added

        //List<String> groups = Arrays.asList("group-one", "other-group", "group-three");
        //claims.setStringListClaim("groups", groups); // multi-valued claims work too and will end up as a JSON array

        // A JWT is a JWS and/or a JWE with JSON claims as the payload.
        // In this example it is a JWS so we create a JsonWebSignature object.
        JsonWebSignature jws = new JsonWebSignature();

        // The payload of the JWS is JSON content of the JWT Claims
        jws.setPayload(claims.toJson());

        // The JWT is signed using the private key
        jws.setKey(rsaJsonWebKey.getPrivateKey());

        // Set the Key ID (kid) header because it's just the polite thing to do.
        // We only have one key in this example but a using a Key ID helps
        // facilitate a smooth key rollover process
        jws.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());

        // Set the signature algorithm on the JWT/JWS that will integrity protect the claims
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

        // Sign the JWS and produce the compact serialization or the complete JWT/JWS
        // representation, which is a string consisting of three dot ('.') separated
        // base64url-encoded parts in the form Header.Payload.Signature
        // If you wanted to encrypt it, you can simply set this jwt as the payload
        // of a JsonWebEncryption object and set the cty (Content Type) header to "jwt".
        String jwt = null;
        try {
            jwt = jws.getCompactSerialization();
        } catch (JoseException e) {
            e.printStackTrace();
        }

        return jwt;
    }

    public String getUsernameFromToken(String jwtToken) {

        Boolean result = false;
        String username = "";

        if ((jwtToken == null)||(jwtToken.length() == 0)) {

            logger.warning("JWT token is null or invalid");
            return username;
        }

        String[] split_string = jwtToken.split("\\.");

        if (split_string.length != 3) {

            logger.warning("JWT token cannot be split into 3 parts, only has " + split_string.length + " parts");
            return username;
        }

        String base64EncodedHeader = split_string[0];
        String base64EncodedBody = split_string[1];
        String base64EncodedSignature = split_string[2];

        byte[] decodedBytes = Base64.getUrlDecoder().decode(base64EncodedBody);
        String decodedBody = new String(decodedBytes);

        try {

            JSONObject jsonObject = new JSONObject(decodedBody);
            username = jsonObject.get("username").toString();

            logger.info("username in jwt token: " + username);


        } catch (Exception e) {

            logger.warning("JWT error getting iss and aud");
            result = false;
        }

        return username;
    }

}
