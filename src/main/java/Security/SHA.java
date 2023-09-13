package Security;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class SHA {
	
	public SHA() {}
	
    public String encryptPassword(String password) {
    	return Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
    }
}