import Item from '../../src/modules/Item'

Cypress.Commands.add('createItem', (name: string, amount: number) => {
  cy.visit('http://localhost:3000')
  cy.get('#addNewItemButton').click()
  cy.get('#name-control').type(name)
  cy.get('#amount-control').type(amount.toString())
  cy.get('#save-button').click()
  cy.get('#back-button').click()
})

Cypress.Commands.add('editItem', (item: Item) => {
  cy.visit('http://localhost:3000')
  cy.contains(item.name).click()
  for (const itemProperty in item) {
    if (itemProperty !== 'name')
      itemProperty &&
        cy.get('#' + itemProperty + '-control').type(item[itemProperty].toString())
  }
  cy.get('#save-button').click()
  cy.get('#back-button').click()
})

Cypress.Commands.add('deleteItem', (item: Item) => {
  cy.deleteItemByName(item.name)
})

Cypress.Commands.add('deleteItemByName', (itemName: string) => {
  cy.get('#itemList').get('#itemName').contains(itemName).click()
  cy.get('#delete-button').click()
})

Cypress.Commands.add('verifyItemProperties', (item: Item) => {
  cy.contains(item.name).click()
  for (const itemProperty in item) {
    itemProperty &&
      cy
        .get('#' + itemProperty + '-control')
        .should('have.value', item[itemProperty].toString())
  }
})

Cypress.Commands.add('createDefaultItems', () => {
  cy.createItem('A', 0)
  cy.createItem('AA', 1000000)
  cy.createItem('D', 50000)
})

Cypress.Commands.add('deleteDefaultItems', () => {
  //TODO: Do better
  /*cy.contains('Navn').click()
  //cy.get('#sortDirectionButton').click()
  cy.get('#searchField').clear()
  cy.deleteItemByName('A')
  cy.wait(500)
  cy.deleteItemByName('AA')
  cy.wait(500)
  cy.deleteItemByName('D')*/
})
