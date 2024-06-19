package integration;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.openclassrooms.starterjwt.SpringBootSecurityJwtApplication;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;


@SpringBootTest(classes = SpringBootSecurityJwtApplication.class)
@AutoConfigureMockMvc
public class SessionIntegrTest {
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JwtUtils jwtUtils;

	private String jwt;
	
//	MARCHE 
	@Test
	@WithMockUser
	public void testSessionFindAll() throws Exception {
		UserDetailsImpl userDetails = UserDetailsImpl.builder().username("yoga@studio.com").build();
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "dddddddddddddddddd");
		jwtUtils = new JwtUtils();
		jwtUtils.setJwtSecret("openclassrooms");
		jwtUtils.setJwtExpirationMs(86400000);
		jwt = jwtUtils.generateJwtToken(authentication);
		mockMvc.perform(get("/api/session").header("Authorization", "Bearer " + jwt)).andExpect(status().isOk());
	}
}
