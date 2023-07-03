package commons;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public String username;

    @ManyToMany(fetch = FetchType.EAGER)
    public Set<Board> joinedBoards = new HashSet<>();

    public String password;

    /**
     * User constructor
     */
    public User() {
    }

    /**
     * @param username username of the user
     * @param password password of the user
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * @return the id of the user
     */
    public Set<Board> getJoinedBoards() {
        return joinedBoards;
    }

    /**
     * @param joinedBoards the boards the user has joined
     */
    public void setJoinedBoards(Set<Board> joinedBoards) {
        this.joinedBoards = joinedBoards;
    }

    /**
     * @return the id of the user
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", joinedBoards=" + joinedBoards +
                ", password='" + password + '\'' +
                '}';
    }

    /**
     * @param o other object to compare to
     * @return whether or not the two objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id)
                && Objects.equals(username, user.username)
                && Objects.equals(joinedBoards, user.joinedBoards)
                && Objects.equals(password, user.password);
    }

    /**
     * @return the hash code as an int
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, username, joinedBoards, password);
    }

    /**
     * @return the username of the user
     */
    public Object getUsername() {
        return username;
    }
}
