import React, { useState } from 'react'
import './App.scss'
import { Route } from 'react-router-dom'
import SplashPage from './components/SplashPage'
import Navbar from './components/Navbar'
import MainPage from './components/MainPage'
import Col from 'react-bootstrap/Col'
import Row from 'react-bootstrap/Row'
import { loginRequest } from './asyncRequests'
import Login from './components/Login'
import LoginRequest from './modules/LoginRequest'
import AuthSession from './modules/AuthSession'

const DOMAIN = process.env.REACT_APP_DOMAIN || ''
const SERVER_PATH = process.env.REACT_APP_SERVER_PATH || ''
const GET_ALL_ITEMS_ENDPOINT = process.env.REACT_APP_GET_ALL_ITEMS_ENDPOINT || ''
const USER_SERVER_PATH = process.env.REACT_APP_USER_SERVER_PATH || ''

const App = () => {
  const [showLoginModal, setShowLoginModal] = useState<boolean>(false)
  const [loginSession, setLoginSession] = useState<AuthSession | undefined>(undefined)
  const [loginError, setLoginError] = useState<any>()

  if (!DOMAIN)
    return <p>Domain for server not set, configure process.env.DOMAIN variable</p>

  const login = async (loginDetails: LoginRequest) => {
    try {
      const authSession = await loginRequest(
        DOMAIN + USER_SERVER_PATH + 'login/',
        loginDetails
      )
      setLoginSession(authSession)
      setShowLoginModal(false)
      setLoginError(undefined)
    } catch (e) {
      setLoginError(e)
    }
  }

  const promptLoginOrLogout = (promptLogin: boolean) => {
    promptLogin ? setShowLoginModal(true) : setLoginSession(undefined)
  }

  return (
    <Col className="vh-100 overflow-hidden">
      <Navbar login={loginSession} onLogin={promptLoginOrLogout} />
      <Row className="Content d-flex m-0 flex-shrink-1 h-100">
        <Route path="/" exact>
          {loginSession ? (
            <MainPage
              DOMAIN={DOMAIN}
              GET_ALL_ITEMS_ENDPOINT={GET_ALL_ITEMS_ENDPOINT}
              SERVER_PATH={SERVER_PATH}
              loginSession={loginSession}
            />
          ) : (
            <>
              <Login
                show={showLoginModal}
                error={loginError}
                onClose={() => setShowLoginModal(false)}
                onLogin={(request) => login(request)}
              />
              <SplashPage login={loginSession} onLogin={promptLoginOrLogout} />
            </>
          )}
        </Route>
        <Route path="/login">
          <p> Logg inn her </p>
        </Route>
      </Row>
    </Col>
  )
}

export default App
