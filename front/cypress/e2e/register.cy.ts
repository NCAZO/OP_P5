/// <reference types="cypress" />

describe('Register spec', () => {

    //BON
    it('Register successfull', () => {
        cy.visit('/register')

        cy.intercept('POST', '/api/auth/register', { statusCode: 200 })

        cy.get('input[formControlName=firstName]').type("prenom")
        cy.get('input[formControlName=lastName]').type("nom")
        cy.get('input[formControlName=email]').type("prenom.nom@test.com")
        cy.get('input[formControlName=password]').type(`${"password"}{enter}{enter}`)

        cy.url().should('include', '/login')
    })

    //BON
    it('return error if email already used', () => {
        cy.visit('/register')

        cy.intercept('POST', '/api/auth/register', { statusCode: 400 })

        cy.get('input[formControlName=firstName]').type("prenom")
        cy.get('input[formControlName=lastName]').type("nom")
        cy.get('input[formControlName=email]').type("prenom.nom@test.com")
        cy.get('input[formControlName=password]').type(`${"password"}{enter}{enter}`)
        cy.get('.error').should('be.visible');
    })

    //BON
    it('disable submit btn if empty email', () => {
        cy.visit('/register')

        cy.get('input[formControlName=firstName]').type("prenom")
        cy.get('input[formControlName=lastName]').type("nom")
        cy.get('input[formControlName=email]').clear
        cy.get('input[formControlName=password]').type(`${"password"}{enter}{enter}`)
        cy.get('input[formControlName=email]').should('have.class', 'ng-invalid')
        cy.get('button[type=submit]').should('be.disabled')
    })

    //BON
    it('disable submit btn if empty password', () => {
        cy.visit('/register')

        cy.get('input[formControlName=firstName]').type("prenom")
        cy.get('input[formControlName=lastName]').type("nom")
        cy.get('input[formControlName=email]').type("prenom.nom@test.com")
        cy.get('input[formControlName=password]').clear
        cy.get('input[formControlName=password').should('have.class', 'ng-invalid')
        cy.get('button[type=submit]').should('be.disabled')
    })

    //BON
    it('disable submit btn if empty firstname', () => {
        cy.visit('/register')

        cy.get('input[formControlName=firstName]').clear
        cy.get('input[formControlName=lastName]').type("nom")
        cy.get('input[formControlName=email]').type("prenom.nom@test.com")
        cy.get('input[formControlName=password]').type(`${"password"}{enter}{enter}`)
        cy.get('input[formControlName=firstName').should('have.class', 'ng-invalid')
        cy.get('button[type=submit]').should('be.disabled')
    })

    //BON
    it('disable submit btn if empty lastname', () => {
        cy.visit('/register')

        cy.get('input[formControlName=firstName]').type("prenom")
        cy.get('input[formControlName=lastName]').clear
        cy.get('input[formControlName=email]').type("prenom.nom@test.com")
        cy.get('input[formControlName=password]').type(`${"password"}{enter}{enter}`)
        cy.get('input[formControlName=lastName').should('have.class', 'ng-invalid')
        cy.get('button[type=submit]').should('be.disabled')
    })
})