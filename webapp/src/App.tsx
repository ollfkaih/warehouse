import React, { useEffect, useState } from 'react'
import './App.scss'
import { Route } from 'react-router-dom'
import SplashPage from './components/SplashPage'
import Navbar from './components/Navbar'
import ItemDetails from './components/ItemDetails'
import SearchBar from './components/SearchBar'
import ItemList from './components/ItemList'
import Col from 'react-bootstrap/Col'
import Row from 'react-bootstrap/Row'
import { Button, Container } from 'react-bootstrap'
import Item from './modules/Item'
import SortButtons from './components/SortButtons'
import SortOption from './modules/SortOption'
import ItemDetailsButtons from './components/ItemDetailsButtons'
import { deleteRequest, saveRequest, getRequest, loginRequest } from './asyncRequests'
import useSWR from 'swr'
import { v4 as uuidv4 } from 'uuid'
import AuthSession from './modules/AuthSession'
import Login from './components/Login'
import LoginRequest from './modules/LoginRequest'

const DOMAIN = process.env.REACT_APP_DOMAIN || ''
const SERVER_PATH = process.env.REACT_APP_SERVER_PATH || ''
const GET_ALL_ITEMS_ENDPOINT = process.env.REACT_APP_GET_ALL_ITEMS_ENDPOINT || ''
const USER_SERVER_PATH = process.env.REACT_APP_USER_SERVER_PATH || ''

const App = () => {
  const [showLoginModal, setShowLoginModal] = useState<boolean>(false)
  const [loginSession, setLoginSession] = useState<AuthSession | undefined>(undefined)
  const [currentItem, setCurrentItem] = useState<Item>()
  const [searchText, setSearchText] = useState('')
  const [sortOption, setSortOption] = useState<SortOption>(SortOption.Name)
  const [sortAscendingOrder, setSortAscendingOrder] = useState<boolean>(false)

  const { data, error, mutate } = useSWR(DOMAIN + GET_ALL_ITEMS_ENDPOINT, getRequest, {
    refreshInterval: 1,
  })
  const deleteItem = async (id: string) => {
    if (!loginSession) return
    if (editingItem !== currentItem) {
      setEditingItem(undefined)
    } else {
      await deleteRequest('' + DOMAIN + SERVER_PATH + id, loginSession)
    }
    mutate()
    setCurrentItem(undefined)
  }
  const saveItem = async (item: Item | undefined) => {
    if (!loginSession) return
    item && (await saveRequest(DOMAIN + SERVER_PATH + item.id, loginSession, item))
    mutate()
  }
  const [editingItem, setEditingItem] = useState<Item>()
  useEffect(() => {
    setEditingItem(currentItem)
  }, [currentItem])

  if (!DOMAIN)
    return <p>Domain for server not set, configure process.env.DOMAIN variable</p>

  function showDetailsCol(): React.ReactNode {
    return editingItem != null ? (
      <Col
        className="overflow-auto fit-to-height col-md-pull-6 detailsView"
        xs="12"
        xl="7"
      >
        <div className="detailsViewInsideDiv">
          <ItemDetailsButtons
            setEditingItem={setEditingItem}
            editingItem={editingItem}
            saveItem={saveItem}
            deleteItem={deleteItem}
          />
          <ItemDetails editingItem={editingItem} setEditingItem={setEditingItem} />
        </div>
      </Col>
    ) : (
      <Col></Col>
    )
  }

  const addButtonIfNotEditing = () =>
    editingItem ? (
      <></>
    ) : (
      <Button
        id="addNewItemButton"
        className="rounded-pill position-fixed bottom-0 end-50 m-4 btn-lg"
        onClick={() =>
          setEditingItem({
            name: '',
            id: uuidv4(),
            amount: 0,
            creationDate: new Date(),
          })
        }
      >
        <i className="fal fa-plus me-2" />
        Legg til produkt
      </Button>
    )

  const login = async (loginDetails: LoginRequest) => {
    const authSession = await loginRequest(
      DOMAIN + USER_SERVER_PATH + 'login/',
      loginDetails
    )
    setLoginSession(authSession)
    setShowLoginModal(false)
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
            <Container className="p-3 pe-0 pt-0 m-0 mw-100 h-100">
              <Row className="h-100 flex-nowrap flex-row-reverse rb">
                {showDetailsCol()}
                <Col
                  className="fit-to-height ps-4 col-md-push-5 align-items-end align"
                  xs="12"
                  lg="5"
                >
                  <SearchBar searchText={searchText} setSearchText={setSearchText} />
                  <SortButtons
                    sortOption={sortOption}
                    setSortOption={setSortOption}
                    sortAscendingOrder={sortAscendingOrder}
                    setSortAscendingOrder={setSortAscendingOrder}
                  />
                  <ItemList
                    searchText={searchText}
                    sortOption={sortOption}
                    currentItem={currentItem}
                    setCurrentItem={setCurrentItem}
                    sortAscendingOrder={sortAscendingOrder}
                    swrData={data}
                    swrError={error}
                  />
                  {addButtonIfNotEditing()}
                </Col>
              </Row>
            </Container>
          ) : (
            <>
              <Login
                show={showLoginModal}
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
