interface Item {
  id: string
  name: string
  amount: number
  barcode?: number
  brand?: string
  regularPrice?: number
  salePrice?: number
  purchasePrice?: number
  section?: string
  row?: string
  shelf?: string
  weight?: number
  creationDate: Date
  height?: number
  width?: number
  length?: number
}

export default Item
