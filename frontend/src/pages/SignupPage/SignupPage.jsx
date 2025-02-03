import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { API_URL } from "../../config";
import './SignupPage.css';

const SignupPage = () => {
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const validateForm = () => {
    if (username.trim().length < 4 || username.trim().length > 20) {
      return 'Username must be between 4 and 20 characters!';
    }

    if (password.length < 4 || password.length > 20) {
      return 'Password must be between 4 and 20 characters!';
    }

    const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    if (!emailRegex.test(email)) {
      return 'Please enter a valid email address!';
    }

    if (password !== confirmPassword) {
      return 'Passwords do not match!';
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

    const credentials = { username, password, email };

    try {
      const response = await axios.post(API_URL + '/auth/register', credentials);

      console.log('Status:', response.status);
      console.log('Response Data:', response.data);

      if (response.status === 200) {
        alert('Registration successful!');
        navigate('/login');
      }
    } catch (err) {
      console.error('Error:', err);
      setError(err.response?.data?.message || 'Registration failed');
    }
  };


  return (
    <div className="signup-container">
      <div className="signup-header">
        <div className="arrow-down"></div>
        <div className="text">Create an account</div>
      </div>
      <form className="signup-form" onSubmit={handleSubmit}>
        <div className="input-group">
          <label htmlFor="username">Username</label>
          <input type="text" id="username" value={username} placeholder="Enter username" onChange={(e) => setUsername(e.target.value)} required />
        </div>
        <div className="input-group">
          <label htmlFor="email">Email</label>
          <input type="email" id="email" value={email} placeholder="Enter email" onChange={(e) => setEmail(e.target.value)} required />
        </div>
        <div className="input-group">
          <label htmlFor="password">Password</label>
          <input type="password" id="password" placeholder="Enter password" value={password} onChange={(e) => setPassword(e.target.value)} required />
        </div>
        <div className="input-group">
          <label htmlFor="confirmPassword">Confirm password</label>
          <input type="password" id="confirmPassword" placeholder="Confirm password" value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} required />
        </div>
        {error && <p className="error-message">{error}</p>}
        <div className="input-group">
          <button type="submit" className="signup-btn">Sign Up</button>
        </div>
      </form>
      <div className="signup-bottom">
        <div className="text">Already have an account? <Link className="bottom-link" to="/login">Log in</Link></div>
      </div>
    </div>
  );
}

export default SignupPage;
