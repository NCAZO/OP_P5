package integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.SpringBootSecurityJwtApplication;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(classes = SpringBootSecurityJwtApplication.class)
@AutoConfigureMockMvc
public class AuthIntegreTest {
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;
	
	ObjectMapper ObjMapper = new ObjectMapper();
	
//	 @Test
//	  public void testAuthenticateUser() throws Exception {
//	    ObjectNode requestBody = ObjMapper.createObjectNode();
//	    requestBody.put("email", "yoga@studio.com");
//	    requestBody.put("password", "test!1234");
//
//	    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
//	        .contentType(MediaType.APPLICATION_JSON)
//	        .content(ObjMapper.writeValueAsString(requestBody)))
//	        .andReturn();
//	    assertThat(result.getResponse().getStatus()).isEqualTo(200);
//	  }
	
//	@Test
//	public void testRegisterUser() throws Exception {
//
//		SignupRequest signupRequest = new SignupRequest();
//		signupRequest.setEmail("nicolas@test.com");
//		signupRequest.setFirstName("Nicolas");
//		signupRequest.setLastName("CAZO");
//		signupRequest.setPassword("password");
//
//		String jsonRequest = objectMapper.writeValueAsString(signupRequest);
//
//		mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
//				.andExpect(status().isOk());
//		
//		LoginRequest loginRequest = new LoginRequest();
//		loginRequest.setEmail("nicolas@test.com");
//		loginRequest.setPassword("password");
//		String loginJsonRequest = objectMapper.writeValueAsString(loginRequest);
//
//		MvcResult result = mockMvc
//				.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(loginJsonRequest))
//				.andExpect(status().isOk()).andReturn();
//		
//		String responseBody = result.getResponse().getContentAsString();
//		String authToken = objectMapper.readTree(responseBody).get("token").textValue();
//		Integer userId = objectMapper.readTree(responseBody).get("id").intValue();
//		String stringUserId = String.valueOf(userId);
//		
//		mockMvc.perform(delete("/api/user/{id}", stringUserId).contentType(MediaType.APPLICATION_JSON)
//				.header("Authorization", "Bearer " + authToken))
//				.andExpect(status().isOk());
//	}
	
//	@Test
//	  public void testRegisterUserEmailAlreadyExist() throws Exception {
//	    ObjectNode requestBody = ObjMapper.createObjectNode();
//	    requestBody.put("email", "yoga@studio.com");
//	    requestBody.put("password", "password");
//	    requestBody.put("firstName", "Test");
//	    requestBody.put("lastName", "Exemple");
//
//	    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
//	        .contentType(MediaType.APPLICATION_JSON)
//	        .content(ObjMapper.writeValueAsString(requestBody)))
//	        .andExpect(MockMvcResultMatchers.status().isBadRequest()) // Expecting a bad request
//	        .andReturn();
//
//	    String responseContent = result.getResponse().getContentAsString();
//	    assertThat(responseContent).contains("Error: Email is already taken!");
//	  }
//	
}