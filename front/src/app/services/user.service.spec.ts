import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { User } from '../interfaces/user.interface';

import { UserService } from './user.service';

describe('UserService', () => {
  let userService: UserService;
  const mockUsers: User[] = [
    {
      id: 1,
      firstName: 'firstname',
      lastName: 'lastname',
      email: 'fn.ln@gmail.com',
      password: '123456789',
      admin: false,
      createdAt: new Date(),
      updatedAt: new Date()
    },]

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule
      ]
    });
    userService = TestBed.inject(UserService);
  });

  it('should get user by Id', () => {
    userService.getById('1').subscribe(user => {
      expect(user).toEqual(mockUsers[0]);
    });
  });

  it('should delete user by Id', () => {
    userService.delete('1').subscribe(user => {
      expect(user).toEqual(mockUsers[0]);
    });

    userService.getById('1').subscribe(user => {
      expect(user).toBeUndefined();
    });
  });
});