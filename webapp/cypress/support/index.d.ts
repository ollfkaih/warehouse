/// <reference types="cypress" />

declare namespace Cypress {
  interface Chainable<Subject> {
    /**
     * Logs in to the web ui using the user fixture.
     */
    login(): void

    /**
     * Sends a request to webserver to create a new user with random username.
     */
    register(): void

    /**
     * Create an item with name and amount.
     * @param name The name of the item
     * @param amount The amount of the item (in the warehouse)
     */
    createItem(name: string, amount: number): void //Chainable<any>

    /**
     * Overwrites all properties of an item.
     * @param item the item to overwrite
     */
    editItem(item: any): void //I havent figured out how to import item wo breaking commands.ts, but item: Item is specified in commands.ts

    /**
     * Deletes the item.
     * @param item to delete
     */
    deleteItem(item: any): void

    /**
     * Deletes item with a specified name.
     * @param itemName to delete
     */
    deleteItemByName(itemName: string): void

    /**
     * Verifies that the item values in ui corresponds to the items actual values.
     * @param item to verify
     */
    verifyItemProperties(item: any): void
  }
}
