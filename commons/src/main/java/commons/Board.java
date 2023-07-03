package commons;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @ManyToMany(mappedBy = "joinedBoards", fetch = FetchType.EAGER)
    Set<User> joinedUsers = new HashSet<>();

    @OneToMany(targetEntity = Tag.class, cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "Tag", referencedColumnName = "id", nullable = true)
    public Set<Tag> tags = new HashSet<>();

    public String title;

    /**
     * Board constructor
     */
    public Board() {
    }

    /**
     * Method for deleting any references that users might have to the deleted board
     * Necessary for safely deleting a board without violating constraints
     */
    @PreRemove
    private void removeBoardFromUsers() {
        for (User user : joinedUsers) {
            user.joinedBoards.remove(this);
        }
    }

    /**
     * Equals method
     *
     * @param o other object to compare to
     * @return whether or not the two objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Board board = (Board) o;

        if (id != board.id) return false;
        return Objects.equals(title, board.title);
    }

    /**
     * Hash code method
     *
     * @return the hash code as an int
     */
    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
    }

    /**
     * To string method
     *
     * @return a string containing all attributes
     */
    @Override
    public String toString() {
        return "Board{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }

    /**
     * @param tag The tag to add to the board
     */
    public void addTag(Tag tag) {
        tags.add(tag);
    }

    /**
     * @param tag The tag to remove from the board
     */
    public void removeTag(Tag tag) {
        for (Tag t : tags) {
            if (Objects.equals(t.id, tag.id)) {
                tags.remove(t);
                break;
            }
        }
    }
}
