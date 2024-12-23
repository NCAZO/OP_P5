/// <reference types="cypress" />
beforeEach(() => {
  // Interception pour la connexion admin
  cy.intercept(
    {
      method: 'POST',
      url: '/api/auth/login',
    },
    {
      statusCode: 200,
      body: {
        id: 1,
        username: 'yoga@studio.com',
        firstName: 'prenom',
        lastName: 'nom',
        admin: true
      },
    }
  ).as('loginAdmin');

  // Interception pour la récupération des sessions
  cy.intercept(
    {
      method: 'GET',
      url: '/api/session',
    },
    {
      statusCode: 200,
      body: [
        {
          id: 1,
          name: 'Rental 1',
          description: 'Description du Rental 1',
          date: '2024-02-29',
        },
        {
          id: 2,
          name: 'Rental 2',
          description: 'Description du Rental 2',
          date: '2024-03-01',
        },
      ],
    }
  ).as('getSessions');

  // Effectuer le login
  cy.visit('/login');
  cy.get('input[formControlName=email]').type('yoga@studio.com');
  cy.get('input[formControlName=password]').type('test!1234{enter}{enter}');

  // Attendre la complétion des requêtes
  cy.wait('@loginAdmin');
  cy.wait('@getSessions');
});
// Tests sur les sessions
describe('Session Management', () => {
  it('should display available sessions', () => {
    cy.get('.items > .item').should('have.length.at.least', 1);
  });

  it('should display Create/Detail/Edit buttons if user is admin', () => {
    cy.get('#btnCreateSession').should('be.visible');
    cy.get('#btnDetailSession').should('be.visible');
    cy.get('#btnUpdateSession').should('be.visible');
  });
});

// Tests d'information sur une session
describe('Session Info', () => {
  beforeEach(() => {
    cy.intercept(
      {
        method: 'GET',
        url: '/api/session/1',
      },
      {
        id: 1,
        name: 'Nouvelle session Test',
        date: '2024-02-29',
        teacher_id: 1,
        description: 'Session de découverte',
        users: [],
        createdAt: '2024-02-29',
        updatedAt: '2024-02-29',
      }
    ).as('sessionInfo1');

    cy.intercept(
      {
        method: 'GET',
        url: '/api/teacher',
      },
      [
        {
          id: 1,
          lastName: 'DELAHAYE',
          firstName: 'Margot',
          createdAt: '2024-01-01',
          updatedAt: '2024-01-01',
        },
        {
          id: 2,
          lastName: 'THIERCELIN',
          firstName: 'Hélène',
          createdAt: '2024-02-02',
          updatedAt: '2024-02-02',
        },
      ]
    ).as('teachers');
  });

  it('should display session details', () => {
    cy.get('#btnDetailSession').click();
    cy.url().should('include', '/sessions/detail/1');
    cy.get('h1').should('contain.text', 'Nouvelle Session Test');
    cy.get(':nth-child(2) > .ml1').should('contain.text', 'February 29, 2024');
    cy.get('.description').should('contain.text', 'Session de découverte');
  });

  it('should display delete button if user is admin', () => {
    cy.get('#btnDetailSession').click();
    cy.get('#deleteSession').should('be.visible');
  });
});

// Création de session
describe('Create Session', () => {
  beforeEach(() => {
    cy.intercept(
      {
        method: 'GET',
        url: '/api/teacher',
      },
      [
        {
          id: 1,
          lastName: 'DELAHAYE',
          firstName: 'Margot',
        },
        {
          id: 2,
          lastName: 'THIERCELIN',
          firstName: 'Hélène',
        },
      ]
    ).as('teachers');
  });

  it('should create a new session', () => {
    cy.get('button[routerLink="create"]').click();
    cy.url().should('include', '/sessions/create');
    cy.get('input[formControlName=name]').type('Nouvelle session Test');
    cy.get('input[formControlName=date]').type('2024-02-29');
    cy.get('mat-select').click();
    cy.get('mat-option').contains('Margot DELAHAYE').click();
    cy.get('textarea[formControlName=description]').type('Session de découverte');
    cy.get('button[type=submit]').click();
    cy.url().should('include', '/sessions');
  });

  it('should validate required fields', () => {
    cy.get('button[routerLink="create"]').click();
    cy.url().should('include', '/sessions/create');
    cy.get('button[type=submit]').should('be.disabled');
  });
});

describe('Delete Session', () => {
  beforeEach(() => {
    cy.intercept(
      {
        method: 'GET',
        url: '/api/session/1',
      },
      {
        id: 1,
        name: 'Nouvelle session Test',
        date: '2024-02-29',
        teacher_id: 1,
        description: 'Session de découverte',
        users: [],
        createdAt: '2024-02-29',
        updatedAt: '2024-02-29',
      }
    ).as('sessionInfo1');

    cy.intercept(
      {
        method: 'GET',
        url: '/api/teacher',
      },
      [
        {
          id: 1,
          lastName: 'DELAHAYE',
          firstName: 'Margot',
          createdAt: '2024-01-01',
          updatedAt: '2024-01-01',
        },
        {
          id: 2,
          lastName: 'THIERCELIN',
          firstName: 'Hélène',
          createdAt: '2024-02-02',
          updatedAt: '2024-02-02',
        },
      ]
    ).as('teachers');

    cy.intercept(
      {
        method: 'DELETE',
        url: '/api/session/1',
      },
      {
        statusCode: 200,
      }
    ).as('deleteSession');

    cy.intercept('GET', '/api/session', [
      {
        id: 2,
        name: 'Ancienne session',
        date: '2024-01-15',
        teacher_id: 2,
        description: 'Session d\'initiation',
        users: [],
        createdAt: '2024-01-10',
        updatedAt: '2024-01-12',
      },
    ]).as('sessionsAfterDelete');
  });

  it('should delete a session after clicking detail and delete', () => {
    // Cliquer sur le bouton "Détails" de la première session
    cy.get('button#btnDetailSession').first().click();

    // Cliquer sur le bouton "Supprimer" dans la page des détails
    cy.get('button#deleteSession').click();

    // Attendre que la suppression soit effectuée
    cy.wait('@deleteSession');

    // Vérifier que la liste des sessions a été rafraîchie
    cy.wait('@sessionsAfterDelete');

    // Vérifier que la session a bien été supprimée
    cy.get('.items > .item').should('have.length', 1); // Ajuster selon le nombre d'items restants
  });
});
