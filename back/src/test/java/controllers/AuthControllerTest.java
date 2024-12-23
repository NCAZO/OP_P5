package controllers;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;


import static org.mockito.Mockito.*;

import java.util.Optional;


public class AuthControllerTest {
	

    // Création Mock
    UserRepository authServiceMock = mock(UserRepository.class);
    AuthenticationManager authenticationManagerMock = mock(AuthenticationManager.class);
    PasswordEncoder passwordEncoderMock = mock(PasswordEncoder.class);
    UserRepository userRepositoryMock = mock(UserRepository.class);
    JwtUtils jwtUtilsMock = mock(JwtUtils.class);

    @Test
	public void testAuthenticateUser() {
		Long id = 1L;
		String email = "nicolas.test@gmail.com";
		String password = "password";
		String firstname = "Nicolas";
		String lastname = "Test";
		boolean isAdmin = false;

		UserDetailsImpl userDetails = UserDetailsImpl.builder().username(email).firstName(firstname).lastName(lastname)
				.id(id).password(password).build();

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

		when(authenticationManagerMock.authenticate(new UsernamePasswordAuthenticationToken(email, password)))
				.thenReturn(authentication);
		when(jwtUtilsMock.generateJwtToken(authentication)).thenReturn("jwt");
		when(userRepositoryMock.findByEmail(email)).thenReturn(Optional.of(User.builder().id(id).email(email)
				.password(password).firstName(firstname).lastName(lastname).admin(isAdmin).build()));

		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setEmail(email);
		loginRequest.setPassword(password);

		AuthController authController = new AuthController(authenticationManagerMock, passwordEncoderMock, jwtUtilsMock,
				authServiceMock);
		ResponseEntity<?> response = authController.authenticateUser(loginRequest);
		JwtResponse responseBody = (JwtResponse) response.getBody();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(email, responseBody.getUsername());
		assertEquals(firstname, responseBody.getFirstName());
		assertEquals(lastname, responseBody.getLastName());
		assertEquals(id, responseBody.getId());
		assertEquals(isAdmin, responseBody.getAdmin());
		assertEquals("Bearer", responseBody.getType());
		assertNotNull(responseBody.getToken());
	}
	
	 @Test
	    public void testSuccessfullRegistration() {

		 String email = "test.nathan@mail.fr";
			String password = "1";

			when(userRepositoryMock.existsByEmail(email)).thenReturn(false);
			when(passwordEncoderMock.encode(password)).thenReturn("hashed");
			when(userRepositoryMock.save(any(User.class))).thenReturn(new User());

			AuthController authController = new AuthController(authenticationManagerMock, passwordEncoderMock, jwtUtilsMock,
					userRepositoryMock);

			SignupRequest signupRequest = new SignupRequest();
			signupRequest.setEmail(email);
			signupRequest.setFirstName("");
			signupRequest.setLastName("");
			signupRequest.setPassword(password);
			ResponseEntity<?> response = authController.registerUser(signupRequest);
			assertEquals(HttpStatus.OK, response.getStatusCode());
		 
//	        User newUser = new User();
//	        
//	        newUser.setEmail("nico@test.com");
//	        newUser.setPassword("nicolas");
//	        
//	        User saveUser = new User();
//	        saveUser.setId(1L);
//	        saveUser.setEmail("nico@test.com");
//	        saveUser.setPassword("nicolas");
//	        
//	        when(userRepositoryMock.existsByEmail(saveUser.getEmail())).thenReturn(false);
//	        
//	        // Configurez le mock pour renvoyer true lors de l'inscription
//	        when(userRepositoryMock.save(newUser)).thenReturn(saveUser);
//	        
//
//	        // Appelez la méthode de la classe sous test
//	        User registredUser = userRepositoryMock.save(newUser);
//
//	        // Vérifiez que l'inscription a réussi
//	        assertSame(registredUser, saveUser);
	    }
	 
	 @Test
		public void testEmailAlreadyUseRegistration() {
			String email = "nico@test.com";
			String password = "nicolas";
			String firstname = "Nicolas";
			String lastname = "CAZO";
			when(userRepositoryMock.existsByEmail(email)).thenReturn(true);
			AuthController authController = new AuthController(authenticationManagerMock, passwordEncoderMock, jwtUtilsMock,
					userRepositoryMock);

			SignupRequest signupRequest = new SignupRequest();
			signupRequest.setEmail(email);
			signupRequest.setFirstName(firstname);
			signupRequest.setLastName(lastname);
			signupRequest.setPassword(password);

			ResponseEntity<?> response = authController.registerUser(signupRequest);

			MessageResponse messageResponse = (MessageResponse) response.getBody();
			assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
			assertEquals("Error: Email is already taken!", messageResponse.getMessage());

		}
	 
//	 @Test
//	    public void testEmailAlreadyUseRegistration() {
//
//	        User newUser = new User();
//	        
//	        newUser.setEmail("nico@test.com");
//	        newUser.setPassword("nicolas");
//	        
//	        User saveUser = new User();
//	        saveUser.setId(1L);
//	        saveUser.setEmail("nico@test.com");
//	        saveUser.setPassword("nicolas");
//
//	        when(userRepositoryMock.existsByEmail(saveUser.getEmail())).thenReturn(true);
//	        
//	        AuthController authController = new AuthController(null, null, null, authServiceMock);
//	        
//	        SignupRequest signupRequest = new SignupRequest();
//			signupRequest.setEmail(newUser.getEmail());
//			signupRequest.setFirstName("");
//			signupRequest.setLastName("");
//			signupRequest.setPassword(newUser.getPassword());
//	        
//			ResponseEntity<?> response = authController.registerUser(signupRequest);
//			
//	        MessageResponse messageResponse = (MessageResponse) response.getBody();
//			assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//			assertEquals("Error: Email is already taken!", messageResponse.getMessage());
//	    }
}
