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
import { deleteRequest, saveRequest, getRequest } from './asyncRequests'
import useSWR from 'swr'
import { v4 as uuidv4 } from 'uuid'

const REACT_APP_DOMAIN = 'http://localhost:8080'
const REACT_APP_SERVER_PATH = '/warehouse/item/'
const REACT_APP_GET_ALL_ITEMS_ENDPOINT = '/warehouse/items'

const App = () => {
  const [login, setLogin] = useState(true)
  const [currentItem, setCurrentItem] = useState<Item>()
  const [searchText, setSearchText] = useState('')
  const [sortOption, setSortOption] = useState<SortOption>(SortOption.Name)
  const [sortAscendingOrder, setSortAscendingOrder] = useState<boolean>(false)

  const { data, error, mutate } = useSWR(
    REACT_APP_DOMAIN + REACT_APP_GET_ALL_ITEMS_ENDPOINT,
    getRequest
  )
  const deleteItem = async (id: string) => {
    await deleteRequest(REACT_APP_DOMAIN + REACT_APP_SERVER_PATH + id)
    mutate()
    setCurrentItem(undefined)
  }
  const saveItem = async (item: Item | undefined) => {
    item && (await saveRequest(REACT_APP_DOMAIN + REACT_APP_SERVER_PATH + item.id, item))
    mutate()
  }
  const [editingItem, setEditingItem] = useState<Item>()
  useEffect(() => {
    setEditingItem(currentItem)
  }, [currentItem])

  if (!REACT_APP_DOMAIN)
    return <p>Domain for server not set, configure REACT_APP_DOMAIN variable</p>

  function showDetailsCol(): React.ReactNode {
    return currentItem != null && editingItem != null ? (
      <Col className="fit-to-height col-md-pull-6 detailsView" xs="12" lg="7">
        <div className="detailsViewInsideDiv">
          <ItemDetailsButtons
            setCurrentItem={setCurrentItem}
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

  return (
    <Col className="vh-100">
      <Navbar login={login} onLogin={setLogin} />
      <Row className="Content d-flex m-0 flex-shrink-1 h-100">
        <Route path="/" exact>
          {login ? (
            <Container className="p-3 pe-0 pt-0 m-0 mw-100 h-100">
              <Row className="h-100 flex-nowrap flex-row-reverse rb">
                {showDetailsCol()}
                <Col className="fit-to-height ps-4 col-md-push-5 align-items-end align" xs="12" lg="5">
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
                  <Button
                    className="rounded-pill position-fixed bottom-0 end-50 m-4 btn-lg btn"
                    onClick={() =>
                      setEditingItem({
                        name: '',
                        id: uuidv4(),
                        amount: 0,
                        creationDate: new Date(),
                      })
                    }
                  >
                    <i className="fal fa-plus me-2"></i>Legg til produkt
                  </Button>
                </Col>
              </Row>
            </Container>
          ) : (
            <SplashPage />
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
