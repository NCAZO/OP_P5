import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';
import { expect } from '@jest/globals';

import { MeComponent } from './me.component';

describe('MeComponent', () => {
  let meComponent: MeComponent;
  let fixture: ComponentFixture<MeComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [{ provide: SessionService, useValue: mockSessionService }],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    meComponent = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create component', () => {
    expect(meComponent).toBeTruthy();
  }
  );

  it('should call back', () => {
    const spy = jest.spyOn(window.history, 'back');
    meComponent.back();
    expect(spy).toHaveBeenCalled();
  });

  it('should call delete', () => {
    const spy = jest.spyOn(window.history, 'back');
    meComponent.delete();
    expect(spy).toHaveBeenCalled();
  });

  // it('should call ngOnInit', () => {
  //   const spy = jest.spyOn(meComponent.userService, 'getById');
  //   meComponent.ngOnInit();
  //   expect(spy).toHaveBeenCalled();
  // });
});