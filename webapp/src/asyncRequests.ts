import Item from './modules/Item'
import dateFormat from 'dateformat'

export const getRequest = async (url: string) => fetch(url).then((res) => res.json())
export const deleteRequest = async (url: string) =>
  fetch(url, { method: 'DELETE' }).then((res) => res.json())
export const saveRequest = async (url: string, item: Item) =>
  fetch(url, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      ...item,
      creationDate: dateFormat(item.creationDate, 'yyyy-mm-dd"T"HH:MM:ss.l"000000"'),
    }),
  }).then((res) => res.json())
