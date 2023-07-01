package ru.practicum.shareit.item.comments;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

public class CommentMapper {

    private CommentMapper() {
    }

    public static Comment getComment(String text, Item item, User user) {
        return Comment.builder()
                .text(text)
                .item(item)
                .author(user)
                .created(LocalDateTime.now())
                .build();

    }

    public static CommentDto mapToCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .text(comment.getText())
                .build();
    }
}
