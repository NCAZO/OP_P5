package com.openclassrooms.starterjwt.controllers;

import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SessionControllerTest {

	// Création de Mock
		SessionMapper sessionMapperMock = mock(SessionMapper.class);
		SessionService sessionServiceMock = mock(SessionService.class);
	
		@Test
		public void testSessionFindByID() {
			Long id = 1L;
			String name = "Session name";
			Session session = Session.builder().id(id).name(name).build();

			SessionDto sessionDto = new SessionDto();
			sessionDto.setId(id);
			sessionDto.setName(name);

			when(sessionServiceMock.getById(id)).thenReturn(session);
			when(sessionMapperMock.toDto(session)).thenReturn(sessionDto);

			SessionController sessionController = new SessionController(sessionServiceMock, sessionMapperMock);
			ResponseEntity<?> response = sessionController.findById(id.toString());

			verify(sessionServiceMock).getById(id);
			verify(sessionMapperMock).toDto(session);
			assertEquals(HttpStatus.OK, response.getStatusCode());
			assertEquals(response.getBody(), sessionDto);
		}
		
		@Test
		public void testSessionFindByIDNotOK() {
			Long id = 1L;

			when(sessionServiceMock.getById(id)).thenReturn(null);

			SessionController sessionController = new SessionController(sessionServiceMock, sessionMapperMock);
			ResponseEntity<?> response = sessionController.findById(id.toString());

			verify(sessionServiceMock).getById(id);
			assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
			assertEquals(response.getBody(), null);
		}
		
		@Test
		public void testSessionFindAll() {
			List<Session> sessions = new ArrayList<>();
			sessions.add(new Session());
			sessions.add(new Session());
			sessions.add(new Session());

			List<SessionDto> sessionDtos = new ArrayList<>();
			sessionDtos.add(new SessionDto());
			sessionDtos.add(new SessionDto());
			sessionDtos.add(new SessionDto());

			when(sessionServiceMock.findAll()).thenReturn(sessions);
			when(sessionMapperMock.toDto(sessions)).thenReturn(sessionDtos);
			SessionController sessionController = new SessionController(sessionServiceMock, sessionMapperMock);
			ResponseEntity<?> responseEntity = sessionController.findAll();

			assertEquals(200, responseEntity.getStatusCodeValue());
			assertEquals(sessionDtos, responseEntity.getBody());
		}
		
		@Test
		public void testSessionFindByIDBadRequest() {
			SessionController sessionController = new SessionController(sessionServiceMock, sessionMapperMock);
			ResponseEntity<?> response = sessionController.findById("wrong ID");

			assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		}
		
		@Test
		public void testSessionCreate() {
			String name = "newSession";
			Session session = Session.builder().name(name).build();

			SessionDto sessionDto = new SessionDto();
			sessionDto.setName(name);

			when(sessionMapperMock.toEntity(sessionDto)).thenReturn(session);
			when(sessionServiceMock.create(session)).thenReturn(session);
			when(sessionMapperMock.toDto(session)).thenReturn(sessionDto);

			SessionController sessionController = new SessionController(sessionServiceMock, sessionMapperMock);
			ResponseEntity<?> responseEntity = sessionController.create(sessionDto);

			verify(sessionMapperMock).toEntity(sessionDto);
			verify(sessionServiceMock).create(session);
			verify(sessionMapperMock).toDto(session);
			assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
			assertEquals(sessionDto, responseEntity.getBody());
		}
		
		@Test
		public void testSessionUpdateOK() {
			Long id = 1L;
			String name = "createdSession";
			Session session = Session.builder().id(id).name(name).build();

			SessionDto sessionDto = new SessionDto();
			sessionDto.setId(id);
			sessionDto.setName("updatedSession");
			when(sessionMapperMock.toEntity(sessionDto)).thenReturn(session);
			when(sessionServiceMock.update(id, session)).thenReturn(session);
			when(sessionMapperMock.toDto(session)).thenReturn(sessionDto);
			SessionController sessionController = new SessionController(sessionServiceMock, sessionMapperMock);
			ResponseEntity<?> responseEntity = sessionController.update(id.toString(), sessionDto);

			verify(sessionMapperMock).toEntity(sessionDto);
			verify(sessionServiceMock).update(id, session);
			verify(sessionMapperMock).toDto(session);
			assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
			assertEquals(sessionDto, responseEntity.getBody());
		}
		
		@Test
		public void testSessionUpdateBadRequest() {
			SessionController sessionController = new SessionController(sessionServiceMock, sessionMapperMock);
			ResponseEntity<?> response = sessionController.update("wrong ID", null);

			assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		}
		
		@Test
		public void testSessionDeleteOK() {
			Long id = 1L;
			String name = "nomSession";
			Session session = Session.builder().id(id).name(name).build();

			when(sessionServiceMock.getById(id)).thenReturn(session);
			SessionController sessionController = new SessionController(sessionServiceMock, sessionMapperMock);
			ResponseEntity<?> response = sessionController.save(id.toString());

			verify(sessionServiceMock).getById(id);
			assertEquals(HttpStatus.OK, response.getStatusCode());
		}
		
		@Test
		public void testSessionDeleteBadRequest() {
			SessionController sessionController = new SessionController(sessionServiceMock, sessionMapperMock);
			ResponseEntity<?> response = sessionController.save("wrong ID");

			assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		}
		
		@Test
		public void testSessionDeleteNotOK() {
			Long id = 1L;

			when(sessionServiceMock.getById(id)).thenReturn(null);
			SessionController sessionController = new SessionController(sessionServiceMock, sessionMapperMock);
			ResponseEntity<?> response = sessionController.save(id.toString());

			verify(sessionServiceMock).getById(id);
			assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		}
		
		@Test
	    void testSessionParticipateOK() {
			
			SessionController sessionController = new SessionController(sessionServiceMock, sessionMapperMock);
			ResponseEntity<?> response = sessionController.participate("1", "1");

			assertEquals(HttpStatus.OK, response.getStatusCode());
	    }
		
		@Test
		public void testSessionParticipateBadRequest() {
			SessionController sessionController = new SessionController(sessionServiceMock, sessionMapperMock);
			ResponseEntity<?> response = sessionController.participate("notgoodId", "wrongId");

			assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		}

		@Test
		public void testSessionNoLongerParticipateOK() {
			Long userId = 1L;
			Long sessionId = 1L;

			SessionController sessionController = new SessionController(sessionServiceMock, sessionMapperMock);
			ResponseEntity<?> response = sessionController.noLongerParticipate(sessionId.toString(), userId.toString());

			verify(sessionServiceMock).noLongerParticipate(sessionId, userId);
			assertEquals(HttpStatus.OK, response.getStatusCode());
		}

		@Test
		public void testSessionNoLongerParticipateBadRequest() {
			SessionController sessionController = new SessionController(sessionServiceMock, sessionMapperMock);
			ResponseEntity<?> response = sessionController.noLongerParticipate("wrong ID", "wrongId");

			assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		}
}
