import User from './User'

interface AuthSession {
  token: string
  user: User
}

export default AuthSession
