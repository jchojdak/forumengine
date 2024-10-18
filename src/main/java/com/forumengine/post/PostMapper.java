package com.forumengine.post;

import com.forumengine.comment.CommentDTO;
import com.forumengine.comment.CommentMapper;
import com.forumengine.post.dto.CreatePostDTO;
import com.forumengine.post.dto.PostCommentsDTO;
import com.forumengine.post.dto.PostDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostMapper {

    private final CommentMapper commentMapper;

    public PostMapper(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    public Post toPost(CreatePostDTO createPostDTO) {
        Post post = new Post();
        post.setTitle(createPostDTO.getTitle());
        post.setContent(createPostDTO.getContent());

        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        return post;
    }

    public PostDTO toPostDTO(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setContent(post.getContent());
        postDTO.setCategoryId(post.getCategory().getId());
        postDTO.setAuthorId(post.getAuthor().getId());

        postDTO.setUpdatedAt(post.getUpdatedAt());
        postDTO.setCreatedAt(post.getCreatedAt());

        return postDTO;
    }

    public List<PostDTO> toPostDTOs(List<Post> posts) {
        return posts.stream()
                .map(this::toPostDTO)
                .collect(Collectors.toList());
    }

    public PostCommentsDTO toPostCommentsDTO(Post post) {
        PostCommentsDTO postCommentsDTO = new PostCommentsDTO();
        postCommentsDTO.setId(post.getId());
        postCommentsDTO.setTitle(post.getTitle());
        postCommentsDTO.setContent(post.getContent());
        postCommentsDTO.setCategoryId(post.getCategory().getId());
        postCommentsDTO.setAuthorId(post.getAuthor().getId());

        postCommentsDTO.setUpdatedAt(post.getUpdatedAt());
        postCommentsDTO.setCreatedAt(post.getCreatedAt());

        List<CommentDTO> commentDTOs = commentMapper.toCommentDTOs(post.getComments());

        postCommentsDTO.setComments(commentDTOs);

        return postCommentsDTO;
    }

}
