import { Link } from 'react-router-dom'
import BootstrapNavbar from 'react-bootstrap/Navbar'
import Button from 'react-bootstrap/Button'

interface IProps {
  login: boolean
  onLogin: (bool: boolean) => void
}

const Navbar = (props: IProps) => (
  <BootstrapNavbar className="pb-0 m-0" expand={false}>
    <BootstrapNavbar.Brand className="navbar-left ms-3">
      <Link
        className="align-items-center"
        to="/"
        style={{ display: 'flex', textDecoration: 'none' }}
      >
        <img
          className="d-inline-block align-top me-3 title"
          alt="Warehouse"
          src="/icon/purple_logo.svg"
        />
      </Link>
    </BootstrapNavbar.Brand>
    <BootstrapNavbar.Text className="navbar-right me-4">
      {props.login ? (
        <Link to="/">
          <Button
            className="rounded-pill"
            variant="outline-primary"
            onClick={() => props.onLogin(false)}
          >
            Logg ut
          </Button>
        </Link>
      ) : (
        <>
          <Link to="/">
            <Button
              className="rounded-pill"
              variant="outline-primary"
              onClick={() => props.onLogin(true)}
            >
              Logg inn
            </Button>
          </Link>
        </>
      )}
    </BootstrapNavbar.Text>
  </BootstrapNavbar>
)
export default Navbar
