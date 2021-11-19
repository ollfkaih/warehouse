import React, { useEffect, useState } from 'react'
import ItemListElement from './ItemListElement'
import Item from '../modules/Item'
import { Col, Container, Row } from 'react-bootstrap'
import SortOption from '../modules/SortOption'

interface IProps {
  currentItem?: Item
  setCurrentItem: (item: Item) => void
  searchText: string
  sortOption: SortOption
  sortAscendingOrder: boolean
  swrData: any
  swrError: any
}

const ItemList = (props: IProps) => {
  const [items, setItems] = useState<[Item]>(props.swrData)

  useEffect(() => {
    setItems(props.swrData)
  }, [props.swrData])

  if (props.swrError) return <p>Could not load items</p>
  if (!props.swrData) return <p>Loading</p>
  if (!items) return <p>Could not load items</p>

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
      <Container fluid id="itemList" className="item-list w-auto mt-3 p-0">
        <Row className="text-primary bg-secondary text-bold rounded-1 mb-2 m-0 p-2">
          <Col>
            <strong>
              <i className="fad fa-industry"></i> Produsent
            </strong>
          </Col>
          <Col>
            <strong>
              <i className="fad fa-box"></i> Produkt
            </strong>
          </Col>
          <Col>
            <strong>
              <i className="fad fa-inventory"></i> Antall
            </strong>
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
              <ItemListElement
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
