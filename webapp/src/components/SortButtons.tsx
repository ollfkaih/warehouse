import Button from 'react-bootstrap/Button'
import { Image, ToggleButton, ToggleButtonGroup } from 'react-bootstrap'
import SortOption from '../modules/SortOption'

interface IProps {
  sortOption: SortOption
  setSortOption: (sortoption: SortOption) => void
  sortAscendingOrder: boolean
  setSortAscendingOrder: (order: boolean) => void
}

const SortButtons = (props: IProps) => {
  return (
    <div className="mb-2">
      <ToggleButtonGroup
        className="me-2"
        type="radio"
        name="SortButtons"
        defaultValue={SortOption.Date}
      >
        <ToggleButton
          variant="light"
          className="text-primary mx-1 rounded-pill sortToggle"
          id="brand-button"
          value={SortOption.Brand}
          onClick={() => props.setSortOption(SortOption.Brand)}
        >
          Produsent
        </ToggleButton>
        <ToggleButton
          variant="light"
          className="text-primary mx-1 rounded-pill sortToggle"
          id="name-button"
          value={SortOption.Name}
          onClick={() => props.setSortOption(SortOption.Name)}
        >
          Navn
        </ToggleButton>
        <ToggleButton
          variant="light"
          className="text-primary mx-1 rounded-pill sortToggle"
          id="amount-button"
          value={SortOption.Amount}
          onClick={() => props.setSortOption(SortOption.Amount)}
        >
          Antall
        </ToggleButton>
        <ToggleButton
          variant="light"
          className="text-primary mx-1 rounded-pill sortToggle"
          id="price-button"
          value={SortOption.Price}
          onClick={() => props.setSortOption(SortOption.Price)}
        >
          Pris
        </ToggleButton>
        <ToggleButton
          variant="light"
          className="text-primary mx-1 rounded-pill sortToggle"
          id="date-button"
          value={SortOption.Date}
          onClick={() => props.setSortOption(SortOption.Date)}
        >
          Dato
        </ToggleButton>
      </ToggleButtonGroup>
      <Button
        id="sortDirectionButton"
        variant="secondary"
        className="m-0 pe-2 pb-1 ps-2 pt-1"
        onClick={() => props.setSortAscendingOrder(!props.sortAscendingOrder)}
      >
        {props.sortAscendingOrder ? (
          <Image fluid src="icon/sort/sort-up.svg" width="15em" height="24em" />
        ) : (
          <Image fluid src="icon/sort/sort-down.svg" width="15em" height="24em" />
        )}
      </Button>
    </div>
  )
}

export default SortButtons
