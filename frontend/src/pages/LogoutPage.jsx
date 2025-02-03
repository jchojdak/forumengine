import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const LogoutPage = () => {
  const navigate = useNavigate();

  useEffect(() => {
    localStorage.removeItem('forumengine-token');
    localStorage.removeItem('forumengine-username');
    localStorage.removeItem('forumengine-role');
    localStorage.removeItem('forumengine-user_id');

    setTimeout(() => {
        navigate('/');
      }, 3000);
  }, [navigate]);

  return (
    <div>
        <div>Logging out...</div>
        <div>Redirecting to home...</div>
    </div>
  );
};

export default LogoutPage;
