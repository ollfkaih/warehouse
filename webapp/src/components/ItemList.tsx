import React, { useState } from 'react'
import ItemElement from './ItemElement'
import Item from '../modules/Item'
import warehouseItems from '../warehouse.json'
import { Col, Container, Row } from 'react-bootstrap'
import SortOption from '../modules/SortOption'

interface IProps {
  currentItem?: Item
  setCurrentItem: (item: Item) => void
  searchText: string
  sortOption: SortOption
  sortAscendingOrder: boolean
}

const ItemList = (props: IProps) => {
  // eslint-disable-next-line
  const [items, setItems] = useState(
    warehouseItems.map((item) => ({
      ...item,
      creationDate: new Date(item.creationDate),
    }))
  )
  // eslint-disable-next-line
  const [newItem, setNewItem] = useState()

  const sortingComparator = (item1: Item, item2: Item, ascending: boolean): number => {
    const ascendingFactor = ascending ? 1 : -1
    switch (props.sortOption) {
      case SortOption.Brand: {
        if (item2.brand && item1.brand)
          return (
            ascendingFactor *
            item1.brand.localeCompare(item2.brand, undefined, {
              caseFirst: 'upper',
            })
          )
        if (item1.brand) return -1
        return 0
      }
      case SortOption.Price: {
        if (item1.regularPrice) {
          if (item2.regularPrice)
            return ascendingFactor * (item1.regularPrice - item2.regularPrice)
          return -1
        } else {
          if (item2.regularPrice) return 1
        }
        return 0
      }
      case SortOption.Amount: {
        return ascendingFactor * (item1.amount - item2.amount)
      }
      case SortOption.Name: {
        return ascendingFactor * item1.name.localeCompare(item2.name)
      }
      case SortOption.Date: {
        if (item1.creationDate === item2.creationDate) return 0
        return ascendingFactor * (item1.creationDate < item2.creationDate ? 1 : -1)
      }
      default: {
        return 0
      }
    }
  }

  const renderItems = (ascending: boolean) => {
    return (
      <Container fluid className="item-list w-auto p-0">
        <Row className="text-primary bg-secondary text-bold rounded-1 mb-1 m-0 p-1">
          <Col>
            <strong>Produsent</strong>
          </Col>
          <Col>
            <strong>Produkt</strong>
          </Col>
          <Col>
            <strong>Antall</strong>
          </Col>
        </Row>
        <Container fluid className="overflow-auto w-auto item-list m-0 p-0">
          {items
            .filter(
              (item) =>
                item.name.toLowerCase().match(props.searchText.toLowerCase()) ||
                item.brand?.toLowerCase().match(props.searchText.toLowerCase()) ||
                item.barcode?.toString() === props.searchText
            )
            .sort((item1, item2) => sortingComparator(item1, item2, ascending))
            .map((item) => (
              <ItemElement
                key={item.id}
                item={item}
                selected={props.currentItem === item ? true : false}
                setCurrentItem={props.setCurrentItem}
              />
            ))}
        </Container>
      </Container>
    )
  }

  return <>{renderItems(props.sortAscendingOrder)}</>
}
export default ItemList
