import React, { useState } from 'react'
import './App.scss'
import { Route } from 'react-router-dom'
import SplashPage from './components/SplashPage'
import Navbar from './components/Navbar'
import ItemDetails from './components/ItemDetails'
import SearchBar from './components/SearchBar'
import ItemList from './components/ItemList'
import Col from 'react-bootstrap/Col'
import Row from 'react-bootstrap/Row'
import { Container } from 'react-bootstrap'
import Item from './modules/Item'
import SortButtons from './components/SortButtons'
import SortOption from './modules/SortOption'

const REACT_APP_DOMAIN = 'http://localhost:8080'

function App() {
  const [login, setLogin] = useState(true)
  const [currentItem, setCurrentItem] = useState<Item>()
  const [searchText, setSearchText] = useState('')
  const [sortOption, setSortOption] = useState<SortOption>(SortOption.Name)
  const [sortAscendingOrder, setSortAscendingOrder] = useState<boolean>(false)

  if (!REACT_APP_DOMAIN)
    return <p>Domain for server not set, configure REACT_APP_DOMAIN variable</p>

  function showDetailsCol(): React.ReactNode {
    return currentItem != null ? (
      <Col className="fit-to-height col-md-pull-6" sm="12" md="6">
        <ItemDetails item={currentItem} />
      </Col>
    ) : (
      <></>
    )
  }

  return (
    <Col className="vh-100 overflow-hidden">
      <Navbar login={login} onLogin={setLogin} />
      <Row className="Content d-flex m-0 flex-shrink-1 h-100">
        <Route path="/" exact>
          {login ? (
            <Container className="p-3 pe-0 pt-0 m-0 mw-100 h-100">
              <Row className="h-100 flex-nowrap flex-row-reverse rb">
                {showDetailsCol()}
                <Col
                  className="fit-to-height col-md-push-6"
                  sm="12"
                  md={currentItem == null ? 12 : 6}
                >
                  <SearchBar searchText={searchText} setSearchText={setSearchText} />
                  <SortButtons
                    sortOption={sortOption}
                    setSortOption={setSortOption}
                    sortAscendingOrder={sortAscendingOrder}
                    setSortAscendingOrder={setSortAscendingOrder}
                  />
                  <ItemList
                    domain={REACT_APP_DOMAIN}
                    searchText={searchText}
                    sortOption={sortOption}
                    currentItem={currentItem}
                    setCurrentItem={setCurrentItem}
                    sortAscendingOrder={sortAscendingOrder}
                  />
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
