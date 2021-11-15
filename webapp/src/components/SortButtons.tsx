import Button from 'react-bootstrap/Button'
import { ToggleButton, ToggleButtonGroup } from 'react-bootstrap'
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
          className="text-primary"
          id="brand-button"
          value={SortOption.Brand}
          onClick={() => props.setSortOption(SortOption.Brand)}
        >
          Produsent
        </ToggleButton>
        <ToggleButton
          variant="light"
          className="text-primary"
          id="name-button"
          value={SortOption.Name}
          onClick={() => props.setSortOption(SortOption.Name)}
        >
          Navn
        </ToggleButton>
        <ToggleButton
          variant="light"
          className="text-primary"
          id="amount-button"
          value={SortOption.Amount}
          onClick={() => props.setSortOption(SortOption.Amount)}
        >
          Antall
        </ToggleButton>
        <ToggleButton
          variant="light"
          className="text-primary"
          id="toggle-button"
          value={SortOption.Price}
          onClick={() => props.setSortOption(SortOption.Price)}
        >
          Pris
        </ToggleButton>
        <ToggleButton
          variant="light"
          className="text-primary"
          id="date-button"
          value={SortOption.Date}
          onClick={() => props.setSortOption(SortOption.Date)}
        >
          Dato
        </ToggleButton>
      </ToggleButtonGroup>
      <Button
        variant="secondary"
        onClick={() => props.setSortAscendingOrder(!props.sortAscendingOrder)}
      >
        {props.sortAscendingOrder ? 'Opp' : 'Ned'}-pil
      </Button>
    </div>
  )
}

export default SortButtons
