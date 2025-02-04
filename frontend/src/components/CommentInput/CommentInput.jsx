import { useState } from 'react';
import useAuth from '../../hooks/useAuth';
import axios from 'axios';
import { API_URL } from '../../config';
import './CommentInput.css';

const CommentInput = ({ postId, onCommentAdded }) => {
  const { isLoggedIn } = useAuth();
  const [comment, setComment] = useState('');
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!comment.trim()) return;

    setLoading(true);
    setError(null);

    try {
      const token = localStorage.getItem('forumengine-token');
      await axios.post(`${API_URL}/posts/${postId}/comments`, {
        content: comment,
      }, {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      });

      setComment('');
      onCommentAdded();
    } catch (err) {
      setError('Failed to post comment. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  if (!isLoggedIn) {
    return <p>You must be logged in to post a comment.</p>;
  }

  return (
    <form onSubmit={handleSubmit} className="comment-form">
      <textarea
        value={comment}
        onChange={(e) => setComment(e.target.value)}
        placeholder="Write a comment..."
        required
      />
      <button type="submit" disabled={loading}>
        {loading ? 'Posting...' : 'Post comment'}
      </button>
      {error && <p className="error-message">{error}</p>}
    </form>
  );
};

export default CommentInput;
