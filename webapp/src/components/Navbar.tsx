import { Link } from 'react-router-dom'
import BootstrapNavbar from 'react-bootstrap/Navbar'
import { Image } from 'react-bootstrap'
import AuthSession from '../modules/AuthSession'
import LoginButton from './LoginButton'

interface IProps {
  login?: AuthSession
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
      <LoginButton login={props.login} onLogin={props.onLogin} />
    </BootstrapNavbar.Text>
  </BootstrapNavbar>
)
export default Navbar
