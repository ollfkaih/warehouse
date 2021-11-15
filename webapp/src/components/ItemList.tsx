import React, { useEffect, useState } from 'react'
import ItemListElement from './ItemListElement'
import Item from '../modules/Item'
import { Col, Container, Row } from 'react-bootstrap'
import SortOption from '../modules/SortOption'
import useSWR from 'swr'

const GET_ALL_ITEMS_ENDPOINT = '/warehouse/items'

interface IProps {
  domain: string
  currentItem?: Item
  setCurrentItem: (item: Item) => void
  searchText: string
  sortOption: SortOption
  sortAscendingOrder: boolean
}

const ItemList = (props: IProps) => {
  const getItems = async (url: string) => fetch(url).then((res) => res.json())
  const { data, error } = useSWR(props.domain + GET_ALL_ITEMS_ENDPOINT, getItems)
  const [items, setItems] = useState<[Item]>(data)
  // eslint-disable-next-line
  const [newItem, setNewItem] = useState()

  useEffect(() => {
    setItems(data)
  }, [data])

  if (error) return <p>Error</p>
  if (!data) return <p>Loading</p>
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
