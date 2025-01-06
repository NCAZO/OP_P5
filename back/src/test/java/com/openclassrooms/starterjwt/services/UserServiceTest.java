package com.openclassrooms.starterjwt.services;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;
import static org.mockito.Mockito.times;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;

public class UserServiceTest {
	

	//Créez un mock pour le user repo
    UserRepository userRepoMock = mock(UserRepository.class);
	
	@Test
	public void testGetUserById() {

		User newUser = new User();
		
		newUser.setId(1L);
		
		when(userRepoMock.findById(newUser.getId())).thenReturn(Optional.of(newUser));
		
		UserService userService = new UserService(userRepoMock);
		
		User foundUser = userService.findById(newUser.getId());

		//Verif correspondace user recherché et user trouvé
		Assertions.assertEquals(newUser.getId(), foundUser.getId());
	}

	@Test
	public void testDeleteUser() {
		
		User newUser = new User();
		
		newUser.setId(1L);
		
		doNothing().when(userRepoMock).deleteById(anyLong());
		
		//Créé un utilisateur et le supprime
		UserService userService = new UserService(userRepoMock);
		userService.delete(newUser.getId());
		
		//Vérifier la suppression de l'utilisateur
		verify(userRepoMock, times(1)).deleteById(newUser.getId());
	}
}
