import { Button, Col, Row } from 'react-bootstrap'
import { Link } from 'react-router-dom'
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
          <p className="frontTextTop">#1 easy to use</p>
          <h1 className="text-primary frontTextHeading">Warehouse</h1>
          <p className="frontTextDescription">
            Store and manage your infrastructure like a visionary, not a functionary
          </p>
          <LoginButton login={props.login} onLogin={props.onLogin} />
        </Col>
        <Col md="6" className="px-5">
          <img
            className="frontImage"
            src="artwork/warehouse-artwork.svg"
            width="100%"
            alt="desc"
          ></img>
        </Col>
      </Row>
    </>
  )
}
export default SplashPage
