package com.forumengine.comment;

import com.forumengine.comment.dto.CommentDTO;
import com.forumengine.comment.dto.CreateCommentDTO;
import com.forumengine.exception.AccessDeniedException;
import com.forumengine.exception.CommentNotBelongToPostException;
import com.forumengine.exception.EntityNotFoundException;
import com.forumengine.post.Post;
import com.forumengine.post.PostRepository;
import com.forumengine.security.CustomUserDetails;
import com.forumengine.user.User;
import com.forumengine.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private static final String SORT_PROPERTIES = "createdAt";
    private static final String ACCESS_DENIED_MESSAGE = "You do not have permission to delete this post.";
    private static final String POST_NOT_FOUND_MESSAGE = "Post %s";
    private static final String COMMENT_NOT_FOUND_MESSAGE = "Comment %s";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private final CommentMapper commentMapper;

    public CommentDTO createComment(Long postId, CreateCommentDTO createCommentDTO, String authorName) {
        String content = createCommentDTO.getContent();

        Optional<Post> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            throw new EntityNotFoundException(postId.toString());
        }

        Optional<User> author = userRepository.findByUsername(authorName);
        if (author.isEmpty()) {
            throw new EntityNotFoundException(authorName);
        }

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setPost(post.get());
        comment.setAuthor(author.get());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);

        return commentMapper.toCommentDTO(savedComment);
    }

    public List<CommentDTO> getAllComments(Long postId, Integer page, Integer size, Sort.Direction sort) {
        int pageNumber = (page != null && page >= 0) ? page : 0;
        int pageSize = (size != null && size >= 1) ? size : 10;
        Sort.Direction sortDirection = (sort != null) ? sort : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortDirection, SORT_PROPERTIES));

        Page<Comment> commentsPage = commentRepository.findAllByPostId(postId, pageable);

        if (commentsPage.isEmpty()) throw new EntityNotFoundException(postId.toString());

        return commentMapper.toCommentDTOs(commentsPage.getContent());
    }

    public void deleteCommentById(Long postId, Long commentId, Authentication auth) {
        Optional<Post> post = postRepository.findById(postId);

        if (post.isEmpty()) {
            throw new EntityNotFoundException(postId.toString());
        }

        Optional<Comment> comment = commentRepository.findById(commentId);

        if (comment.isEmpty()) {
            throw new EntityNotFoundException(COMMENT_NOT_FOUND_MESSAGE.formatted(commentId));
        }

        if (!comment.get().getPost().getId().equals(postId)) {
            throw new CommentNotBelongToPostException(commentId, postId);
        }

        Long loggedInUserId = getUserIdFromAuthentication(auth);
        Long authorId = comment.get().getAuthor().getId();

        if (isAuthor(loggedInUserId, authorId) || isAdmin(auth)) {
            commentRepository.deleteById(commentId);
        } else {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }
    }

    private boolean isAdmin(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(authentication -> authentication.getAuthority().equals(ROLE_ADMIN));
    }

    private boolean isAuthor(Long userId, Long authorId) {
        return userId.equals(authorId);
    }

    private Long getUserIdFromAuthentication(Authentication auth) {
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        return userDetails.getId();
    }

    public CommentDTO getCommentById(Long postId, Long commentId) {
        Optional<Post> post = postRepository.findById(postId);

        if (post.isEmpty()) {
            throw new EntityNotFoundException(POST_NOT_FOUND_MESSAGE.formatted(postId));
        }

        Optional<Comment> comment = commentRepository.findById(commentId);

        if (comment.isEmpty()) {
            throw new EntityNotFoundException(COMMENT_NOT_FOUND_MESSAGE.formatted(commentId));
        }

        if (!comment.get().getPost().getId().equals(postId)) {
            throw new CommentNotBelongToPostException(commentId, postId);
        }

        return commentMapper.toCommentDTO(comment.get());
    }
}
