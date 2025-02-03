import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import { API_URL } from '../../config';
import './PostPage.css';

const PostPage = () => {
  const { postId } = useParams();
  const [post, setPost] = useState(null);
  const [comments, setComments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [sortOrder, setSortOrder] = useState('ASC');
  const [pageSize, setPageSize] = useState(10);
  const [pageNumber, setPageNumber] = useState(0);
  const [users, setUsers] = useState({});
  const [author, setAuthor] = useState(null);

  useEffect(() => {
    const fetchPost = axios.get(`${API_URL}/posts/${postId}`);
    
    fetchPost.then(postResponse => {
      setPost(postResponse.data);
      return axios.get(`${API_URL}/users/${postResponse.data.authorId}`);
    }).then(authorResponse => {
      setAuthor(authorResponse.data);
    }).catch(error => {
      setError(error.message);
      setLoading(false);
    });
    
    const fetchComments = axios.get(`${API_URL}/posts/${postId}/comments`, {
      params: {
        page: pageNumber,
        size: pageSize,
        sort: sortOrder
      }
    });

    fetchComments.then(commentsResponse => {
      setComments(commentsResponse.data);
      setLoading(false);

      const userRequests = commentsResponse.data.map(comment => 
        axios.get(`${API_URL}/users/${comment.authorId}`)
      );

      Promise.all(userRequests)
        .then(userResponses => {
          const usersData = {};
          userResponses.forEach(response => {
            usersData[response.data.id] = response.data.username;
          });
          setUsers(usersData);
        })
        .catch(userError => console.error("Error fetching user data:", userError));
    }).catch(error => {
      setError(error.message);
      setLoading(false);
    });
  }, [postId, sortOrder, pageSize, pageNumber]);

  if (loading) return <p>Loading...</p>;
  if (error) return <p>Error: {error}</p>;
  if (!post) return <p>Post not found.</p>;

  return (
    <div className="post-page">
      <h1>{post.title}</h1>
      <p>{post.content}</p>
      <small>By {author ? author.username : 'Unknown'} on {new Date(post.createdAt).toLocaleString()}</small>
      <h2>Comments</h2>
      <div className="filters">
        <label>
          Sort by:
          <select value={sortOrder} onChange={(e) => setSortOrder(e.target.value)}>
            <option value="ASC">Oldest</option>
            <option value="DESC">Newest</option>
          </select>
        </label>
        <label>
          Items per page:
          <select value={pageSize} onChange={(e) => setPageSize(Number(e.target.value))}>
            <option value={5}>5</option>
            <option value={10}>10</option>
            <option value={20}>20</option>
          </select>
        </label>
        <label>
          Page number:
          <input type="number" value={pageNumber} onChange={(e) => setPageNumber(Number(e.target.value))} min="0" />
        </label>
      </div>
      <ul className="comments">
        {comments.map(comment => (
          <li key={comment.id} className="comment-item">
            <p>{comment.content}</p>
            <small>By {users[comment.authorId] || `User ${comment.authorId}`} on {new Date(comment.createdAt).toLocaleString()}</small>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default PostPage;