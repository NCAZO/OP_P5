/// <reference types="cypress" />

//BON
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
describe('session', () => {
    beforeEach(() => {
        cy.intercept(
            {
                method: 'GET',
                url: '/api/session',
            },
            [
                {
                    id: 1,
                    name: 'Nouvelle session Test',
                    date: '2024-02-29',
                    teacher_id: 1,
                    description: 'Session de découverte',
                    users: [],
                    createdAt: '2024-02-29',
                    updatedAt: '2024-02-29',
                },
    
                {
                    id: 2,
                    name: 'Nouvelle session Test2',
                    date: '2024-02-29',
                    teacher_id: 1,
                    description: 'Session de découverte2',
                    users: [],
                    createdAt: '2024-02-29',
                    updatedAt: '2024-02-29',
                },
            ]
        ).as('session');
    })
    //BON
    it('should display sessions', () => {
        cy.get('.items > .item').should('have.length.at.least', 1);
    })

    //BON
    it('should display Create/Detail/Edit button if user is admin', () => {
        cy.get('#btnCreateSession').should('be.visible')
        cy.get('#btnDetailSession').should('be.visible')
        cy.get('#btnUpdateSession').should('be.visible')
    })
})

//BON
describe('info session', () => {

    //BON
    beforeEach(() => {
        cy.intercept(
            {
                method: 'GET',
                url: '/api/session',
            },
            [
                {
                    id: 1,
                    name: 'Nouvelle session Test',
                    date: '2024-02-29',
                    teacher_id: 1,
                    description: 'Session de découverte',
                    users: [],
                    createdAt: '2024-02-29',
                    updatedAt: '2024-02-29',
                },

                {
                    id: 2,
                    name: 'Nouvelle session Test2',
                    date: '2024-02-29',
                    teacher_id: 1,
                    description: 'Session de découverte2',
                    users: [],
                    createdAt: '2024-02-29',
                    updatedAt: '2024-02-29',
                },
            ]
        ).as('session');

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
                    createdAt: '2024-01-01 01:01:01',
                    updatedAt: '2024-01-01 01:01:02',
                },
                {
                    id: 2,
                    lastName: 'THIERCELIN',
                    firstName: 'Hélène',
                    createdAt: '2024-02-02 02:02:02',
                    updatedAt: '2024-02-02 02:02:02',
                },
            ]
        ).as('teacher');
    })

    //BON
    it('should display all session informations', () => {
        cy.get('#btnDetailSession').click()

        cy.url().should('include', '/sessions/detail/1');

        cy.get('h1').should('contain.text', 'Nouvelle Session Test');
        cy.get(':nth-child(2) > .ml1').should('contain.text', 'February 29, 2024');
        // cy.get('.ml3 > .ml1').should('contain.text', 'Hélène THIERCELIN');
        cy.get('.description').should('be.visible').contains('Session de découverte');
    })

    //BON
    it('should display delete button if user admin', () => {
        cy.get('#btnDetailSession').click()

        cy.url().should('include', '/sessions/detail/1');

        cy.get('#deleteSession').should('be.visible')
    })


})

//BON
describe('create session', () => {
    //BON
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
                    createdAt: '2024-01-01 01:01:01',
                    updatedAt: '2024-01-01 01:01:02',
                },
                {
                    id: 2,
                    lastName: 'THIERCELIN',
                    firstName: 'Hélène',
                    createdAt: '2024-02-02 02:02:02',
                    updatedAt: '2024-02-02 02:02:02',
                },
            ]
        ).as('teacher');
    })

    //BON
    it('create session', () => {
        cy.get('button[routerLink="create"]').click();

        cy.url().should('include', '/sessions/create');

        cy.get('input[formControlName=name]').type("Nouvelle session Test");
        cy.get('input[formControlName=date]').type("2024-02-29");

        cy.get('mat-select').click();
        cy.get('mat-option').contains('Margot DELAHAYE').click();

        cy.get('textarea[formControlName=description]').type("Session de découverte");

        cy.get('button[type=submit]').click();
        cy.url().should('include', '/sessions');
    })

    //BON
    it('create session required fields', () => {
        cy.get('button[routerLink="create"]').click();

        cy.url().should('include', '/sessions/create');

        cy.get('button[type=submit]').should('be.disabled');
    })
})

