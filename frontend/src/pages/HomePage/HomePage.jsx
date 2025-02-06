import { useEffect, useState } from 'react';
import axios from 'axios';
import './HomePage.css';
import { Link } from 'react-router-dom';
import useAuth from '../../hooks/useAuth';
import { API_URL } from '../../config';
import { FaPlus } from "react-icons/fa";

const HomePage = () => {
  const [categories, setCategories] = useState([]);
  const [posts, setPosts] = useState({});
  const [authors, setAuthors] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const { isLoggedIn } = useAuth();

  useEffect(() => {
    axios.get(`${API_URL}/categories`)
      .then(response => {
        setCategories(response.data);
        return Promise.all(
          response.data.map(category =>
            axios.get(`${API_URL}/posts?page=0&size=2&sort=DESC&categoryId=${category.id}`)
              .then(res => ({ categoryId: category.id, posts: res.data }))
          )
        );
      })
      .then(postResponses => {
        const postsData = {};
        const authorRequests = [];

        postResponses.forEach(({ categoryId, posts }) => {
          postsData[categoryId] = posts;

          posts.forEach(post => {
            authorRequests.push(
              axios.get(`${API_URL}/users/${post.authorId}`).then(res => ({
                authorId: post.authorId,
                username: res.data.username
              }))
            );
          });
        });

        setPosts(postsData);

        return Promise.all(authorRequests);
      })
      .then(authorResponses => {
        const authorsData = {};
        authorResponses.forEach(({ authorId, username }) => {
          authorsData[authorId] = username;
        });
        setAuthors(authorsData);
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
                <Link to={`/post/add/${category.id}`}>
                  <button className="add-button"><FaPlus /> Add new post</button>
                </Link>
              )}
            </div>
            <p>{category.description}</p>
            <ul className="posts">
              {(posts[category.id] || []).map(post => (
                <li key={post.id} className="post-item">
                  <h3><Link to={`/post/${post.id}`}>{post.title}</Link></h3>
                  <p>{post.content}</p>
                  <small>By <Link className="nav-link-grey" to={`/user/${post.authorId}`}>{authors[post.authorId] || 'Unknown'}</Link> on {new Date(post.createdAt).toLocaleString()}</small>
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
