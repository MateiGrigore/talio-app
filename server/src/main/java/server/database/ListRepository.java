package server.database;

import commons.ListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ListRepository extends JpaRepository<ListEntity, Long> {
    /** Return all lists belonging to a certain board
     * @param boardId id of the board
     * @return all the lists belonging
     */
    @Query("SELECT l FROM ListEntity l WHERE l.board.id = ?1")
    List<ListEntity> findAllListsByBoardId(long boardId);

    /**
     * Helper query that finds all lists in the board,
     * ordered by their ID;
     *
     * this helps as the order of IDs of lists in a board
     * is always increasing when traversing from left to right
     *
     * (that is, the farther away from the left edge of the board,
     * the higher the ID of the list residing at that position)
     *
     * @param boardId ID of board on which the search is executed
     * @return List of all ListEntities that match the board id
     */
    @Query("SELECT l FROM ListEntity l WHERE l.board.id = ?1 ORDER BY l.id ASC")
    List<ListEntity> findAllListsByBoardIdDesc(long boardId);
}
