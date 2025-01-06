package com.openclassrooms.starterjwt.security;

import static org.mockito.Mockito.mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;

public class UserDetailsServiceImplTest {
	
	
	UserRepository userRepoMock = mock(UserRepository.class);

	@Test
	void testLoadUserByUsername() {
		Long userId = 1L;
		String email = "test@mail.com";
		String password = "password";
		String firstName = "Doe";
		String lastName = "John";

		User user = User.builder().id(userId).email(email).password(password).firstName(firstName).lastName(lastName)
				.build();

		when(userRepoMock.findByEmail(anyString())).thenReturn(Optional.of(user));
		UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl(userRepoMock);
		UserDetails userDetails = userDetailsService.loadUserByUsername(email);

		assert userDetails instanceof UserDetailsImpl;

		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;

		assertEquals(userId, userDetailsImpl.getId());
		assertEquals(email, userDetailsImpl.getUsername());
		assertEquals(firstName, userDetailsImpl.getFirstName());
		assertEquals(lastName, userDetailsImpl.getLastName());
		assertEquals(password, userDetailsImpl.getPassword());
	}

	@Test
    void testLoadUserByUsernameNotFound() {
      when(userRepoMock.findByEmail(anyString())).thenReturn(Optional.empty());
      UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl(userRepoMock);

      assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("test@mail.com"));
    }

}
