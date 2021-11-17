import Form from 'react-bootstrap/Form'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'
import Item from '../modules/Item'

interface IProps {
  editingItem: Item
  setEditingItem: (item: Item | undefined) => void
}

const ItemDetails = (props: IProps) => {
  const changeValue: (e: any, propertyName: keyof Item) => void = (
    e: any,
    propertyName: keyof Item
  ) => {
    const number = Number(e?.target?.value)
    const returnLegalNumberOrOldValue =
      !isNaN(number) && number > 0 && number < Number.MAX_SAFE_INTEGER
        ? Number(e.target.value)
        : props.editingItem[propertyName]
    props.setEditingItem({
      ...props.editingItem,
      [propertyName]: e.target.value === '' ? '' : returnLegalNumberOrOldValue,
    })
  }

  return (
    <>
      {props.editingItem && (
        <Form className="overflow-auto h-100">
          <Row md="12" className="m-0">
            <Col md="6">
              <h5
                className="p-1 text-truncate"
                data-toggle="tooltip"
                title={props.editingItem.brand}
              >
                {props.editingItem.brand?.substring(0, 30)}
              </h5>
              <h2
                className="p-1 text-truncate"
                data-toggle="tooltip"
                title={props.editingItem.name}
              >
                {props.editingItem.name.substring(0, 30)}
              </h2>
            </Col>
            <Col md="6" className="p-1">
              <Row className="m-0">
                <h5>Plassering</h5>
              </Row>
              <Row className="m-0">
                <Form.Floating as={Col}>
                  <Form.Control
                    id="section-control"
                    className="bg-secondary"
                    placeholder="AB"
                    value={props.editingItem.section ?? ''}
                    onChange={(e) => changeValue(e, 'section')}
                  />
                  <label htmlFor="section-control">Seksjon</label>
                </Form.Floating>
                <Form.Floating as={Col}>
                  <Form.Control
                    id="row-control"
                    className="bg-secondary"
                    placeholder="AB"
                    value={props.editingItem.row ?? ''}
                    onChange={(e) => changeValue(e, 'row')}
                  />
                  <label htmlFor="row-control">Reol</label>
                </Form.Floating>
                <Form.Floating as={Col}>
                  <Form.Control
                    id="shelf-control"
                    className="bg-secondary"
                    placeholder="AB"
                    value={props.editingItem.shelf ?? ''}
                    onChange={(e) => changeValue(e, 'shelf')}
                  />
                  <label htmlFor="shelf-control">Hylle</label>
                </Form.Floating>
              </Row>
            </Col>
          </Row>
          <Row className="m-1">
            <label className="h5" htmlFor="amount-control">
              Antall
            </label>
          </Row>
          <Row className="m-1">
            <Form.Group as={Col}>
              <Form.Control
                id="amount-control"
                className="bg-secondary"
                placeholder="0"
                value={props.editingItem.amount ?? ''}
                type="text"
                onChange={(e) => changeValue(e, 'amount')}
              />
            </Form.Group>
          </Row>
          <Row className="m-1">
            <h5>Pris</h5>
          </Row>
          <Row className="m-1">
            <Form.Floating as={Col}>
              <Form.Control
                id="regularPrice-control"
                className="bg-secondary"
                placeholder="0.0"
                value={props.editingItem.regularPrice ?? ''}
                type="text"
                onChange={(e) => changeValue(e, 'regularPrice')}
              />
              <label htmlFor="regularPrice-control">Ordinær pris</label>
            </Form.Floating>
            <Form.Floating as={Col}>
              <Form.Control
                id="salePrice-control"
                className="bg-secondary"
                placeholder="0.0"
                value={props.editingItem.salePrice ?? ''}
                type="text"
                onChange={(e) => changeValue(e, 'salePrice')}
              />
              <label htmlFor="salePrice-control">Salgspris</label>
            </Form.Floating>
            <Form.Floating as={Col}>
              <Form.Control
                id="purchasePrice-control"
                className="bg-secondary"
                placeholder="0.0"
                value={props.editingItem.purchasePrice ?? ''}
                type="text"
                onChange={(e) => changeValue(e, 'purchasePrice')}
              />
              <label htmlFor="purchasePrice-control">Innkjøpspris</label>
            </Form.Floating>
          </Row>
          <Row className="m-1">
            <h5>Dimensjoner</h5>
          </Row>
          <Row className="m-1">
            <Form.Floating as={Col}>
              <Form.Control
                id="length-control"
                className="bg-secondary"
                placeholder="0.0"
                value={props.editingItem.length ?? ''}
                type="text"
                onChange={(e) => changeValue(e, 'length')}
              />
              <label htmlFor="length-control">Lengde</label>
            </Form.Floating>
            <Form.Floating as={Col}>
              <Form.Control
                id="width-control"
                className="bg-secondary"
                placeholder="0.0"
                value={props.editingItem.width ?? ''}
                type="text"
                onChange={(e) => changeValue(e, 'width')}
              />
              <label htmlFor="width-control">Bredde</label>
            </Form.Floating>
            <Form.Floating as={Col}>
              <Form.Control
                id="height-control"
                className="bg-secondary"
                placeholder="0.0"
                value={props.editingItem.height ?? ''}
                type="text"
                onChange={(e) => changeValue(e, 'height')}
              />
              <label htmlFor="height-control">Høyde</label>
            </Form.Floating>
          </Row>
          <Row className="m-1">
            <h5>Vekt</h5>
          </Row>
          <Row className="m-1">
            <Form.Floating as={Col}>
              <Form.Control
                id="weight-control"
                className="bg-secondary"
                placeholder="0.0"
                value={props.editingItem.weight ?? ''}
                type="text"
                onChange={(e) => changeValue(e, 'weight')}
              />
              <label htmlFor="weight-control">kg</label>
            </Form.Floating>
          </Row>
          <Row className="m-1">
            <label className="h5" htmlFor="barcode-control">
              Strekkode
            </label>
          </Row>
          <Row className="m-1">
            <Form.Group as={Col}>
              <Form.Control
                id="barcode-control"
                className="bg-secondary"
                placeholder="0360002914522"
                value={props.editingItem.barcode ?? ''}
                type="text"
                onChange={(e) => changeValue(e, 'barcode')}
              />
            </Form.Group>
          </Row>
        </Form>
      )}
    </>
  )
}

export default ItemDetails
