package natemobiles.app.simpleyelpapiforandroid.configs;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;

/**
 * YelpApiV2
 * 
 * Yelp API class extends DefaultApi10a for Oauth.
 * 
 * @author nkemavaha
 *
 */
public class YelpApiV2 extends DefaultApi10a{

	/**
	 * Authorize URL for Yelp's Oauth version 2
	 */
	private static final String AUTHORIZE_URL = "https://api.yelp.com/oauth/v2/request_auth?oauth_token=%s";
	
	/**
	 * Request Token URL for Yelp's Oauth version 2
	 */
	private static final String REQUEST_TOKEN_RESOURCE = "https://api.yelp.com/oauth/v2/request_token";
	
	/**
	 * Access Token URL for Yelp's Oauth version 2
	 */
	private static final String ACCESS_TOKEN_RESOURCE = "https://api.yelp.com/oauth/v2/access_token";

	@Override
	public String getAccessTokenEndpoint() {
		return ACCESS_TOKEN_RESOURCE;
	}

	@Override
	public String getAuthorizationUrl(Token requestToken) {
		return String.format(AUTHORIZE_URL, requestToken.getToken());
	}

	@Override
	public String getRequestTokenEndpoint() {
		return REQUEST_TOKEN_RESOURCE;
	}
}
