package server.database;

import commons.Board;
import commons.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * @param username username of the user
     * @return list of boards the user has joined
     */
    @Query("SELECT u.joinedBoards FROM User u WHERE u.username = ?1")
    List<Board> findAllBoardsByUsername(String username);

    /**
     * @param username username of the user
     * @return user with the given username
     */
    @Query("SELECT u FROM User u WHERE u.username = ?1")
    Optional<User> findByUsername(String username);

//    @Query("update u SET newUser FROM User u WHERE u.username = ?1")
//    void changeByUsername(User newUser, String username);
}
