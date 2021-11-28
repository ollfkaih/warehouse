import { Col, Row } from 'react-bootstrap'
import AuthSession from '../modules/AuthSession'
import LoginButton from './LoginButton'

interface IProps {
  login?: AuthSession
  onLogin: (bool: boolean) => void
}

const SplashPage = (props: IProps) => {
  return (
    <>
      <Row className="align-items-center frontRow">
        <Col md="6" className="frontText align-middle px-5">
          <p className="frontTextTop">Gruppe 2138 presenterer</p>
          <h1 className="text-primary frontTextHeading">Warehouse</h1>
          <p className="frontTextDescription">
            Kontroller ditt varelager på en visjonær måte med fremtidens verktøy.
          </p>
          <LoginButton login={props.login} onLogin={props.onLogin} />
        </Col>
        <Col md="6" className="px-5">
          <img
            className="frontImage"
            src="artwork/warehouse-artwork.svg"
            width="100%"
            alt="desc"
          />
        </Col>
      </Row>
    </>
  )
}
export default SplashPage
