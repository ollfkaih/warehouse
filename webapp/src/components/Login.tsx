import { useState } from 'react'
import { Button, Col, Form, Modal } from 'react-bootstrap'
import LoginRequest from '../modules/LoginRequest'

interface IProps {
  show: boolean
  error?: any
  onClose: () => void
  onLogin: (LoginRequest: LoginRequest) => void
}

const Login = (props: IProps) => {
  const [loginDetails, setLoginDetails] = useState<LoginRequest>({
    username: '',
    password: '',
  })

  const close = () => {
    setLoginDetails({ username: '', password: '' })
    props.onClose()
  }

  return (
    <Modal show={props.show} onHide={close}>
      <Modal.Header closeButton>
        <Modal.Title>Logg inn</Modal.Title>
      </Modal.Header>

      <Modal.Body>
        <Form.Floating as={Col}>
          <Form.Control
            id="username-control"
            className="bg-secondary"
            placeholder="0.0"
            value={loginDetails.username}
            type="text"
            onChange={(e) =>
              setLoginDetails({ ...loginDetails, username: e.target.value })
            }
          />
          <label htmlFor="username-control" className="loginInputFieldLabel">
            <i className="far fa-user"></i> Brukernavn
          </label>
        </Form.Floating>

        <Form.Floating as={Col}>
          <Form.Control
            id="password-control"
            className="bg-secondary mt-3"
            placeholder="0.0"
            value={loginDetails.password}
            type="password"
            onChange={(e) =>
              setLoginDetails({ ...loginDetails, password: e.target.value })
            }
          />
          <label htmlFor="password-control" className="loginInputFieldLabel">
            <i className="far fa-lock"></i> Passord
          </label>
        </Form.Floating>
        <Col className="mt-2">
          <span className="bg-white text-danger rounded p-1">{props.error}</span>
        </Col>
      </Modal.Body>

      <Modal.Footer>
        <Button variant="secondary" onClick={close}>
          <i className="far fa-times"></i> Lukk
        </Button>
        <Button
          id="login-button-modal"
          variant="primary"
          onClick={() => {
            props.onLogin(loginDetails)
          }}
        >
          <i className="far fa-key"></i> Logg inn
        </Button>
      </Modal.Footer>
    </Modal>
  )
}
export default Login
