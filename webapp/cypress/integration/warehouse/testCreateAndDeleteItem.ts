/// <reference types="cypress" />
import item from '../../fixtures/item.json'
import user from '../../fixtures/user.json'

before(() => {
  cy.register()
})

beforeEach(() => {
  cy.login()
})

describe('Warehouse', () => {
  it('should be able to create and delete a new item', () => {
    cy.createItem(item.name, 0)
    cy.contains(item.name).should('be.visible')

    cy.deleteItem(item)
    cy.contains(item.name).should('not.exist')
  })
})
