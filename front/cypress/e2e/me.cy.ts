/// <reference types="cypress" />

describe('User account', () => {
  beforeEach(() => {
    cy.visit('/login')
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'yoga@studio.com',
        firstName: 'prenom',
        lastName: 'nom',
        admin: true
      },
    });

    cy.get('input[formControlName=email]').type("yoga@studio.com");
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);
  })

  it('display info user', () => {

    const currentDate = new Date().toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });

    cy.intercept('GET', '/api/user/1', {
      body: [{
              id: 1,
              email: 'user@studio.com',
              lastName: 'lastName',
              firstName: 'firstName',
              admin: false,
              createdAt: '2023-04-10T00:00:00',
              updatedAt: '2023-04-10T00:00:00',
      }]
    }).as('user information')
    
    cy.get('.mat-toolbar > .ng-star-inserted > :nth-child(2)').click();
    cy.url().should('include', '/me')
    cy.get('.my2 > .mat-focus-indicator').should('exist')

    

    // cy.get('.mat-card-content > div.ng-star-inserted > :nth-child(1)').should('contain', 'Admin ADMIN')
    // cy.get('.mat-card-content > div.ng-star-inserted > :nth-child(2)').should('contain', 'yoga@studio.com')

    // cy.get('.mat-card-content > div.ng-star-inserted > :nth-child(3)').should('contain', 'admin')

    // cy.get('.p2 :nth-child(1)').should('contain', currentDate)
    // cy.get('.p2 :nth-child(2)').should('contain', currentDate)

  })
})