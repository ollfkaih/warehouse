import { Link } from 'react-router-dom'
import BootstrapNavbar from 'react-bootstrap/Navbar'
import Button from 'react-bootstrap/Button'
import { Image } from 'react-bootstrap'

interface IProps {
  login: boolean
  onLogin: (bool: boolean) => void
}

const Navbar = (props: IProps) => (
  <BootstrapNavbar className="p-0 m-0" expand={false}>
    <BootstrapNavbar.Brand className="navbar-left ms-3 py-1">
      <Link to="/">
        <Image
          className="text-primary"
          alt="Warehouse"
          height="60em"
          src="/icon/purple_logo.svg"
        />
      </Link>
    </BootstrapNavbar.Brand>
    <BootstrapNavbar.Text className="navbar-right me-4">
      {props.login ? (
        <Link to="/">
          <Button
            className="rounded-pill px-4"
            variant="outline-primary"
            onClick={() => props.onLogin(false)}
          >
            <i className="far fa-lock"></i> Logg ut
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
