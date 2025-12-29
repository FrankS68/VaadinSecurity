package consulting.segieth.security;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class BlueskyMetadataController {

    @GetMapping(value = "/client-metadata.json", produces = "application/json")
    public Map<String, Object> getMetadata() {
        Map<String, Object> metadata = new HashMap<>();

        // 1. Die Client ID MUSS die URL zu dieser Datei sein
        String appUrl = "localhost:8080";
		metadata.put("client_id", "https://"
        		+ appUrl
        		+ "/client-metadata.json");
        
        metadata.put("client_name", "Meine Vaadin Bluesky App");
        metadata.put("client_uri", "https://"
        		+ appUrl);
        metadata.put("logo_uri", "https://"
        		+ appUrl
        		+ "/icons/icon.png");
        
        // 2. Redirect URIs (müssen exakt mit deiner Spring Security Konfiguration übereinstimmen)
        metadata.put("redirect_uris", List.of(
            "https://"
            + appUrl
            + "/login/oauth2/code/bluesky"
        ));

        // 3. OAuth Spezifikationen für Bluesky
        metadata.put("grant_types", List.of("authorization_code", "refresh_token"));
        metadata.put("response_types", List.of("code"));
        metadata.put("scope", "atproto");
        
        // 4. Wichtig für die Sicherheit: DPoP wird von Bluesky erzwungen
        metadata.put("token_endpoint_auth_method", "none"); // PKCE wird genutzt
        metadata.put("dpop_bound_access_tokens", true);

        return metadata;
    }
}
