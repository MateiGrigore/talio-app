package commons;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public int r;
    public int g;
    public int b;

    public String name;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    public Board board;

    @ManyToMany(mappedBy = "tags", fetch = FetchType.EAGER)
    public Set<Card> cards = new HashSet<>();

    /**
     * Tag constructor
     */
    public Tag() {
    }

    /**
     * @param name The name of the tag
     * @param r    The red value of the tag
     * @param g    The green value of the tag
     * @param b    The blue value of the tag
     */
    public Tag(String name, int r, int g, int b) {
        this.name = name;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    /**
     * @param board The board to add the tag to
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * @return The name of the tag
     */
    public String getName() {
        return name;
    }

    /**
     * @param o The object to compare to
     * @return True if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return r == tag.r && g == tag.g && b == tag.b && Objects.equals(id, tag.id)
                && Objects.equals(name, tag.name) && Objects.equals(board, tag.board)
                && Objects.equals(cards, tag.cards);
    }

    /**
     * @return The hashcode of the tag
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, r, g, b, name, board, cards);
    }

    /**
     * @return The string representation of the tag
     */
    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", r=" + r +
                ", g=" + g +
                ", b=" + b +
                ", name='" + name + '\'' +
                ", board=" + board +
                ", cards=" + cards +
                '}';
    }
}
