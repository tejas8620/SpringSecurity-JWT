package in.tejas.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.tejas.binding.CredentialBinding;
import in.tejas.entity.UserEntity;
import in.tejas.service.JwtService;
import in.tejas.service.MyUserDetailsService;

@RestController
@RequestMapping("/api")
public class UserRestController {

	
	@Autowired
	private PasswordEncoder pwdEncoder;
	
	@Autowired
	private MyUserDetailsService userDtlSrv;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private JwtService jwt;
	
	
	//get welcome method
	@GetMapping("/welcome")
	public String getWelcomeMsg() {
		return "Welcome to our Spring Security JWT application";
	}
	
	
	//greet method
	@GetMapping("/greet")
	public String getGreetMsg() {
		return "Good Morning..";
	}
	
	
	//register method
	@PostMapping("/register")
	public String registerUser(@RequestBody UserEntity user) {
		
		String encodedPwd = pwdEncoder.encode(user.getUpwd());
		user.setUpwd(encodedPwd);
		
		boolean saveUser = userDtlSrv.saveUser(user);
		
		if(saveUser) {
			return "User Registerd";
		}
		else {
			return "Registration Failed";
		}
		
	}
	
	
	//login method
	@PostMapping("/login")
	public ResponseEntity<String> checkLogin(@RequestBody CredentialBinding  user){
		
		UsernamePasswordAuthenticationToken token = 
				new UsernamePasswordAuthenticationToken(user.getUname(), user.getUpwd());
		
		try {
			Authentication authenticate = authManager.authenticate(token);
			
			if(authenticate.isAuthenticated()) {
				String jwtToken = jwt.generateToken(user.getUname());
				return new ResponseEntity<String>(jwtToken, HttpStatus.OK);
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		return new ResponseEntity<String>("Invalid Credentials", HttpStatus.BAD_REQUEST);
		
	}
	
}
