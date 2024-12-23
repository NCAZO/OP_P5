import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { of } from 'rxjs';
import { SessionService } from '../../../../services/session.service';
import { TeacherService } from '../../../../services/teacher.service';
import { Session } from '../../interfaces/session.interface';
import { SessionApiService } from '../../services/session-api.service';
import { DetailComponent } from './detail.component';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { Teacher } from 'src/app/interfaces/teacher.interface';

// Définition du bloc de test pour le composant DetailComponent
describe('DetailComponent', () => {

  // Simulation des données nécessaires pour les tests
  const mockSessionInformation: SessionInformation = { token: '', type: '', id: 1, username: '', firstName: '', lastName: '', admin: true };
  const mockTeacher: Teacher = { id: 1, lastName: 'Doe', firstName: 'John', createdAt: new Date(), updatedAt: new Date() };

  // Variables utilisées dans les tests
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let mockRouter: jest.Mocked<Router>;
  let mockMatSnackBar: jest.Mocked<MatSnackBar>;
  let mockActivatedRoute: jest.Mocked<ActivatedRoute>;
  let mockSessionApiService: jest.Mocked<SessionApiService>;
  let mockSessionService: jest.Mocked<SessionService>;
  let mockTeacherService: jest.Mocked<TeacherService>;
  let mockSession: jest.Mocked<Session>;

  // Initialisation avant chaque test
  beforeEach(async () => {

    // Création de la simulation d'une session
    mockSession = {
      id: 1,
      users: [0],
      teacher_id: 1
    } as unknown as jest.Mocked<Session>;

    // Simulations des services et des modules requis
    mockRouter = {
      navigate: jest.fn() // Simulation de la navigation
    } as unknown as jest.Mocked<Router>;

    mockMatSnackBar = {
      open: jest.fn() // Simulation de l'ouverture de la snackbar
    } as unknown as jest.Mocked<MatSnackBar>;

    mockSessionService = {
      sessionInformation: mockSessionInformation // Simulation de la récupération des informations de session
    } as unknown as jest.Mocked<SessionService>;

    mockSessionApiService = {
      delete: jest.fn(), // Simulation de la suppression de session
      participate: jest.fn(), // Simulation de la participation à une session
      unParticipate: jest.fn(), // Simulation de l'annulation de participation
      detail: jest.fn().mockReturnValue(of(mockSession)) // Simulation de la récupération des détails de session
    } as unknown as jest.Mocked<SessionApiService>;

    mockTeacherService = {
      detail: jest.fn(), // Simulation de la récupération des détails du professeur
    } as unknown as jest.Mocked<TeacherService>;

    mockActivatedRoute = {
      snapshot: {
        paramMap: {
          get: jest.fn().mockReturnValue('1') // Simulation de la récupération du paramètre 'id' dans l'URL
        },
      },
    } as unknown as jest.Mocked<ActivatedRoute>;

    // Configuration du module de test avec les modules, déclarations et services simulés
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        MatCardModule,
        MatIconModule
      ],
      declarations: [DetailComponent],
      providers: [
        FormBuilder,
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: Router, useValue: mockRouter },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: SessionService, useValue: mockSessionService },
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
      ],
    }).compileComponents();

    // Création de l'instance du composant à tester
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges(); // Déclenche la détection de changement pour initialiser les données

  });

  // Test pour vérifier que le composant est bien créé
  it('should create', () => {
    expect(component).toBeTruthy(); // Vérifie que le composant a été créé avec succès
  });

  // Bloc de test pour la fonction back
  describe('back', () => {
    it('should call windows.history.back()', () => {
      const backSpy = jest.spyOn(window.history, 'back'); // Espionne l'appel à history.back()

      component.back(); // Appel de la méthode back
      expect(backSpy).toHaveBeenCalledTimes(1); // Vérifie que la méthode history.back() a été appelée une fois
    });
  });

  // Bloc de test pour la méthode ngOnInit
  describe('ngOnInit', () => {
    it('should call sessionApiService.detail and teacherService.detail on ngOnInit', () => {
      // Espionne l'appel à la méthode detail de sessionApiService et teacherService
      const SessionApiServiceSpy = jest.spyOn(mockSessionApiService, 'detail').mockReturnValue(of(mockSession));
      const TeacherServiceSpy = jest.spyOn(mockTeacherService, 'detail').mockReturnValue(of(mockTeacher));

      component.ngOnInit(); // Appelle la méthode ngOnInit du composant
      expect(SessionApiServiceSpy).toHaveBeenCalledWith('1'); // Vérifie que sessionApiService.detail a été appelé avec l'ID '1'
      expect(TeacherServiceSpy).toHaveBeenCalledWith('1'); // Vérifie que teacherService.detail a été appelé avec l'ID '1'
      expect(component.session).toEqual(mockSession); // Vérifie que la session récupérée est correcte
      expect(component.teacher).toEqual(mockTeacher); // Vérifie que le professeur récupéré est correct
      expect(component.isParticipate).toBeFalsy(); // Vérifie que isParticipate est défini à false après l'initialisation
    });
  });

  // Bloc de test pour la méthode delete
  describe('delete', () => {
    it('should call sessionApiService.delete and router.navigate on delete', () => {
      // Espionne l'appel à la méthode delete de sessionApiService
      const sessionApiServiceSpy = jest.spyOn(mockSessionApiService, 'delete').mockReturnValue(of(null));

      component.delete(); // Appelle la méthode delete
      expect(sessionApiServiceSpy).toHaveBeenCalledWith('1'); // Vérifie que la méthode delete est appelée avec l'ID '1'
      expect(mockMatSnackBar.open).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 }); // Vérifie que la snackbar affiche le message de succès
      expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']); // Vérifie que la navigation est redirigée vers 'sessions'
    });
  });

  // Bloc de test pour la méthode participate
  describe('participate', () => {
    it('should call sessionApiService.participate on participate', () => {
      // Espionne l'appel à la méthode participate de sessionApiService
      const participeSpy = jest.spyOn(mockSessionApiService, 'participate').mockReturnValue(of(void 0));

      component.participate(); // Appelle la méthode participate
      expect(participeSpy).toHaveBeenCalledWith(mockSession.id?.toString(), mockSessionInformation.id.toString()); // Vérifie que participate est appelée avec les bons IDs
    });
  });

  // Bloc de test pour la méthode unParticipate
  describe('unParticipate', () => {
    it('should call sessionApiService.unParticipate on unParticipate', () => {
      // Espionne l'appel à la méthode unParticipate de sessionApiService
      const unParticipeSpy = jest.spyOn(mockSessionApiService, 'unParticipate').mockReturnValue(of(void 0));

      component.unParticipate(); // Appelle la méthode unParticipate
      expect(unParticipeSpy).toHaveBeenCalledWith(mockSession.id?.toString(), mockSessionInformation.id.toString()); // Vérifie que unParticipate est appelée avec les bons IDs
    });
  });

});
