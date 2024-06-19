import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { Teacher } from '../interfaces/teacher.interface';

import { TeacherService } from './teacher.service';

describe('TeacherService', () => {
  let teacherService: TeacherService;

  const mockTeachers: Teacher[] = [
    {
      id: 1,
      lastName: 'lastname1',
      firstName: 'firstname1',
      createdAt: new Date(),
      updatedAt: new Date()
    },
    {
      id: 2,
      lastName: 'lastname2',
      firstName: 'firstname2',
      createdAt: new Date(),
      updatedAt: new Date()
    }
  ]

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule
      ]
    });
    teacherService = TestBed.inject(TeacherService);
  });

  it('should get all teachers', () => {
    teacherService.all().subscribe(teachers => {
      expect(teachers).toEqual(mockTeachers);
    });
  });

  it('should get teacher by id', () => {
    teacherService.detail('1').subscribe(teacher => {
      expect(teacher).toEqual(mockTeachers[0]);
    });
  });
});