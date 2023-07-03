package server;

import commons.Board;
import commons.ListEntity;
import server.database.BoardRepository;
import server.database.CardRepository;
import server.database.ListRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ListController {

    private ListRepository lists;
    private CardRepository cards;
    private BoardRepository boards;

    /**
     * @param lists
     * @param cards
     * @param boards
     */
    public ListController(ListRepository lists, CardRepository cards, BoardRepository boards) {
        this.lists = lists;
        this.cards = cards;
        this.boards = boards;
    }

    /**
     * @return All the lists in the database
     */
    public List<ListEntity> getAllLists() {
        return lists.findAll();
    }
    /**
     * @param id The id of the list
     * @return The list with the given id
     */
    public ListEntity getListByID(long id) {
        return lists.findById(id).orElse(null);
    }
    /**
     * @param boardId The id of the board
     * @return All the lists in the given board
     */
    public List<ListEntity> getListsByBoardId(long boardId) {

        Optional<Board> optionalBoard = boards.findById(boardId);
        if (optionalBoard.isEmpty())
            return new ArrayList<>();

        return lists.findAllListsByBoardId(boardId);
    }
    /**
     * @param list    The list to be added
     * @param boardId The id of the board to which the list will be added
     * @return True if the list was added successfully, false otherwise
     */
    public boolean addList(ListEntity list, long boardId) {
        Optional<Board> boardInDB = boards.findById(boardId);

        if (boardInDB.isEmpty()) return false;

        list.setBoard(boardInDB.get());
        lists.save(list);

        return true;
    }

    /**
     * @param list The list to be updated
     * @return The updated list
     */
    public ListEntity saveListOverwrite(ListEntity list) {
        return lists.save(list);
    }


    /**
     * @param list the list to be removed
     * @return True if the list was deleted successfully, false otherwise
     */
    public boolean deleteList(ListEntity list) {
        Optional<ListEntity> optionalList = lists.findById(list.id);
        if (optionalList.isEmpty()) return false;

        lists.deleteById(list.id);

        return true;
    }

    /**
     * Helper database-related method for finding the list
     * that resides at Nth position in a board,
     * counting from the left
     *
     * @param boardId ID of board on which the lists reside
     * @param n       specified as the list's position
     * @return ListEntity found at that position
     */
    public ListEntity getNthListFromBoard(long boardId, long n) {
        List<ListEntity> foundLists = lists.findAllListsByBoardIdDesc(boardId);
        if (n >= foundLists.size() || n < 0) return null;

        return foundLists.get((int) n);
    }


}
