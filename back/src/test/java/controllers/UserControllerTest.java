package controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.services.UserService;

public class UserControllerTest {
	
    // Créez un mock pour le service User
    UserService userServiceMock = mock(UserService.class);
    // Créez un mock pour le service User
    UserMapper userMapperMock = mock(UserMapper.class);

	@Test
	public void testUserFindByIdOK() {
		
		User user;
		Long id = 1L;
		String email = "nicolas.test@gmail.com";
		String lastName = "Nicolas";
		String firstName = "Test";
		String password = "password";
		boolean admin = true;
		LocalDateTime createdAt = LocalDateTime.now();
		LocalDateTime updatedAt = LocalDateTime.now();

		user = User.builder().id(id).email(email).lastName(lastName).firstName(firstName).password(password)
				.admin(admin).createdAt(createdAt).updatedAt(updatedAt).build();

		UserDto userDto = new UserDto();
		userDto.setId(id);
		userDto.setLastName(lastName);

		when(userServiceMock.findById(id)).thenReturn(user);
		when(userMapperMock.toDto(user)).thenReturn(userDto);

		UserController userController = new UserController(userServiceMock, userMapperMock);
		ResponseEntity<?> response = userController.findById(id.toString());

		verify(userServiceMock).findById(id);
		verify(userMapperMock).toDto(user);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(response.getBody(), userDto);
	}
	
	@Test
	public void testUserFindByIdNotOK() {
        
		Long id = 1L;

		when(userServiceMock.findById(id)).thenReturn(null);

		UserController userController = new UserController(userServiceMock, userMapperMock);
		ResponseEntity<?> response = userController.findById(id.toString());

		verify(userServiceMock).findById(id);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(response.getBody(), null);
	}
	
	@Test
	public void testTeacherFindByIdBadRequest() {
		UserController userController = new UserController(userServiceMock, userMapperMock);
		ResponseEntity<?> response = userController.findById("notgoodId");

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	@Test
	public void testUserDeleteOK() {
		Long id = 1L;
		String email = "nicolas.test@gmail.com";
		String lastName = "Nicolas";
		String firstName = "Test";
		String password = "password";
		boolean admin = true;
		LocalDateTime createdAt = LocalDateTime.now();
		LocalDateTime updatedAt = LocalDateTime.now();

		User user = User.builder().id(id).email(email).lastName(lastName).firstName(firstName).password(password)
				.admin(admin).createdAt(createdAt).updatedAt(updatedAt).build();

		UserDetailsImpl userDetails = UserDetailsImpl.builder().id(id).username(email).firstName(firstName)
				.lastName(lastName).password(password).admin(admin).build();

		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

		SecurityContext securityContext = mock(SecurityContext.class);
		SecurityContextHolder.setContext(securityContext);

		when(securityContext.getAuthentication()).thenReturn(authentication);
		when(userServiceMock.findById(id)).thenReturn(user);
		doNothing().when(userServiceMock).delete(id);

		UserController userController = new UserController(userServiceMock, userMapperMock);
		ResponseEntity<?> response = userController.save(id.toString());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(userServiceMock).findById(id);
		verify(userServiceMock).delete(id);
	}
	
	@Test
	public void testUserDeleteNotOK() {
		Long id = 1L;

		when(userServiceMock.findById(id)).thenReturn(null);

		UserController userController = new UserController(userServiceMock, userMapperMock);
		ResponseEntity<?> response = userController.save(id.toString());

		verify(userServiceMock).findById(id);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
	
	@Test
	public void testUserDeleteBadRequest() {
		UserController userController = new UserController(userServiceMock, userMapperMock);
		ResponseEntity<?> response = userController.save("wrong ID");

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	@Test
	public void testUserUnauthorizedDelete() {
		Long id = 1L;
		String email = "marc.petit@user.com";
		String lastName = "Petit";
		String firstName = "Marc";
		String password = "password";
		boolean admin = true;
		LocalDateTime createdAt = LocalDateTime.now();
		LocalDateTime updatedAt = LocalDateTime.now();

		User user = User.builder().id(id).email(email).lastName(lastName).firstName(firstName).password(password)
				.admin(admin).createdAt(createdAt).updatedAt(updatedAt).build();

		UserDetailsImpl userDetailsImpl = mock(UserDetailsImpl.class);

		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetailsImpl, null);

		SecurityContext securityContext = mock(SecurityContext.class);
		SecurityContextHolder.setContext(securityContext);

		when(securityContext.getAuthentication()).thenReturn(authentication);
		when(userServiceMock.findById(id)).thenReturn(user);
		when(userDetailsImpl.getUsername()).thenReturn("test.test@gmail.com");

		UserController userController = new UserController(userServiceMock, userMapperMock);
		ResponseEntity<?> response = userController.save(id.toString());

		assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
		verify(userServiceMock).findById(id);
		verify(userServiceMock, never()).delete(id);
	}
}
