/// <reference types="cypress" />

//BON
describe('Login spec', () => {

  //BON
  it('Login successfull', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')
  });

  //BON
  it('login failed if bad credential', () => {

    cy.visit('/login');
    cy.get('input[formControlName=email]').type("unknow@test.com");
    cy.get('input[formControlName=password]').type(`${"password"}{enter}{enter}`);
    cy.url().should('not.contain', '/sessions');
    cy.get('.error').should('be.visible');
  });

  //BON
  it('disable submit btn if no email', () => {
    cy.visit('/login')

    cy.get('input[formControlName=email]').clear;
    cy.get('input[formControlName=password]').type(`${"password"}{enter}{enter}`);
    cy.get('input[formControlName=email]').should('have.class', 'ng-invalid');
    cy.get('.error').should('be.visible');
    cy.get('button[type=submit]').should('be.disabled');
  });

  //BON
  it('disable submit btn if no password', () => {
    cy.visit('/login')

    cy.get('input[formControlName=password]').clear;
    cy.get('input[formControlName=email]').type(`${"yoga@studio.com"}{enter}{enter}`);
    cy.get('input[formControlName=password]').should('have.class', 'ng-invalid');
    cy.get('.error').should('be.visible');
    cy.get('button[type=submit]').should('be.disabled');
  });
});

//BON
describe('logOut', () => {
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
  //BON
  it('Logout successfull', () => {
    cy.url().should('include', '/sessions');
    cy.get('.mat-toolbar > .ng-star-inserted > :nth-child(3)').click();
    cy.url().should('not.contain', '/sessions');
  });
})