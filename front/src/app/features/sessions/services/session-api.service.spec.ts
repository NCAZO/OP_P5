import { HttpClientModule } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';

describe('SessionsApiService', () => {
  let sessionAPIService: SessionApiService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule,
        HttpClientTestingModule
      ]
    });
    sessionAPIService = TestBed.inject(SessionApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });
  
  it('should be created', () => {
    expect(sessionAPIService).toBeTruthy();
  });

  it('should display all sessions', () => {
    const mockSessions: Session[] = [
      { id: 1, name: 'Session1', date: new Date(), teacher_id: 1, description: 'Description1', users: []},
      { id: 2, name: 'Session2', date: new Date(), teacher_id: 2, description: 'Description2', users: []},
      { id: 3, name: 'Session3', date: new Date(), teacher_id: 1, description: 'Description3', users: []},
      { id: 4, name: 'Session4', date: new Date(), teacher_id: 2, description: 'Description4', users: []},
    ];

    sessionAPIService.all().subscribe(sessions => {
      expect(sessions.length).toBe(4);
      expect(sessions).toEqual(mockSessions);
    });

    const req = httpMock.expectOne('api/session');
    expect(req.request.method).toBe('GET');
    req.flush(mockSessions);
  });

  it('should display session by Id', () => {
    const mockSession: Session = {
      id: 1,
      name: 'Session1',
      date: new Date(),
      teacher_id: 1,
      description: 'Description1',
      users: []
    };

    sessionAPIService.detail('1').subscribe(session => {
      expect(session).toEqual(mockSession);
    });

    const req = httpMock.expectOne('api/session/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockSession);
  });

  it('should delete session by Id', () => {
    const sessionId = '1';

    sessionAPIService.delete(sessionId).subscribe();

    const req = httpMock.expectOne(`api/session/${sessionId}`);
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });

  it('should create new session', () => {
    const newSession: Session = {
      id: 1,
      name: 'New Session',
      date: new Date(),
      teacher_id: 1,
      description: 'New Description',
      users: []
    };

    sessionAPIService.create(newSession).subscribe(session => {
      expect(session).toEqual(newSession);
    });

    const req = httpMock.expectOne('api/session');
    expect(req.request.method).toBe('POST');
    req.flush(newSession);
  });

  it('should update existing session', () => {
    const updatedSession: Session = {
      id: 1,
      name: 'Updated Session',
      date: new Date(),
      teacher_id: 1,
      description: 'Updated Description',
      users: []
    };

    sessionAPIService.update('1', updatedSession).subscribe(session => {
      expect(session).toEqual(updatedSession);
    });

    const req = httpMock.expectOne('api/session/1');
    expect(req.request.method).toBe('PUT');
    req.flush(updatedSession);
  });

  it('should autorize user to participate in a session', () => {
    const sessionId = '1';
    const userId = '1';

    sessionAPIService.participate(sessionId, userId).subscribe();

    const req = httpMock.expectOne(`api/session/${sessionId}/participate/${userId}`);
    expect(req.request.method).toBe('POST');
    req.flush({});
  });

  it('should autorize user to unparticipate in a session', () => {
    const sessionId = '1';
    const userId = '1';

    sessionAPIService.unParticipate(sessionId, userId).subscribe();

    const req = httpMock.expectOne(`api/session/${sessionId}/participate/${userId}`);
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });
});