import { useEffect, useState } from 'react';
import axios from 'axios';
import './HomePage.css';
import { Link } from 'react-router-dom';
import useAuth from '../../hooks/useAuth';
import { API_URL } from "../../config";

const HomePage = () => {
  const [categories, setCategories] = useState([]);
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const { isLoggedIn } = useAuth();

  useEffect(() => {
    const fetchCategories = axios.get(API_URL + '/categories');
    const fetchPosts = axios.get(API_URL + '/posts?page=0&size=10&sort=DESC');

    Promise.all([fetchCategories, fetchPosts])
      .then(([categoriesResponse, postsResponse]) => {
        setCategories(categoriesResponse.data);
        setPosts(postsResponse.data);
        setLoading(false);
      })
      .catch(error => {
        setError(error.message);
        setLoading(false);
      });
  }, []);

  if (loading) return <p>Loading...</p>;
  if (error) return <p>Error: {error}</p>;

  return (
    <div className="homepage">
      <h1><span className="yellow-slash">/</span> #Home</h1>
      <ul className="categories">
        {categories.map(category => (
          <li key={category.id} className="category-item">
            <div className="category-header">
              <h2><Link to={`/category/${category.id}`}>#{category.name}</Link></h2>
              {isLoggedIn && (
                <button className="add-button">Add new post</button>
              )}
            </div>
            <p>{category.description}</p>
            <ul className="posts">
              {posts.filter(post => post.categoryId === category.id).map(post => (
                <li key={post.id} className="post-item">
                  <h3><Link to={`/post/${post.id}`}>{post.title}</Link></h3>
                  <p>{post.content}</p>
                  <small>Created at: {new Date(post.createdAt).toLocaleString()}</small>
                </li>
              ))}
            </ul>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default HomePage;