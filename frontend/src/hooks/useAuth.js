import { useState, useEffect } from 'react';

const useAuth = () => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [username, setUsername] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem('forumengine-token');
    const storedUsername = localStorage.getItem('forumengine-username');
    if (token) {
      setIsLoggedIn(true);
    } else {
      setIsLoggedIn(false);
    }
    if (storedUsername) {
      setUsername(storedUsername);
    } else {
      setUsername(null)
    }
  }, []);

  return { isLoggedIn, username };
};

export default useAuth;