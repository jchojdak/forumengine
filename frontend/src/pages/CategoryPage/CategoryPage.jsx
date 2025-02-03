import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import './CategoryPage.css';
import { Link } from 'react-router-dom';
import useAuth from '../../hooks/useAuth';
import { API_URL } from '../../config';

const CategoryPage = () => {
  const { categoryId } = useParams();
  const [posts, setPosts] = useState([]);
  const [category, setCategory] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [sortOrder, setSortOrder] = useState('ASC');
  const [pageSize, setPageSize] = useState(10);
  const [pageNumber, setPageNumber] = useState(0);
  const { isLoggedIn } = useAuth();

  useEffect(() => {
    const fetchCategory = axios.get(`${API_URL}/categories/${categoryId}`);
    const fetchPosts = axios.get(`${API_URL}/posts`, {
        params: {
          page: pageNumber,
          size: pageSize,
          sort: sortOrder,
          categoryId: categoryId
        }
      });
  
    Promise.all([fetchCategory, fetchPosts])
      .then(([categoryResponse, postsResponse]) => {
        setCategory(categoryResponse.data);
        setPosts(postsResponse.data);
        setLoading(false);
      })
      .catch(error => {
        setError(error.message);
        setLoading(false);
      });
  }, [categoryId, sortOrder, pageSize, pageNumber]);

  const handleSortChange = (e) => {
    setSortOrder(e.target.value);
  };

  const handlePageSizeChange = (e) => {
    setPageSize(Number(e.target.value));
  };

  const handlePageNumberChange = (e) => {
    setPageNumber(Number(e.target.value));
  };

  if (loading) return <p>Loading...</p>;
  if (error) return <p>Error: {error}</p>;
  if (!category) return <p>Category not found.</p>;

  return (
    <div className="category-page">
      <h1><span className="yellow-slash">/</span> #{category.name}</h1>
      <p>{category.description}</p>
      {isLoggedIn && <button className="add-button">Add new post</button>}
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
          <input type="number" value={pageNumber} onChange={handlePageNumberChange} min="0" />
        </label>
      </div>
      <ul className="posts">
        {posts.map(post => (
          <li key={post.id} className="post-item">
            <h3><Link to={`/post/${post.id}`}>{post.title}</Link></h3>
            <p>{post.content}</p>
            <small>Created at: {new Date(post.createdAt).toLocaleString()}</small>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default CategoryPage;