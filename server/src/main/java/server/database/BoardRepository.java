package server.database;

import commons.Board;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * BoardRepository type. Serves as an interface to save Boards.
 */
public interface BoardRepository extends JpaRepository<Board, Long> {
}
