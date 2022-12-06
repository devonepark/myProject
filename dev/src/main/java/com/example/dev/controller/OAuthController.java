package com.example.dev.controller;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dev.vo.OauthToken;

import kong.unirest.Unirest;

// - 지금 이 부분을 oauth 서버에서 구현 했지만 사실 사용할 클라이언트가 개발해야하는 코드다.

// - 예를들어 페이스북 개발자 센터에 clientId, secretKey를 발급 받고 인증 코드를 발급 받을 redirecturi 를 등록하게 되는데 '/callback' URL이 등록한 redirecturi 페이지이며, 페이스북으로 부터 코드를 받아 토큰을 발급받는 코드를 작성하는 것이라고 보면 된다.

@RestController
public class OAuthController {

    // 클라이언트가 구현해야하는 코드 - 발급 받은 코드로 토큰 발행
    @RequestMapping("/callback")
    public OauthToken.response code(@RequestParam String code){

        String cridentials = "clientId:secretKey";
        // base 64로 인코딩 (basic auth 의 경우 base64로 인코딩 하여 보내야한다.)
        String encodingCredentials = new String(
                Base64.encodeBase64(cridentials.getBytes())
        );
        String requestCode = code;
        OauthToken.request.accessToken request = new OauthToken.request.accessToken(){{
            setCode(requestCode);
            setGrant_type("authorization_code");
            setRedirect_uri("http://localhost:8081/callback");
        }};
        // oauth 서버에 http 통신으로 토큰 발행
        OauthToken.response oauthToken = Unirest.post("http://localhost:8081/oauth/token")
                .header("Authorization","Basic "+encodingCredentials)
                .fields(request.getMapData())
                .asObject(OauthToken.response.class).getBody();
        return oauthToken;
    }

}