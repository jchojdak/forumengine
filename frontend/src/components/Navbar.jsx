import { Link } from 'react-router-dom';
import './Navbar.css';
import useAuth from '../hooks/useAuth';

const Navbar = () => {
  const { isLoggedIn, username } = useAuth();

  return (
    <nav className="navbar">
      <div className="navbar-left">
        <Link className="navbar-logo" to="/">ForumEngine</Link>
        <ul className="navbar-menu">
          <li className="navbar-li">
            <Link className="navbar-link" to="/">#Home</Link>
          </li>
          <li className="navbar-li">
            <Link className="navbar-link" to="#">#Test1</Link>
          </li>
          <li className="navbar-li">
            <Link className="navbar-link" to="#">#Test2</Link>
          </li>
        </ul>
      </div>
      <div className="navbar-right">
        <ul className="navbar-menu">
          {isLoggedIn ? (
            <>
              <li className="navbar-li">
                <Link className="navbar-link" to="/profile">Profile</Link>
              </li>
              <li className="navbar-li">
                <Link className="navbar-link" to="/profile">{username}</Link>
              </li>
              <li className="navbar-li">
                <Link className="navbar-link" to="/logout">Log out</Link>
              </li>
            </>
          ) : (
            <>
              <li className="navbar-li">
                <Link className="navbar-link" to="/login">Log in</Link>
              </li>
              <li className="navbar-li">
                <Link className="navbar-link" to="/signup">Sign Up</Link>
              </li>
            </>
          )}
        </ul>
      </div>
    </nav>
  );
}

export default Navbar;
