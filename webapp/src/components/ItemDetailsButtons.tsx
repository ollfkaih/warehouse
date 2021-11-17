import Button from 'react-bootstrap/Button'
import { Col, Row } from 'react-bootstrap'
import Item from '../modules/Item'

interface IProps {
  currentItem: Item
  setCurrentItem: (item: Item | undefined) => void
  editingItem: Item | undefined
  saveItem: (item: Item | undefined) => void
  deleteItem: (id: string) => void
}

const ItemDetailsButtons = (props: IProps) => {
  return (
    <Row>
      <Col xs="4" md={{ span: 3, offset: 1 }} className="">
        <Button
          variant="light"
          className="text-primary"
          id="back-button"
          onClick={() => props.setCurrentItem(undefined)}
        >
          Tilbake
        </Button>
      </Col>
      <Col xs="4" md={{ span: 3, offset: 1 }}>
        <Button
          variant="success-light"
          className="text-success success-light border-none"
          id="save-button"
          onClick={() => props.saveItem(props.editingItem)}
        >
          Lagre
        </Button>
      </Col>
      <Col xs="4" md={{ span: 3, offset: 1 }}>
        <Button
          variant="danger-light "
          className="text-danger danger-light border-none"
          id="delete-button"
          onClick={() => props.deleteItem(props.currentItem.id)}
        >
          Slett
        </Button>
      </Col>
    </Row>
  )
}

export default ItemDetailsButtons
