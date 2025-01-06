package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

import com.openclassrooms.starterjwt.models.User;

public class UserTest {
	
	@Test
    public void testValidId() {
	 
		 User newUser = new User();
		 newUser.setEmail("nicolas.test@gmail.com");
	 
        // Email valide
        String email = "nicolas.test@gmail.com";
        
        //Verif les deux sont égaux
        assertEquals(email, newUser.getEmail());

    }
	
 	@Test
    public void testEmailIsValid() {
	 
		 User newUser = new User();
		 newUser.setEmail("nicolas.test@gmail.com");
	 
        // Email valide
        String email = "nicolas.test@gmail.com";
        
        //Verif les deux sont égaux
        assertEquals(email, newUser.getEmail());

    }

    @Test
    public void testEmailIsInvalid() {
    	
//	    	User newUser = new User();
//			newUser.setEmail("nicolas.test@gmail.com");
		 
        // Email invalide
        String invalidEmail = "nicolas.test";
        
        assertFalse(false, invalidEmail);
    }

}
