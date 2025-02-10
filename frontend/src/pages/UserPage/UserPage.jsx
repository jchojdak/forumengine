import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import axios from 'axios';
import { API_URL } from '../../config';
import useAuth from '../../hooks/useAuth';
import './UserPage.css';

const UserPage = () => {
  const { userId } = useParams();
  const { userId: loggedInUserId } = useAuth();
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    mobile: '',
    email: ''
  });
  
  useEffect(() => {
    axios.get(`${API_URL}/users/${userId}`)
      .then(response => {
        setUser(response.data);
        setFormData({
          firstName: response.data.firstName,
          lastName: response.data.lastName,
          mobile: response.data.mobile,
          email: response.data.email
        });
        setLoading(false);
      })
      .catch(error => {
        setError("Failed to load user data. Please try again later.");
        setLoading(false);
      });
  }, [userId]);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await axios.patch(`${API_URL}/users`, formData, {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('forumengine-token')}`
        }
      });
      alert('Profile updated successfully');
    } catch (err) {
      alert('Failed to update profile');
    }
  };

  if (loading) return <p>Loading...</p>;
  if (error) return <p>Error: {error}</p>;
  if (!user) return <p>User not found.</p>;

  return (
    <div className="user-page">
      <h1><span className="yellow-slash">/</span> #Profile <span className="yellow-slash">/</span> {user.username}</h1>
      <div className="user-group">
        <h2>User details</h2>
        <p><strong>Username:</strong> {user.username}</p>
        <p><strong>Full Name:</strong> {user.firstName} {user.lastName}</p>
        <p><strong>Mobile:</strong> {user.mobile}</p>
        <p><strong>Email:</strong> {user.email}</p>
        <p><strong>Registered at:</strong> {new Date(user.registeredAt).toLocaleString()}</p>
        <p><strong>Status:</strong> {user.active ? 'active' : 'inactive'}</p>
        <p><strong>Banned:</strong> {user.blocked ? 'yes' : 'no'}</p>
      </div>

      <div className="user-group">
        {userId === loggedInUserId && (
          <form onSubmit={handleSubmit}>
            <h2>Update my details</h2>
            <div className="input-group">
              <label>First name: <input type="text" name="firstName" placeholder="Enter your first name..." value={formData.firstName} onChange={handleChange} /></label>
            </div>
            <div className="input-group">
              <label>Last name: <input type="text" name="lastName" placeholder="Enter your last name..." value={formData.lastName} onChange={handleChange} /></label>
            </div>
            <div className="input-group">
              <label>Mobile: <input type="text" name="mobile" placeholder="Enter your mobile phone..." value={formData.mobile} onChange={handleChange} /></label>
            </div>
            <div className="input-group">
              <label>Email: <input type="email" name="email" placeholder="Enter your email..." value={formData.email} onChange={handleChange} /></label>
            </div>
            <button type="submit" className="update-btn">Update</button>
          </form>
        )}
      </div>
      <div className="user-group">
        <h2>Posts</h2>
          <Link className="nav-link"  to={`#`}><p>Not implemented yet!</p></Link>
      </div>
    </div>
  );
};

export default UserPage;
