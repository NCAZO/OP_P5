import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';

describe('SessionService', () => {
  let sessionService: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    sessionService = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(sessionService).toBeTruthy();
  });

  it('should be connected', () => {
    const user = {
      token: 'token',
      type: 'test',
      username: 'username',
      firstName: 'firstname',
      lastName: 'lastname',
      id: 1,
      admin: false,
    };

    sessionService.logIn(user);
    sessionService.sessionInformation = user;

    expect(sessionService.isLogged).toBe(true);
  });

  it('should be disconnected', () => {
    sessionService.logOut();
    const user = {
      token: 'token',
      type: 'test',
      username: 'username',
      firstName: 'firstname',
      lastName: 'lastname',
      id: 1,
      admin: false,
    };

    sessionService.logIn(user);
    expect(sessionService.isLogged).toBe(true);
    sessionService.logOut();

    expect(sessionService.isLogged).toBe(false);
    expect(sessionService.sessionInformation).toBe(undefined);
  });

  it('should be observable', () => {
    expect(sessionService.$isLogged).toBeTruthy();
  })
});
