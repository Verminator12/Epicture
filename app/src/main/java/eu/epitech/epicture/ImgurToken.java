package eu.epitech.epicture;

import android.net.Uri;

public class ImgurToken {
    private String accessToken;
    private String tokenType;
    private String accountId;
    private String accountUsername;

    public ImgurToken (String accessToken, String tokenType, String accountId, String accountUsername) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.accountId = accountId;
        this.accountUsername = accountUsername;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getAccountUsername() {
        return accountUsername;
    }

    public String toAuthHeader() {
        if (tokenType.toLowerCase().equals("client_id"))
            return "Client-ID " + accessToken;
        if (tokenType.toLowerCase().equals("bearer"))
            return "Bearer " + accessToken;
        else
            return tokenType + " " + accessToken;
    }

    public ImgurToken parseUri(String fragment) {
        Uri fragmentQuery = Uri.parse("?" + fragment);

        String accessToken = fragmentQuery.getQueryParameter("access_token");
        String tokenType= fragmentQuery.getQueryParameter("token_type");
        String accountId = fragmentQuery.getQueryParameter("account_id");
        String accountUsername = fragmentQuery.getQueryParameter("account_username");

        return new ImgurToken(accessToken, tokenType, accountId, accountUsername);
    }
}
