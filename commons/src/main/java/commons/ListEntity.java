package commons;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class ListEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    /**
     * Foreign Key to the Board on which this list was created.
     * Many-to-One relation
     */
    @ManyToOne(targetEntity = Board.class, cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "Board", referencedColumnName = "id", nullable = true)
    public Board board;

    /**
     * Getter for the board field
     *
     * @return the board that this list belongs to
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Getter for the name of the list
     *
     * @return name of the list
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the list's id
     *
     * @return the id of the list
     */
    public long getId() {
        return id;
    }

    /**
     * Setter for the board field.
     *
     * @param board board to be set
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    public String name;

    /**
     * ListEntity constructor
     */
    public ListEntity() {

    }

    /**
     * To string method
     *
     * @return a string containing all attributes
     */
    @Override
    public String toString() {
        return "ListEntity{" +
                "id=" + id +
                ", board=" + board +
                ", name='" + name + '\'' +
                '}';
    }

    /**
     * Equals method
     *
     * @param o object to be compared to
     * @return boolean value whether the 2 objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListEntity that = (ListEntity) o;
        return id == that.id && Objects.equals(board, that.board)
                && Objects.equals(name, that.name);
    }

    /**
     * Hash code method
     *
     * @return the hash code as an int
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, board, name);
    }
}
