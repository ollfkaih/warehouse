/// <reference types="cypress" />

describe('Warehouse', () => {
  it('should be able to create a new item', () => {
    const item = {
      name: 'ProduktNavn',
      amount: 565646565,
      barcode: 1234567890128,
      brand: 'Produsent',
      regularPrice: 7500,
      salePrice: 6000,
      purchasePrice: 5000,
      section: 'AD',
      row: 'BC',
      shelf: '16',
      weight: 10,
      height: 50,
      width: 60,
      length: 15,
    }
    cy.visit('http://localhost:3000')
    cy.get('#addNewItemButton').click()
    cy.get('#brand-control').type(item.brand)
    cy.get('#name-control').type(item.name)
    cy.get('#section-control').type(item.section)
    cy.get('#row-control').type(item.row)
    cy.get('#shelf-control').type(item.shelf)
    cy.get('#amount-control').type(item.amount.toString())
    cy.get('#regularPrice-control').type(item.regularPrice.toString())
    cy.get('#salePrice-control').type(item.salePrice.toString())
    cy.get('#purchasePrice-control').type(item.purchasePrice.toString())
    cy.get('#length-control').type(item.length.toString())
    cy.get('#width-control').type(item.width.toString())
    cy.get('#height-control').type(item.height.toString())
    cy.get('#weight-control').type(item.weight.toString())
    cy.get('#barcode-control').type(item.barcode.toString())
    cy.get('#save-button').click()
    cy.get('#back-button').click()

    // verify that issue has been created
    cy.contains(item.name).should('be.visible')
    cy.contains(item.name).click()
    cy.get('#delete-button').click()
    /*if (!nameAlreadyExists)*/ cy.contains(item.name).should('not.exist')
  })
})
