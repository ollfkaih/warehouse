import { Col, Row } from 'react-bootstrap'
import Item from '../modules/Item'

interface IProps {
  item: Item
  setCurrentItem: (item: Item) => void
}

const ItemElement = ({ item, setCurrentItem }: IProps) => {
  return (
    <Row
      md="12"
      className="mb-3 bg-secondary flex-nowrap rounded-3"
      onClick={() => setCurrentItem(item)}
    >
      <Col className="text-truncate">{item.brand}</Col>
      <Col className="text-truncate">{item.name}</Col>
      <Col className="text-truncate">{item.amount}</Col>
    </Row>
  )
}
export default ItemElement
