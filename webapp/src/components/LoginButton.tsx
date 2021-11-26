import { Button } from 'react-bootstrap'
import { Link } from 'react-router-dom'
import AuthSession from '../modules/AuthSession'

interface IProps {
  login?: AuthSession
  onLogin: (bool: boolean) => void
}

const LoginButton = (props: IProps) => (
  <>
    {props.login ? (
      <Link to="/">
        <Button
          id="logout-btn"
          className="rounded-pill px-4"
          variant="outline-primary"
          onClick={() => props.onLogin(false)}
        >
          <i className="far fa-lock" /> Logg ut
        </Button>
      </Link>
    ) : (
      <Link to="/">
        <Button
          id="login-btn"
          className="rounded-pill"
          variant="outline-primary"
          onClick={() => props.onLogin(true)}
        >
          Logg inn
        </Button>
      </Link>
    )}
  </>
)

export default LoginButton
