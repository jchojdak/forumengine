import { useState, useEffect } from 'react';

const useAuth = () => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [username, setUsername] = useState(null);
  const [userId, setUserId] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem('forumengine-token');
    const storedUsername = localStorage.getItem('forumengine-username');
    const userId = localStorage.getItem('forumengine-user_id');
    if (token) {
      setIsLoggedIn(true);
    } else {
      setIsLoggedIn(false);
    }
    if (storedUsername) {
      setUsername(storedUsername);
    } else {
      setUsername(null);
    }
    if (userId) {
      setUserId(userId);
    } else {
      setUserId(null);
    }
  }, []);

  return { isLoggedIn, username, userId };
};

export default useAuth;