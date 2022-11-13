package com.idempotent.token.service.util;

public interface TokenUtilService {

    String genToken(String value);

    Boolean validToken(String token, String value);
}
