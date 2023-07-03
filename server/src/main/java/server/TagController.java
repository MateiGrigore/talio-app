package server;

import commons.Board;
import commons.Tag;
import server.database.BoardRepository;
import server.database.TagRepository;

import java.util.ArrayList;
import java.util.List;

public class TagController {

    private BoardRepository boards;
    private TagRepository tags;

    /**
     * @param boards
     * @param tags
     */
    public TagController(BoardRepository boards, TagRepository tags) {
        this.boards = boards;
        this.tags = tags;
    }

    /**
     * @param tag   The tag to be added
     * @param board The board to which the tag is to be added
     * @return The tag that was added
     */
    public Tag addTagToBoard(Tag tag, Board board) {
        board.addTag(tag);
        boards.save(board);
        return tag;
    }

    /**
     * @param tag   The tag to be removed
     * @param board The board from which the tag is to be removed
     * @return The tag that was removed
     */
    public Tag removeTagFromBoard(Tag tag, Board board) {
        board.removeTag(tag);
        boards.save(board);
        return tag;
    }

    /**
     * @param boardID The id of the board
     * @return The tags associated with the board
     */
    public List<Tag> getTagsForBoard(long boardID) {
        return new ArrayList<>(boards.findById(boardID).get().tags);
    }

    /**
     * @param tag The tag to be added
     * @return The tag that was added
     */
    public Tag addTag(Tag tag) {
        return tags.save(tag);
    }


    /**
     * @param id The id of the tag
     * @return The tag with the given id
     */
    public Tag getTagByID(Long id) {
        return tags.findById(id).get();
    }
}
