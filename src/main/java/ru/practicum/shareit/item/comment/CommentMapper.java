package ru.practicum.shareit.item.comment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class CommentMapper {
    public static Comment mapToComment(CommentDto request) {
        log.info("Запустили метод корвертации коммента из комментдто в коммент в комментмаппере");
        Comment comment = new Comment();
        if (request.getId() != null) {
            comment.setId(request.getId());
        }
        if (request.getText() != null) {
            comment.setText(request.getText());
        }
        if (request.getItem() != null) {
            comment.setItem(request.getItem());
        }
        if (request.getAuthor() != null) {
            comment.setAuthor(request.getAuthor());
        }
        comment.setCreated(LocalDateTime.now());

        return comment;
    }

    public static CommentDto mapToCommentDto(Comment comment) {
        log.info("Запустили метод корвертации коммента из коммент в комментдто в комментмаппере");

        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setItem(comment.getItem());
        dto.setAuthor(comment.getAuthor());
        dto.setCreated(comment.getCreated());

        return dto;
    }

    public static CommentDtoResponse mapToCommentDtoResponse(Comment comment) {
        log.info("Запустили метод корвертации коммента из коммент в комментдтореспонс в комментмаппере");

        CommentDtoResponse dto = new CommentDtoResponse();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setItem(comment.getItem());
        dto.setAuthorName(comment.getAuthor().getName());
        dto.setCreated(comment.getCreated());

        return dto;
    }
}
