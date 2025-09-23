package io.github.filipolszewski.communication.login;

import io.github.filipolszewski.communication.*;
import io.github.filipolszewski.server.ClientHandler;
import lombok.extern.java.Log;

@Log
public class LoginResponseHandler implements ResponseHandler {
    @SuppressWarnings("unchecked")
    @Override
    public void handle(Response<? extends Payload> response) {
        Response<LoginPayload> res = (Response<LoginPayload>) response;
        log.info(res.message());
        var payload = res.payload();
        if(payload != null) log.info(res.payload().userID());
    }
}
