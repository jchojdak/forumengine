import { useEffect, useState } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { API_URL } from '../../config';
import CommentInput from '../../components/CommentInput/CommentInput';
import useAuth from '../../hooks/useAuth';
import './PostPage.css';
import { FaEdit, FaTrash  } from "react-icons/fa";

const PostPage = () => {
  const { postId } = useParams();
  const [post, setPost] = useState(null);
  const [category, setCategory] = useState(null);
  const [comments, setComments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [sortOrder, setSortOrder] = useState('DESC');
  const [pageSize, setPageSize] = useState(10);
  const [pageNumber, setPageNumber] = useState(0);
  const [users, setUsers] = useState({});
  const [author, setAuthor] = useState(null);
  const { isLoggedIn, userId, token, roles } = useAuth();
  const navigate = useNavigate();

  const fetchComments = () => {
    axios.get(`${API_URL}/posts/${postId}/comments`, {
      params: { page: pageNumber, size: pageSize, sort: sortOrder }
    }).then(commentsResponse => {
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
      if (error.response && error.response.status === 404) {
        setComments([]);
      } else {
        setError("Failed to load comments. Please try again later.");
      }
      setLoading(false);
    });
  };

  useEffect(() => {
    const fetchPost = axios.get(`${API_URL}/posts/${postId}`);
    
    fetchPost.then(postResponse => {
      const postData = postResponse.data;
      setPost(postData);

      axios.get(`${API_URL}/users/${postData.authorId}`)
        .then(authorResponse => setAuthor(authorResponse.data))
        .catch(() => setAuthor(null));

      axios.get(`${API_URL}/categories/${postData.categoryId}`)
        .then(categoryResponse => setCategory(categoryResponse.data))
        .catch(() => setCategory(null));
    }).catch(error => {
      setError(error.message);
      setLoading(false);
    });

    fetchComments();
  }, [postId, sortOrder, pageSize, pageNumber]);

  const handleDeletePost = () => {
    console.log(token);
    axios.delete(`${API_URL}/posts/${postId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    }
    )
      .then(() => {
        alert('Post deleted successfully');
        navigate('/');
      })
      .catch(error => {
        alert('Failed to delete post');
        console.error(error);
      });
  };

  const handleDeleteComment = (commentId) => {
    axios.delete(`${API_URL}/posts/${postId}/comments/${commentId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    })
    .then(() => {
      alert('Comment deleted successfully');
      setComments(prevComments => prevComments.filter(comment => comment.id !== commentId));
    })
    .catch(error => {
      alert('Failed to delete comment');
      console.error(error);
    });
  };

  if (loading) return <p>Loading...</p>;
  if (error) return <p>Error: {error}</p>;
  if (!post) return <p>Post not found.</p>;

  const isAuthorOfPost = isLoggedIn && Number(userId) === Number(post.authorId);
  const isAdmin = roles.includes('ROLE_ADMIN');

  return (
    <div className="post-page">
      <h1><span className="yellow-slash">/</span> <Link className="nav-link" to={`/category/${category?.id}`}>#{category?.name}</Link> <span className="yellow-slash">/</span> {post.title}</h1>
      <p>{post.content}</p>
      <small>By <Link className="nav-link" to={`#`}>{author ? author.username : 'Unknown'}</Link> on {new Date(post.createdAt).toLocaleString()}</small>
      
      {(isAuthorOfPost || isAdmin) && (
        <>
          {isAuthorOfPost && <button className="edit-button" onClick={() => {}}><FaEdit /> Edit</button>}
          <button className="delete-button" onClick={handleDeletePost}><FaTrash /> Delete</button>
        </>
      )}

      <h2>Comments</h2>
      {isLoggedIn && (<CommentInput postId={postId} onCommentAdded={fetchComments} />)}

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
          <input type="number" className="page-number" value={pageNumber} onChange={(e) => setPageNumber(Number(e.target.value))} min="0" />
        </label>
      </div>
      {comments.length === 0 ? (
        <p>No comments found.</p>
      ) : (
        <ul className="comments">
          {comments.map(comment => {
            const isAuthorOfComment = isLoggedIn && Number(userId) === Number(comment.authorId);
            return (
              <li key={comment.id} className="comment-item">
                <p>{comment.content}</p>
                <small>By <Link className="nav-link-grey" to={`/user/${comment.authorId}`}>{users[comment.authorId] || `User ${comment.authorId}`}</Link> on {new Date(comment.createdAt).toLocaleString()}</small>
                {(isAuthorOfPost || isAdmin) && (
                  <>
                    {isAuthorOfComment && <button className="edit-button" onClick={() => {}}><FaEdit /> Edit</button>}
                    <button className="delete-button" onClick={() => handleDeleteComment(comment.id)}><FaTrash /> Delete</button>
                  </>
                )}
              </li>
            );
          })}
        </ul>
      )}

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
          <input type="number" className="page-number" value={pageNumber} onChange={(e) => setPageNumber(Number(e.target.value))} min="0" />
        </label>
      </div>
    </div>
  );
};

export default PostPage;