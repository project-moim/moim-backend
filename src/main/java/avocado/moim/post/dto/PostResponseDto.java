package avocado.moim.post.dto;

import avocado.moim.post.entity.Post;
import lombok.Getter;

@Getter
public class PostResponseDto {
    private Long id;
    private String author;
    private String title;
    private String content;
    private String location;

    public PostResponseDto(Post entity) {
        this.id = entity.getId();
        this.author = entity.getAuthor();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.location = entity.getLocation();
    }
}
