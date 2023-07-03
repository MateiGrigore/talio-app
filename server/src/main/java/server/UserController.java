package server;

import commons.Board;
import commons.User;
import server.database.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserController {
    private UserRepository users;

    /**
     * @param users
     */
    public UserController(UserRepository users) {
        this.users = users;
    }

    /**
     * @param user The user to be added
     * @return The added user
     */
    public User addUser(User user) {
        Optional<User> userInDB = users.findByUsername(user.username);
        if (userInDB.isPresent()) return null;
        else return users.save(user);
    }
    /**
     * @return All users in the database
     */
    public List<User> getAllUsers() {
        return users.findAll();
    }

    /**
     * @param username The username of the user
     * @return The user with the specified username
     */
    public User getUserByUsername(String username) {
        return users.findByUsername(username).get();
    }

    /**
     * @param user  The user to be added to the database
     * @param board The board to join
     * @return The User that was saved
     */
    public User joinBoard(User user, Board board) {
        user.joinedBoards.add(board);

        return users.save(user);
    }

    /**
     * @param username The username of the user
     * @return All the boards the user has joined
     */
    public List<Board> getJoinedBoardsByUsername(String username) {
        Optional<User> optionalUser = users.findByUsername(username);
        System.out.println(username + " : " + optionalUser);
        if (optionalUser.isEmpty()) return new ArrayList<>();
        else return users.findAllBoardsByUsername(username);
    }

    /**
     * @param user  The user who joins the board
     * @param board The board to be joined
     */
    public void leaveBoard(User user, Board board) {
        if (!user.joinedBoards.contains(board)) return;

        user.joinedBoards.remove(board);
        users.save(user);
    }

}
