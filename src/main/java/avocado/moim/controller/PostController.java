package avocado.moim.controller;

import avocado.moim.dto.PostListResponseDto;
import avocado.moim.dto.PostResponseDto;
import avocado.moim.dto.PostSaveRequestDto;
import avocado.moim.dto.PostUpdateRequestDto;
import avocado.moim.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @PostMapping("/post")
    public Long save(@RequestBody PostSaveRequestDto requestDto) {
        return postService.save(requestDto);
    }

    @PutMapping("/post/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostUpdateRequestDto requestDto) {
        return postService.update(id, requestDto);
    }

    @GetMapping("/post/{id}")
    public PostResponseDto findById(@PathVariable Long id) {
        return postService.findById(id);
    }

    @GetMapping("/post")
    public List<PostListResponseDto> findAllDesc() {
        return postService.findAllDesc();
    }

    @DeleteMapping("/post/{id}")
    public Long delete(@PathVariable Long id) {
        postService.delete(id);
        return id;
    }
}
