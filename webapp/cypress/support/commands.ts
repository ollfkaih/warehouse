import Item from '../../src/modules/Item'
import { v4 as uuidv4 } from 'uuid'

const userName = uuidv4()
const id = uuidv4()
const password = 'pwd'

Cypress.Commands.add('login', () => {
  cy.visit('http://localhost:3000')
  cy.contains('Logg inn').click()
  cy.get('#username-control').type(userName)
  cy.get('#password-control').type(password)
  cy.get('#login-button-modal').click()
  cy.get('#logout-btn').should('be.visible')
})

Cypress.Commands.add('register', () => {
  cy.request({
    method: 'POST',
    url: 'localhost:8080/warehouse/user/register',
    body: {
      id: id,
      userName: userName,
      password: password,
    },
  })
})

Cypress.Commands.add('createItem', (name: string, amount: number) => {
  cy.get('#addNewItemButton').click()
  cy.get('#name-control').type(name)
  cy.get('#amount-control').type(amount.toString())
  cy.get('#save-button').click()
  cy.get('#back-button').click()
})

Cypress.Commands.add('editItem', (item: Item) => {
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
