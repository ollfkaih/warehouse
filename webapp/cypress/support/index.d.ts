/// <reference types="cypress" />

declare namespace Cypress {
  interface Chainable<Subject> {
    /**
     * Logs in to the web ui using the user fixture
     */
    login(): void
    register(): void
    /**
     * Create an item with name and amount
     * @example
     * cy.createItem('name', 0)
     */
    createItem(name: string, amount: number): void //Chainable<any>
    /**
     * Overwrites all properties of an item
     * @example
     * cy.editItem(item)
     */
    editItem(item: any): void //I havent figured out how to import item wo breaking commands.ts, but item: Item is specified in commands.ts
    deleteItem(item: any): void
    deleteItemByName(itemName: string): void
    verifyItemProperties(item: any): void
    createDefaultItems(): void
    deleteDefaultItems(): void
  }
}
