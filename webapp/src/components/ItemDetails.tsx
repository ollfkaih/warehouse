import Form from 'react-bootstrap/Form'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'
import Item from '../modules/Item'

interface IProps {
  editingItem: Item
  setEditingItem: (item: Item | undefined) => void
}

const ItemDetails = (props: IProps) => {
  function validateNumber(e: any, propertyName: keyof Item) {
    const number = Number(e?.target?.value)
    return !isNaN(number) && number > 0 && number < Number.MAX_SAFE_INTEGER
      ? Number(e.target.value)
      : props.editingItem[propertyName]
  }

  const changeValue = (e: any, propertyName: keyof Item, isNumber: boolean) => {
    const legalValue = isNumber ? validateNumber(e, propertyName) : e.target.value

    props.setEditingItem({
      ...props.editingItem,
      [propertyName]: e.target.value === '' ? '' : legalValue,
    })
  }

  return (
    <>
      {props.editingItem && (
        <Form className="overflow-auto h-100">
          <Row md="12" className="m-1">
            <Col md="6" className="">
              <Row>
                <Form.Floating className="mt-1 mb-1" as={Col}>
                  <Form.Control
                    id="brand-control"
                    className="bg-light border-0"
                    placeholder="AB"
                    value={props.editingItem.brand ?? ''}
                    onChange={(e) => changeValue(e, 'brand', false)}
                  />
                  <label htmlFor="section-control">Produsent</label>
                </Form.Floating>
              </Row>
              <Row>
                <Form.Floating className="mt-1 mb-1" as={Col}>
                  <Form.Control
                    id="name-control"
                    className="bg-light border-0 border-white"
                    placeholder="AB"
                    value={props.editingItem.name ?? ''}
                    onChange={(e) => changeValue(e, 'name', false)}
                  />
                  <label htmlFor="section-control">Produktnavn</label>
                </Form.Floating>
              </Row>
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
                    onChange={(e) => changeValue(e, 'section', false)}
                  />
                  <label htmlFor="section-control">Seksjon</label>
                </Form.Floating>
                <Form.Floating as={Col}>
                  <Form.Control
                    id="row-control"
                    className="bg-secondary"
                    placeholder="AB"
                    value={props.editingItem.row ?? ''}
                    onChange={(e) => changeValue(e, 'row', false)}
                  />
                  <label htmlFor="row-control">Reol</label>
                </Form.Floating>
                <Form.Floating as={Col}>
                  <Form.Control
                    id="shelf-control"
                    className="bg-secondary"
                    placeholder="AB"
                    value={props.editingItem.shelf ?? ''}
                    onChange={(e) => changeValue(e, 'shelf', false)}
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
                onChange={(e) => changeValue(e, 'amount', true)}
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
                onChange={(e) => changeValue(e, 'regularPrice', true)}
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
                onChange={(e) => changeValue(e, 'salePrice', true)}
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
                onChange={(e) => changeValue(e, 'purchasePrice', true)}
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
                onChange={(e) => changeValue(e, 'length', true)}
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
                onChange={(e) => changeValue(e, 'width', true)}
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
                onChange={(e) => changeValue(e, 'height', true)}
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
                onChange={(e) => changeValue(e, 'weight', true)}
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
                onChange={(e) => changeValue(e, 'barcode', true)}
              />
            </Form.Group>
          </Row>
        </Form>
      )}
    </>
  )
}

export default ItemDetails
