import Button from 'react-bootstrap/Button'
import { Col, Row } from 'react-bootstrap'
import Item from '../modules/Item'

interface IProps {
  setCurrentItem: (item: Item | undefined) => void
  editingItem: Item
  saveItem: (item: Item | undefined) => void
  deleteItem: (id: string) => void
}

const ItemDetailsButtons = (props: IProps) => {
  return (
    <Row className="px-3 pb-3">
      <Col xs="4" className="">
        <Button
          variant="light"
          className="text-primary backButton"
          id="back-button"
          onClick={() => props.setCurrentItem(undefined)}
        >
          <i className="far fa-arrow-left"></i> Tilbake
        </Button>
      </Col>
      <Col xs="4">
        <Button
          variant="success-light"
          className="text-success success-light border-none saveButton"
          id="save-button"
          onClick={() => props.saveItem(props.editingItem)}
        >
          <i className="far fa-save"></i> Lagre
        </Button>
      </Col>
      <Col xs="4">
        <Button
          variant="danger-light "
          className="text-danger danger-light border-none deleteButton"
          id="delete-button"
          onClick={() => props.deleteItem(props.editingItem.id)}
        >
          <i className="far fa-trash-alt"></i> Slett
        </Button>
      </Col>
    </Row>
  )
}

export default ItemDetailsButtons