//BON
describe('delete session', () => {
    beforeEach(() => {
        cy.intercept(
            {
                method: 'GET',
                url: '/api/session',
            },
            [
                {
                    id: 1,
                    name: 'Nouvelle session Test',
                    date: '2024-02-29',
                    teacher_id: 1,
                    description: 'Session de découverte',
                    users: [],
                    createdAt: '2024-02-29',
                    updatedAt: '2024-02-29',
                },
            ]
        ).as('session');

        cy.intercept(
            {
                method: 'GET',
                url: '/api/session/1'
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
                url: '/api/teacher/1',
            },
            [
                {
                    id: 1,
                    lastName: 'DELAHAYE',
                    firstName: 'Margot',
                    createdAt: '2024-01-01 01:01:01',
                    updatedAt: '2024-01-01 01:01:02',
                },
            ]
        ).as('teacher');

        cy.intercept('DELETE', '/api/session/1', {
            body: {
                id: 1,
                name: 'Nouvelle session Test',
                date: '2024-02-29',
                teacher_id: 1,
                description: 'Session de découverte',
                users: [],
                createdAt: '2024-02-29',
                updatedAt: '2024-02-29',
            },
        });
    })

    it('delete session', () => {
        cy.get('#btnDetailSession').click();

        cy.get(':nth-child(2) > .mat-focus-indicator').click();
    })
})

//BON
describe('update session', () => {
    beforeEach(() => {
        cy.intercept(
            {
                method: 'GET',
                url: '/api/session',
            },
            [
                {
                    id: 1,
                    name: 'Nouvelle session Test',
                    date: '2024-02-29',
                    teacher_id: 1,
                    description: 'Session de découverte',
                    users: [],
                    createdAt: '2024-02-29',
                    updatedAt: '2024-02-29',
                },
            ]
        ).as('session');

        cy.intercept(
            {
                method: 'GET',
                url: '/api/session/1'
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
                    createdAt: '2024-01-01 01:01:01',
                    updatedAt: '2024-01-01 01:01:02',
                },
                {
                    id: 2,
                    lastName: 'THIERCELIN',
                    firstName: 'Hélène',
                    createdAt: '2024-02-02 02:02:02',
                    updatedAt: '2024-02-02 02:02:02',
                },
            ]
        ).as('teacher');

        cy.intercept('PUT', '/api/session/1', {
            body: {
                id: 1,
                name: 'Nouvelle session Modifié',
                date: '2024-03-29',
                teacher_id: 1,
                description: 'Session d apprentissage',
                users: [],
                createdAt: '2024-02-29',
                updatedAt: '2024-02-29',
            },
        });
    })

    //BON
    it('update session', () => {
        cy.get('.mat-card-actions > .ng-star-inserted').click();

        cy.url().should('include', '/sessions/update/1');

        cy.get('input[formControlName=name]').clear().type("Nouvelle session Modifié");
        cy.get('input[formControlName=date]').clear().type("2024-03-29");

        cy.get('mat-select').click();
        cy.get('mat-option').contains('Hélène THIERCELIN').click();

        cy.get('textarea[formControlName=description]').clear().type("Session d apprentissage");
        cy.get('button[type=submit]').click();


    })

    //BON
    it('update session required fields', () => {
        cy.intercept(
            {
                method: 'GET',
                url: '/api/session',
            },
            [
                {
                    id: 1,
                    name: 'Nouvelle session Test',
                    date: '2024-02-29',
                    teacher_id: 1,
                    description: 'Session de découverte',
                    users: [],
                    createdAt: '2024-02-29',
                    updatedAt: '2024-02-29',
                },
            ]
        ).as('session');

        cy.intercept(
            {
                method: 'GET',
                url: '/api/session/1'
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
                url: '/api/teacher/1',
            },
            [
                {
                    id: 1,
                    lastName: 'DELAHAYE',
                    firstName: 'Margot',
                    createdAt: '2024-01-01 01:01:01',
                    updatedAt: '2024-01-01 01:01:02',
                },
                {
                    id: 2,
                    lastName: 'THIERCELIN',
                    firstName: 'Hélène',
                    createdAt: '2024-02-02 02:02:02',
                    updatedAt: '2024-02-02 02:02:02',
                }
            ]
        ).as('teacher');

        cy.get('.mat-card-actions > .ng-star-inserted').click();

        cy.url().should('include', '/sessions/update/1');

        cy.get('input[formControlName=name]').clear();
        cy.get('input[formControlName=date]').clear();
        cy.get('button[type=submit]').should('be.disabled');
    })
})
