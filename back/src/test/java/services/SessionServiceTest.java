package services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;

import org.junit.jupiter.api.Assertions;

public class SessionServiceTest {
	
	//Création de Mock
    SessionRepository sessionRepoMock = mock(SessionRepository.class);
    UserRepository userRepoMock = mock(UserRepository.class);
	
	@Test
	public void testCreateSession() {
		Session session = new Session();
		session.setId(1L);
		when(sessionRepoMock.save(any(Session.class))).thenReturn(session);
		SessionService sessionService = new SessionService(sessionRepoMock, userRepoMock);
		Session createdSession = sessionService.create(session);

		Assertions.assertEquals(session.getId(), createdSession.getId());
	}
	
	@Test
	public void testFindAllSessions() {
		List<Session> sessionList = new ArrayList<>();
		Session session = new Session();
		session.setId(1L);
		sessionList.add(session);
		when(sessionRepoMock.findAll()).thenReturn(sessionList);
		SessionService sessionService = new SessionService(sessionRepoMock, userRepoMock);
		List<Session> sessionFound = sessionService.findAll();

		Assertions.assertEquals(1, sessionFound.size());
	}

	@Test
	public void testDeleteSession() {
		Session session = new Session();
		session.setId(1L);
		doNothing().when(sessionRepoMock).deleteById(anyLong());
		SessionService sessionService = new SessionService(sessionRepoMock, userRepoMock);
		sessionService.delete(session.getId());

		verify(sessionRepoMock, times(1)).deleteById(session.getId());
	}

	@Test
	public void testSessionGetById() {
		Session session = new Session();
		session.setId(1L);
		when(sessionRepoMock.findById(anyLong())).thenReturn(Optional.of(session));
		SessionService sessionService = new SessionService(sessionRepoMock, userRepoMock);
		Session sessionFound = sessionService.getById(session.getId());

		Assertions.assertEquals(session.getId(), sessionFound.getId());
	}

	@Test
	public void testUpdateSession() {
		Session session = new Session();
		session.setId(1L);
		when(sessionRepoMock.save(any(Session.class))).thenReturn(session);
		SessionService sessionService = new SessionService(sessionRepoMock, userRepoMock);
		Session sessionUpdated = sessionService.update(session.getId(), session);

		Assertions.assertEquals(session.getId(), sessionUpdated.getId());
	}

	@Test
	public void testParticipate() {
		Long sessionId = 1L;
		Session session = Session.builder().name("Session Test").id(sessionId).users(new ArrayList<>()).build();

		Long userId = 2L;
		User user = User.builder().id(userId).email("test@mail.com").lastName("Doe").firstName("John")
				.password("password").build();

		when(sessionRepoMock.findById(anyLong())).thenReturn(Optional.of(session));
		when(userRepoMock.findById(anyLong())).thenReturn(Optional.of(user));
		SessionService sessionService = new SessionService(sessionRepoMock, userRepoMock);
		sessionService.participate(session.getId(), user.getId());

		Assertions.assertEquals(1, session.getUsers().size());
		Assertions.assertEquals(user.getId(), session.getUsers().get(0).getId());
	}

	@Test
	public void testParticipateNotFoundException() {

		Long sessionID = 1L;
		Long userID = 2L;

		Session session = new Session();
		session.setId(sessionID);

		when(sessionRepoMock.findById(sessionID)).thenReturn(Optional.of(session));
		when(userRepoMock.findById(userID)).thenReturn(Optional.empty());

		SessionService sessionService = new SessionService(sessionRepoMock, userRepoMock);

		assertThrows(NotFoundException.class, () -> sessionService.participate(sessionID, userID));

		verify(sessionRepoMock, times(1)).findById(sessionID);
		verify(userRepoMock, times(1)).findById(userID);
		verify(sessionRepoMock, never()).save(any(Session.class));
	}

	@Test
	public void testParticipateBadRequestException() {

		Long sessionID = 1L;

		Long userID = 2L;
		User user = User.builder().id(userID).email("nicolas@test.com").lastName("CAZO").firstName("Nicolas")
				.password("password").build();

		List<User> users = new ArrayList<>();
		users.add(user);

		Session session = Session.builder().name("Session1").id(sessionID).users(users).build();

		when(sessionRepoMock.findById(sessionID)).thenReturn(Optional.of(session));
		when(userRepoMock.findById(userID)).thenReturn(Optional.of(user));

		SessionService sessionService = new SessionService(sessionRepoMock, userRepoMock);

		assertThrows(BadRequestException.class, () -> sessionService.participate(sessionID, userID));
	}

	@Test
	public void testNoLongerParticipate() {
		Long sessionID = 1L;
		Long userID = 2L;
		Session session = new Session();
		User user = new User();
		user.setId(userID);
		List<User> users = new ArrayList<>();
		users.add(user);
		session.setUsers(users);

		when(sessionRepoMock.findById(sessionID)).thenReturn(Optional.of(session));
		SessionService sessionService = new SessionService(sessionRepoMock, userRepoMock);
		sessionService.noLongerParticipate(sessionID, userID);

		verify(sessionRepoMock, times(1)).findById(sessionID);
		verify(sessionRepoMock, times(1)).save(session);

		List<User> updatedUsers = session.getUsers();
		assertTrue(updatedUsers.isEmpty());
	}

	@Test
	public void testNoLongerParticipateBadRequestException() {
		Long sessionID = 1L;
		Long userID = 2L;

		Session session = Session.builder().name("Session1").id(sessionID).users(new ArrayList<>()).build();

		when(sessionRepoMock.findById(sessionID)).thenReturn(Optional.of(session));

		SessionService sessionService = new SessionService(sessionRepoMock, userRepoMock);

		assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(sessionID, userID));
	}

	@Test
	public void testNoLongerParticipateNotFoundException() {
		Long sessionID = 1L;
		Long userID = 2L;
		when(sessionRepoMock.findById(sessionID)).thenReturn(Optional.empty());
		SessionService sessionService = new SessionService(sessionRepoMock, userRepoMock);

		assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(sessionID, userID));
		verify(sessionRepoMock, times(1)).findById(sessionID);
		verify(sessionRepoMock, times(0)).save(any(Session.class));
	}

}
