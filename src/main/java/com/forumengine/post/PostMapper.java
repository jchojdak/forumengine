package com.forumengine.post;

import com.forumengine.post.dto.CreatePostDTO;
import com.forumengine.post.dto.PostDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PostMapper {

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

}
