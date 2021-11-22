import ItemDetails from './ItemDetails'
import SearchBar from './SearchBar'
import ItemList from './ItemList'
import ItemDetailsButtons from './ItemDetailsButtons'
import SortButtons from './SortButtons'
import Item from '../modules/Item'
import SortOption from '../modules/SortOption'
import { v4 as uuidv4 } from 'uuid'
import { Button, Col, Container, Row } from 'react-bootstrap'
import { useEffect, useState } from 'react'
import { deleteRequest, getRequest, saveRequest } from '../asyncRequests'
import useSWR from 'swr'
import AuthSession from '../modules/AuthSession'

interface IProps {
  DOMAIN: string
  GET_ALL_ITEMS_ENDPOINT: string
  SERVER_PATH: string
  loginSession: AuthSession
}

const MainPage = (props: IProps) => {
  const [searchText, setSearchText] = useState('')
  const [sortOption, setSortOption] = useState<SortOption>(SortOption.Name)
  const [sortAscendingOrder, setSortAscendingOrder] = useState<boolean>(false)
  const [currentItem, setCurrentItem] = useState<Item>()
  const [editingItem, setEditingItem] = useState<Item>()
  useEffect(() => {
    setEditingItem(currentItem)
  }, [currentItem])

  const { data, error, mutate } = useSWR(
    props.DOMAIN + props.GET_ALL_ITEMS_ENDPOINT,
    (url) => getRequest(url, props.loginSession),
    { refreshInterval: 1 }
  )

  const deleteItem = async (id: string) => {
    if (!props.loginSession) return
    if (editingItem !== currentItem) {
      setEditingItem(undefined)
    } else {
      await deleteRequest('' + props.DOMAIN + props.SERVER_PATH + id, props.loginSession)
    }
    mutate()
    setCurrentItem(undefined)
  }
  const saveItem = async (item: Item | undefined) => {
    if (!props.loginSession) return
    item &&
      (await saveRequest(
        props.DOMAIN + props.SERVER_PATH + item.id,
        props.loginSession,
        item
      ))
    mutate()
  }

  const showDetailsCol = () => {
    return editingItem != null ? (
      <Col
        className="overflow-auto fit-to-height col-md-pull-6 detailsView"
        xs="12"
        xl="7"
      >
        <div className="detailsViewInsideDiv">
          <ItemDetailsButtons
            setCurrentItem={setCurrentItem}
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

  const addButtonIfNotEditing = () => {
    if (!editingItem)
      return (
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
  }
  return (
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
  )
}
export default MainPage
