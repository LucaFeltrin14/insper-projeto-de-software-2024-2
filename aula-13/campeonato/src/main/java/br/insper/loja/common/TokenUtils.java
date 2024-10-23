package br.insper.loja.common;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import lombok.SneakyThrows;

public class TokenUtils {

    @SneakyThrows
    public static String getEmailFromToken(String token) {
        JWT jwt = JWTParser.parse(token.split(" ")[1]);
        return (String) jwt.getJWTClaimsSet().getClaim("email");
    }

    @SneakyThrows
    public static String getIss(String token) {
        JWT jwt = JWTParser.parse(token.split(" ")[1]);
        String iss = (String) jwt.getJWTClaimsSet().getClaim("iss");
        return iss.substring(iss.lastIndexOf('/') + 1);
    }

    @SneakyThrows
    public static String extractSignatures(String token) {
        String [] parts = token.split("\\.");
        return parts[2];
    }

    public static String removeBearerPrefix(String token) {
        if (token.contains("Bearer")) {
            return token.split(" ")[1];
        }
        return token;
    }
}
