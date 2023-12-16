package com.example.waggle.web.dto.oauth;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;


public class OAuthToken {
    @Getter
    @Builder
    public static class response{

        private String access_token;
        private String token_type;
        private String refresh_token;
        private long expires_in;
        private String scope;

    }

    @Getter
    public static class request{

        @Getter
        @Builder
        public static class accessToken{
            public String code;
            private String grant_type;
            private String redirect_uri;

        }

        @Data
        public static class refreshToken{
            private String refreshToken;
            private String grant_type;

            public Map getMapData(){
                Map map = new HashMap();
                map.put("refresh_token",refreshToken);
                map.put("grant_type",grant_type);
                return map;
            }
        }
    }
}
