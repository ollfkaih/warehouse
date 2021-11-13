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

  const renderItems = () => (
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

  return <>{renderItems()}</>
}
export default ItemList
