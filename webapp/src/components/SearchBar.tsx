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
      <InputGroup.Text className="p-0 mb-3 container-fluid border-white placeholder-primary">
        <Form.Control
          className="bg-light text-primary form-control p-3 border-0 searchForm"
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
