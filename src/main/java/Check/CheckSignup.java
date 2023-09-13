package Check;

import java.util.regex.Pattern;

import javax.swing.JOptionPane;

public class CheckSignup {
	
	String username, password, repass, email;
	
	public CheckSignup(String username, String password, String repass, String email) {
		this.username = username;
		this.password = password;
		this.repass = repass;
		this.email = email;
	};
	
	
	public boolean check() {
		if (!Length()) return false;
		if (!EqualPass()) return false;
		if (!isEmail()) return false;
		return true;
	}
	
	public boolean Length() {
		if (username.length() == 0 || password.length() == 0 || repass.length() == 0 || email.length() == 0) {
			JOptionPane.showConfirmDialog(null, "Please complete all information", "", JOptionPane.CLOSED_OPTION, JOptionPane.DEFAULT_OPTION);
			return false;
		}
		if (username.length() > 256 || password.length() > 256 || repass.length() > 256 || email.length() > 256) {
			JOptionPane.showConfirmDialog(null, "The length of the information cannot be more than 256 characters", "", JOptionPane.CLOSED_OPTION, JOptionPane.DEFAULT_OPTION);
			return false;
		}
		if (password.length() <= 8) {
			JOptionPane.showConfirmDialog(null, "Password must be longer than 8 characters", "", JOptionPane.CLOSED_OPTION, JOptionPane.DEFAULT_OPTION);
			return false;
		}
		return true;
	}
	
	public boolean EqualPass() {
		if (!password.equals(repass)) {
			JOptionPane.showConfirmDialog(null, "Password and re-password are not the same", "", JOptionPane.CLOSED_OPTION, JOptionPane.DEFAULT_OPTION);
			return false;
		}
		return true;
	}
	
//	 		. 	any character
//	 		+ 	1 or many 
//	 		* 	0 or many
//	  		? 	0 or 1
	
	public boolean isEmail() {
		// begins with a digit
		String lp1 = "[0-9]+";	
		String lp2 = ".*[a-zA-Z].*";	// at least 1 letter
		String lp3 = "(\\.[a-zA-Z0-9]+)*";
		String localPart = lp1 + lp2 + lp3;		
		String domainName = "([a-zA-Z]{2,}+)(\\.[a-zA-Z]{2,}+){1,3}";
		Pattern p = Pattern.compile("^" + localPart +"@"+ domainName + "$");
		
		if (p.matcher(email).find()) return true;
		
		// begins with a letter
		lp1 = "[a-zA-Z]";	
		lp2 = "[a-zA-Z0-9]+";	
		lp3 = "(\\.[a-zA-Z0-9]+)*";
		localPart = lp1 + lp2 + lp3;			
		p = Pattern.compile("^" + localPart +"@"+ domainName + "$");
		
		if (p.matcher(email).find()) return true;
		JOptionPane.showConfirmDialog(null, "Invalid email", "", JOptionPane.CLOSED_OPTION, JOptionPane.DEFAULT_OPTION);
		return false;
	}
}
