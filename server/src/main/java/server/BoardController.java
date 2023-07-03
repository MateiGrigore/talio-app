package server;

import commons.Board;
import server.database.BoardRepository;
import java.util.List;
import java.util.Optional;

public class BoardController {
    private BoardRepository boards;

    /**
     * @param boards
     */
    public BoardController(BoardRepository boards) {
        this.boards = boards;
    }

    /**
     * @param board The board to be added
     * @return The added board
     */
    public Board addBoard(Board board) {
        Optional<Board> boardInDB = boards.findById(board.id);
        if (boardInDB.isPresent()) return null;
        else return boards.save(board);
    }



    /**
     * @return All the boards in the database
     */
    public List<Board> getAllBoards() {
        return boards.findAll();
    }

    /**
     * @param id The id of the board
     * @return The board with the given id
     */
    public Board getBoardByID(long id) {
        return boards.findById(id).orElse(null);
    }

    /**
     * Updating a board in the database
     *
     * @param board the board to be updated
     * @return The updated board
     */
    public Board saveBoardOverwrite(Board board) {
        return boards.save(board);
    }

    /**
     * Deleting a board from the database
     *
     * @param board the Board to be deleted
     * @return True if the board ws deleted successfully
     */
    public boolean deleteBoard(Board board) {
        Optional<Board> optionalBoard = boards.findById(board.id);
        if (optionalBoard.isEmpty()) return false;

        boards.deleteById(board.id);

        return true;
    }

    /**
     * Deletes all boards from the database
     *
     * @param boardList the list of boards from server
     */
    public void deleteAllBoards(List<Board> boardList) {
        boards.deleteAll();
    }

}
