import { useState, useEffect } from 'react';
import { isTokenValid } from "../utils/jwtUtils";

const useAuth = () => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [username, setUsername] = useState(null);
  const [userId, setUserId] = useState(null);
  const [token, setToken] = useState(null);
  const [roles, setRoles] = useState([]);

  useEffect(() => {
    const storedToken = localStorage.getItem('forumengine-token');

    if(!isTokenValid(storedToken)) {
      localStorage.removeItem('forumengine-token');
      localStorage.removeItem('forumengine-username');
      localStorage.removeItem('forumengine-user_id');
      localStorage.removeItem('forumengine-roles');

      console.error("Invalid token, logging out.");
    }

    setToken(storedToken);
    setIsLoggedIn(!!storedToken);

    setUsername(localStorage.getItem('forumengine-username') || null);
    setUserId(localStorage.getItem('forumengine-user_id') || null);

    const storedRoles = localStorage.getItem('forumengine-roles');
    if (storedRoles) {
      try {
        setRoles(JSON.parse(storedRoles));
      } catch (error) {
        console.error("Error parsing roles:", error);
        setRoles([]);
      }
    } else {
      setRoles([]);
    }
  }, []);

  return { isLoggedIn, username, userId, roles, token };
};

export default useAuth;