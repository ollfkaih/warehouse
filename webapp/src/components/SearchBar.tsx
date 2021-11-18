import { Form, InputGroup } from 'react-bootstrap'

interface IProps {
  searchText: string
  setSearchText: (String: string) => void
}

const SearchBar = (props: IProps) => {
  const handleSearch = (e: React.ChangeEvent<HTMLInputElement>) =>
    props.setSearchText(e.target.value)

  return (
    <InputGroup>
      <InputGroup.Text className="p-0 mb-2 container-fluid border-white placeholder-primary">
        <Form.Control
          id="searchField"
          className="bg-light text-primary form-control"
          type="text"
          placeholder="Søk..."
          value={props.searchText}
          onChange={handleSearch}
        />
      </InputGroup.Text>
    </InputGroup>
  )
}

export default SearchBar
