package com.forumengine.comment;

import com.forumengine.comment.dto.CommentDTO;
import com.forumengine.comment.dto.CreateCommentDTO;
import com.forumengine.exception.EntityNotFoundException;
import com.forumengine.post.Post;
import com.forumengine.post.PostRepository;
import com.forumengine.user.User;
import com.forumengine.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private static final String SORT_PROPERTIES = "createdAt";

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
}
