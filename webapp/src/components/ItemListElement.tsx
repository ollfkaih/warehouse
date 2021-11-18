import { Col, Row } from 'react-bootstrap'
import Item from '../modules/Item'

interface IProps {
  item: Item
  setCurrentItem: (item: Item) => void
  selected: boolean
}

const ItemListElement = ({ item, setCurrentItem, selected }: IProps) => {
  return (
    <Row
      id={item.id}
      className={
        'mb-3 bg-secondary flex-nowrap rounded-3 bg-secondary m-0 p-2 ' +
        (selected ? 'selected' : 'item-list-element')
      }
      onClick={() => setCurrentItem(item)}
    >
      <Col id="itemBrand" className="text-truncate">
        {item.brand}
      </Col>
      <Col id="itemName" className="text-truncate">
        {item.name}
      </Col>
      <Col id="itemAmount" className="text-truncate">
        {item.amount}
      </Col>
    </Row>
  )
}
export default ItemListElement
