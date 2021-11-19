import Item from './modules/Item'
import dateFormat from 'dateformat'
import LoginRequest from './modules/LoginRequest'
import AuthSession from './modules/AuthSession'

export const getRequest = async (url: string) => fetch(url).then((res) => res.json())
export const deleteRequest = async (url: string, auth: AuthSession) =>
  fetch(url, {
    method: 'DELETE',
    headers: { 'auth-token': auth.token },
  }).then((res) => res.json())
export const saveRequest = async (url: string, auth: AuthSession, item: Item) =>
  fetch(url, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json', 'auth-token': auth.token },
    body: JSON.stringify({
      ...item,
      creationDate: dateFormat(item.creationDate, 'yyyy-mm-dd"T"HH:MM:ss.l"000000"'),
    }),
  }).then((res) => res.json())
export const loginRequest = async (
  url: string,
  loginRequest: LoginRequest
): Promise<AuthSession> => {
  return fetch(url, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(loginRequest),
  }).then((res) => res.json())
}
