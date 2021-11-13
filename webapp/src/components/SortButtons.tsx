import { ButtonGroup } from 'react-bootstrap'
import SortOption from '../modules/SortOption'
import Button from 'react-bootstrap/Button'

interface IProps {
  sortOption: SortOption
  setSortOption: (sortoption: SortOption) => void
}

const SortButtons = ({ sortOption, setSortOption }: IProps) => {
  return (
    <ButtonGroup>
      <Button
        variant="outline-primary"
        onClick={() => setSortOption(SortOption.Name)}
      >
        Navn
      </Button>
      <Button
        variant="outline-primary"
        onClick={() => setSortOption(SortOption.Brand)}
      >
        Produsent
      </Button>
      <Button
        variant="outline-primary"
        onClick={() => setSortOption(SortOption.Amount)}
      >
        Antall
      </Button>
      <Button
        variant="outline-primary"
        onClick={() => setSortOption(SortOption.Price)}
      >
        Pris
      </Button>
      <Button
        variant="outline-primary"
        onClick={() => setSortOption(SortOption.Date)}
      >
        Dato
      </Button>
    </ButtonGroup>
  )
}

export default SortButtons
