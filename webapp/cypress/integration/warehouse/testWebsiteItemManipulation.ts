/// <reference types="cypress" />
import item from '../../fixtures/item.json'
import prodA from '../../fixtures/prodA.json'
import prodB from '../../fixtures/prodB.json'

before(() => {
  cy.register()
})

beforeEach(() => {
  cy.login()
})

describe('Warehouse', () => {
  it('should be able to create and delete a new item', () => {
    cy.viewport(1201, 800)
    cy.createItem(item.name, 0)
    cy.contains(item.name).should('be.visible')

    cy.deleteItem(item)
    cy.contains(item.name).should('not.exist')
  })
  it('should be able to edit item properties', () => {
    cy.viewport(1600, 800)
    cy.createItem(item.name, 0)
    cy.editItem(item)
    cy.verifyItemProperties(item)
    cy.deleteItem(item)
  })

  it('should be able to search for items', () => {
    cy.viewport(1201, 800)

    cy.createItem(prodA.name, 0)
    cy.editItem(prodA)

    cy.createItem(prodB.name, 0)
    cy.editItem(prodB)

    cy.get('#searchField').type(prodA.name)
    cy.get('#itemList').children().should('contain.text', prodA.name)
    cy.get('#itemList').children().should('not.contain.text', prodB.name)

    cy.get('#searchField').clear().type(prodB.name)
    cy.get('#itemList').children().should('not.contain.text', prodA.name)
    cy.get('#itemList').children().should('contain.text', prodB.name)

    cy.get('#searchField').clear().type(prodA.barcode.toString())
    cy.get('#itemList').children().should('contain.text', prodA.name)
    cy.get('#itemList').children().should('not.contain.text', prodB.name)

    cy.get('#searchField').clear().type(prodB.barcode.toString())
    cy.get('#itemList').children().should('not.contain.text', prodA.name)
    cy.get('#itemList').children().should('contain.text', prodB.name)

    cy.get('#searchField').clear().type(prodA.name)
    cy.deleteItemByName(prodA.name)
    cy.get('#searchField').clear().type(prodB.name)
    cy.deleteItemByName(prodB.name)
  })

  it('should be able to sort items', () => {
    cy.viewport(1201, 800)

    cy.createItem(prodA.name, 0)
    cy.editItem(prodA)

    cy.createItem(prodB.name, 0)
    cy.editItem(prodB)

    cy.get('label[for=brand-button]').click()
    cy.get('#itemList').then(($items) => {
      const strings = [...$items].map((item) => item.innerText)
      expect(strings).to.deep.equal([
        `${prodB.brand}\n${prodB.name}\n${prodB.amount}\n${prodA.brand}\n${prodA.name}\n${prodA.amount}`,
      ])
    })

    cy.get('label[for=brand-button]').click()
    cy.get('#itemList').then(($items) => {
      const strings = [...$items].map((item) => item.innerText)
      expect(strings).to.deep.equal([
        `${prodB.brand}\n${prodB.name}\n${prodB.amount}\n${prodA.brand}\n${prodA.name}\n${prodA.amount}`,
      ])
    })

    cy.get('label[for=name-button]').click()
    cy.get('#itemList').then(($items) => {
      const strings = [...$items].map((item) => item.innerText)
      expect(strings).to.deep.equal([
        `${prodB.brand}\n${prodB.name}\n${prodB.amount}\n${prodA.brand}\n${prodA.name}\n${prodA.amount}`,
      ])
    })

    cy.get('label[for=amount-button]').click()
    cy.get('#itemList').then(($items) => {
      const strings = [...$items].map((item) => item.innerText)
      expect(strings).to.deep.equal([
        `${prodB.brand}\n${prodB.name}\n${prodB.amount}\n${prodA.brand}\n${prodA.name}\n${prodA.amount}`,
      ])
    })

    cy.get('label[for=price-button]').click()
    cy.get('#itemList').then(($items) => {
      const strings = [...$items].map((item) => item.innerText)
      expect(strings).to.deep.equal([
        `${prodB.brand}\n${prodB.name}\n${prodB.amount}\n${prodA.brand}\n${prodA.name}\n${prodA.amount}`,
      ])
    })

    cy.get('label[for=date-button]').click()
    cy.get('#itemList').then(($items) => {
      const strings = [...$items].map((item) => item.innerText)
      expect(strings).to.deep.equal([
        `${prodA.brand}\n${prodA.name}\n${prodA.amount}\n${prodB.brand}\n${prodB.name}\n${prodB.amount}`,
      ])
    })
    cy.get('#sortDirectionButton').click()
    cy.get('#itemList').then(($items) => {
      const strings = [...$items].map((item) => item.innerText)
      expect(strings).to.deep.equal([
        `${prodB.brand}\n${prodB.name}\n${prodB.amount}\n${prodA.brand}\n${prodA.name}\n${prodA.amount}`,
      ])
    })

    cy.get('#searchField').clear().type('ProdA')
    cy.deleteItemByName('ProdA')
    cy.get('#searchField').clear().type('ProdB')
    cy.deleteItemByName('ProdB')
  })
})
