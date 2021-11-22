/// <reference types="cypress" />
import item from '../../fixtures/item.json'

describe('Warehouse', () => {
  it('should be able to edit item properties', () => {
    cy.createItem(item.name, 0)
    cy.editItem(item)
    cy.verifyItemProperties(item)
    cy.deleteItem(item)
  })

  it('should be able to search for items', () => {
    //cy.createDefaultItems()
    cy.get('#searchField').type('A')
    //TODO: Verify items in list
    cy.get('#itemList').children().should('contain.text', 'A')
    /*    cy.get('#itemList').children().each((child, collection) => {
    })*/
    //cy.get('#itemList').get('#itemName').contains('A')
    cy.deleteDefaultItems()
  })
})