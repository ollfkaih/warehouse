import Form from 'react-bootstrap/Form'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'
import Item from '../modules/Item'
var Barcode = require('react-barcode')

interface IProps {
  editingItem: Item
  setEditingItem: (item: Item | undefined) => void
}

const ItemDetails = (props: IProps) => {
  function validateNumber(
    e: any,
    propertyName: keyof Item,
    allowLeadingZeroes?: boolean
  ) {
    const number = Number(e?.target?.value)
    return !isNaN(number) &&
      (number > 0 || allowLeadingZeroes) &&
      number < Number.MAX_SAFE_INTEGER
      ? e.target.value
      : props.editingItem[propertyName]
  }

  const changeValue = (
    e: any,
    propertyName: keyof Item,
    isNumber: boolean,
    allowLeadingZeroes?: boolean
  ) => {
    const legalValue = isNumber
      ? validateNumber(e, propertyName, allowLeadingZeroes)
      : e.target.value

    props.setEditingItem({
      ...props.editingItem,
      [propertyName]: e.target.value === '' ? '' : legalValue,
    })
  }

  return (
    <>
      {props.editingItem && (
        <Form className="overflow-auto-y h-100">
          <Row md="12" className="m-0">
            <Row className="m-1 pt-4">
              <h5>
                <i className="far fa-tag"></i> Produktinfo
              </h5>
            </Row>
            <Row className="m-1">
              <Form.Floating className="my-1" as={Col}>
                <Form.Control
                  id="name-control"
                  className="bg-secondary border-0 border-white"
                  placeholder="AB"
                  value={props.editingItem.name ?? ''}
                  onChange={(e) => changeValue(e, 'name', false)}
                />
                <label htmlFor="section-control">Produktnavn</label>
              </Form.Floating>

              <Form.Floating className="my-1" as={Col}>
                <Form.Control
                  id="brand-control"
                  className="bg-secondary border-0"
                  placeholder="AB"
                  value={props.editingItem.brand ?? ''}
                  onChange={(e) => changeValue(e, 'brand', false)}
                />
                <label htmlFor="section-control">Produsent</label>
              </Form.Floating>
            </Row>
            <Row className="m-1">
              <Row className="pt-4">
                <label className="h5" htmlFor="amount-control">
                  <i className="far fa-inventory"></i> Lagerbeholdning
                </label>
              </Row>
              <Col md="6" className="p-0">
                <Row className="m-0">
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
              </Col>
              <Col md="6" className="p-0">
                <Row className="m-0">
                  <div className="inputSectionDiv col me-1">
                    <Form.Group as={Col}>
                      <Form.Control
                        id="section-control"
                        className="input-section"
                        placeholder="?"
                        value={props.editingItem.section ?? ''}
                        onChange={(e) => changeValue(e, 'section', false)}
                      />
                    </Form.Group>
                    <p>Seksjon</p>
                  </div>

                  <div className="inputSectionDiv col">
                    <Form.Group as={Col}>
                      <Form.Control
                        id="row-control"
                        className="input-section"
                        placeholder="?"
                        value={props.editingItem.row ?? ''}
                        onChange={(e) => changeValue(e, 'row', false)}
                      />
                    </Form.Group>
                    <p>Rad</p>
                  </div>

                  <div className="inputSectionDiv col ms-1">
                    <Form.Group as={Col}>
                      <Form.Control
                        id="shelf-control"
                        className="input-section"
                        placeholder="?"
                        value={props.editingItem.shelf ?? ''}
                        onChange={(e) => changeValue(e, 'shelf', false)}
                      />
                    </Form.Group>
                    <p>Hylle</p>
                  </div>
                </Row>
              </Col>
            </Row>

            <Row className="m-1">
              <h5>
                <i className="far fa-coins"></i> Pris
              </h5>
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

            <Row className="m-1 pt-4">
              <h5>
                <i className="far fa-box"></i> Dimensjoner{' '}
                <i style={{ fontWeight: 300 }}>(cm)</i>
              </h5>
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

            <Row className="m-1 pt-4">
              <h5>
                <i className="far fa-weight-hanging"></i> Vekt
              </h5>
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
            <Row className="m-1 pt-4">
              <label className="h5" htmlFor="barcode-control">
                <i className="fad fa-barcode-read"></i> Strekkode
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
                  onChange={(e) => changeValue(e, 'barcode', true, true)}
                />
              </Form.Group>
            </Row>
            {props.editingItem.barcode &&
            props.editingItem.barcode.toString().length === 13 ? (
              <Row className="barcodeRow mt-2">
                <Barcode value={props.editingItem.barcode} />
              </Row>
            ) : (
              <></>
            )}
          </Row>
        </Form>
      )}
    </>
  )
}

export default ItemDetails
