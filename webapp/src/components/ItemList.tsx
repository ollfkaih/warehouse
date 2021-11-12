import ItemElement from './ItemElement'
import Item from '../modules/Item'
import warehouseItems from '../warehouse.json'
import React, { useState } from 'react'
import { Col, Container, Form, InputGroup, Row } from 'react-bootstrap'

interface IProps {
  setCurrentItem: (item: Item) => void
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
  const [searchText, setSearchText] = useState('')

  const handleSearch = (e: React.ChangeEvent<HTMLInputElement>) =>
    setSearchText(e.target.value)

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
          .filter((item) =>
            item.name.toLowerCase().match(searchText.toLowerCase())
          )
          .map((item) => (
            <ItemElement
              key={item.id}
              item={item}
              setCurrentItem={props.setCurrentItem}
            />
          ))}
      </Container>
    </Container>
  )

  return (
    <>
      <InputGroup>
        <InputGroup.Text className="p-0 mb-2 container-fluid border-white placeholder-primary">
          <Form.Control
            className="bg-light text-primary form-control"
            type="text"
            placeholder="Søk..."
            value={searchText}
            onChange={handleSearch}
          />
        </InputGroup.Text>
      </InputGroup>

      {renderItems()}
    </>
  )
}
export default ItemList
