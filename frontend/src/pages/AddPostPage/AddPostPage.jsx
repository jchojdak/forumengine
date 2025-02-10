import { API_URL } from "../../config";
import useAuth from '../../hooks/useAuth';
import React, { useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import axios from 'axios';
import './AddPostPage.css';

const AddPostPage = () => {
  const { isLoggedIn } = useAuth();
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const { categoryId } = useParams();
  const [selectedCategoryId, setSelectedCategoryId] = useState(categoryId || '');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (!title.trim() || !content.trim() || !categoryId) {
      setError('All fields are required!');
      return;
    }

    const postData = {
      categoryId: parseInt(categoryId, 10),
      title,
      content
    };

    try {
      const token = localStorage.getItem('forumengine-token');
      const response = await axios.post(`${API_URL}/posts`, postData, {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });
      
      if (response.status === 200) {
        alert('Post added successfully!');
        navigate('/post/' + response.data.id);
      }
    } catch (err) {
      console.error('Error:', err);
      setError(err.response?.data?.message || 'Failed to add post');
    }
  };

  if (!isLoggedIn) {
    return <p>You must be logged in to post a comment.</p>;
  }

  return (
    <div className="add-post-container">
      <h2>Create a new post</h2>
      <form className="add-post-form" onSubmit={handleSubmit}>
        <div className="input-group">
          <label htmlFor="categoryId">Category ID</label>
          <input 
            type="number" 
            id="categoryId" 
            placeholder="Enter category ID"
            value={selectedCategoryId} 
            onChange={(e) => setSelectedCategoryId(e.target.value)} 
            required 
          />
        </div>
        <div className="input-group">
          <label htmlFor="title">Title</label>
          <input 
            type="text" 
            id="title" 
            placeholder="Enter title"
            value={title} 
            onChange={(e) => setTitle(e.target.value)} 
            required 
          />
        </div>
        <div className="input-group">
          <label htmlFor="content">Content</label>
          <textarea 
            id="content" 
            placeholder="Enter content"
            value={content} 
            onChange={(e) => setContent(e.target.value)} 
            required 
          ></textarea>
        </div>
        {error && <p className="error-message">{error}</p>}
        <button type="submit" className="submit-btn">Add Post</button>
      </form>
    </div>
  );
};

export default AddPostPage;
