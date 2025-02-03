import React, { useState } from 'react';
import axios from 'axios';
import './LoginPage.css';
import { Link, useNavigate } from 'react-router-dom';
import { API_URL } from "../../config";

const LoginPage = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const validateForm = () => {
    if (username.trim().length < 4 || username.trim().length > 20) {
      return 'Username must be between 4 and 20 characters!';
    }

    if (password.length < 4 || password.length > 20) {
      return 'Password must be between 4 and 20 characters!';
    }

    return '';
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    const validationError = validateForm();
    if (validationError) {
      setError(validationError);
      return;
    }
    
    const credentials = { username, password };

    try {
      const response = await axios.post(API_URL + '/auth/login', credentials);

      console.log('Status:', response.status);
      console.log('Response Data:', response.data);

      if (response.status === 200) {
        localStorage.setItem('forumengine-token', response.data.token);
        localStorage.setItem('forumengine-username', response.data.username);
        localStorage.setItem('forumengine-role', response.data.role);
        localStorage.setItem('forumengine-user_id', response.data.id);

        navigate('/');
      }

    } catch (err) {
      console.error('Error:', err);
      setError(err.response?.data?.message || 'Login failed');
    }

  };

  return (
    <div className="login-container">
      <div className="login-header">
        <div className="arrow-down"></div>
        <div className="text">Sign in to an account</div>
      </div>
      <form className="login-form" onSubmit={handleSubmit}>
        <div className="input-group">
          <label htmlFor="username">Username</label>
          <input type="username" id="username" placeholder="Enter username" value={username} onChange={(e) => setUsername(e.target.value)} />
        </div>
        <div className="input-group">
          <label htmlFor="password">Password</label>
          <input type="password" id="password" placeholder="Enter password" value={password} onChange={(e) => setPassword(e.target.value)} />
        </div>
        <div className="options">
          <label><input type="checkbox" />Keep me logged in</label>
          <Link className="options-link" to="#">Forgot password?</Link>
        </div>
        {error && <p className="error-message">{error}</p>}
        <button type="submit" className="login-btn">Login</button>
      </form>
      <div className="login-bottom">
        <div className="text">Don't have an account? <Link className="bottom-link" to="/signup">Sign up</Link></div>
      </div>
    </div>
  );
}

export default LoginPage;