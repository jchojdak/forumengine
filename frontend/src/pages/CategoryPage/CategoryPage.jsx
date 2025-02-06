import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import axios from 'axios';
import './CategoryPage.css';
import useAuth from '../../hooks/useAuth';
import { API_URL } from '../../config';
import { FaPlus } from "react-icons/fa";

const CategoryPage = () => {
  const { categoryId } = useParams();
  const [posts, setPosts] = useState([]);
  const [authors, setAuthors] = useState({});
  const [category, setCategory] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [sortOrder, setSortOrder] = useState('DESC');
  const [pageSize, setPageSize] = useState(10);
  const [pageNumber, setPageNumber] = useState(0);
  const { isLoggedIn } = useAuth();

  useEffect(() => {
    const fetchCategory = axios.get(`${API_URL}/categories/${categoryId}`);
    const fetchPosts = axios.get(`${API_URL}/posts`, {
      params: { page: pageNumber, size: pageSize, sort: sortOrder, categoryId }
    });

    Promise.all([fetchCategory, fetchPosts])
      .then(([categoryResponse, postsResponse]) => {
        setCategory(categoryResponse.data);
        setPosts(postsResponse.data);
        setLoading(false);

        const authorRequests = postsResponse.data.map(post => 
          axios.get(`${API_URL}/users/${post.authorId}`)
        );

        Promise.all(authorRequests)
          .then(authorResponses => {
            const authorsData = {};
            authorResponses.forEach(response => {
              authorsData[response.data.id] = response.data.username;
            });
            setAuthors(authorsData);
          })
          .catch(err => console.error("Error fetching authors:", err));
      })
      .catch(error => {
        setError(error.message);
        setLoading(false);
      });
  }, [categoryId, sortOrder, pageSize, pageNumber]);

  const handleSortChange = (e) => setSortOrder(e.target.value);
  const handlePageSizeChange = (e) => setPageSize(Number(e.target.value));
  const handlePageNumberChange = (e) => setPageNumber(Number(e.target.value));

  if (loading) return <p>Loading...</p>;
  if (error) return <p>Error: {error}</p>;
  if (!category) return <p>Category not found.</p>;

  return (
    <div className="category-page">
      <h1><span className="yellow-slash">/</span> <Link className="nav-link" to={`/category/${category?.id}`}>#{category?.name}</Link></h1>
      <p>{category.description}</p>
      {isLoggedIn && <Link to={`/post/add/${category.id}`}><button className="add-button"><FaPlus /> Add new post</button></Link>}
      
      <div className="filters">
        <label>
          Sort by:
          <select value={sortOrder} onChange={handleSortChange}>
            <option value="ASC">Oldest</option>
            <option value="DESC">Newest</option>
          </select>
        </label>
        <label>
          Items per page:
          <select value={pageSize} onChange={handlePageSizeChange}>
            <option value={5}>5</option>
            <option value={10}>10</option>
            <option value={20}>20</option>
          </select>
        </label>
        <label>
          Page number:
          <input type="number" className="page-number" value={pageNumber} onChange={handlePageNumberChange} min="0" />
        </label>
      </div>

      <ul className="posts">
        {posts.map(post => (
          <li key={post.id} className="post-item">
            <h3><Link to={`/post/${post.id}`}>{post.title}</Link></h3>
            <p>{post.content}</p>
            <small>By <Link className="nav-link-grey" to={`/user/${post.authorId}`}>{authors[post.authorId] || 'Unknown'}</Link> on {new Date(post.createdAt).toLocaleString()}</small>
          </li>
        ))}
      </ul>

      <div className="filters">
        <label>
          Sort by:
          <select value={sortOrder} onChange={handleSortChange}>
            <option value="ASC">Oldest</option>
            <option value="DESC">Newest</option>
          </select>
        </label>
        <label>
          Items per page:
          <select value={pageSize} onChange={handlePageSizeChange}>
            <option value={5}>5</option>
            <option value={10}>10</option>
            <option value={20}>20</option>
          </select>
        </label>
        <label>
          Page number:
          <input type="number" className="page-number" value={pageNumber} onChange={handlePageNumberChange} min="0" />
        </label>
      </div>
    </div>
  );
};

export default CategoryPage;
