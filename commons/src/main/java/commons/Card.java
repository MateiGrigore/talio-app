package commons;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    public String name;

    public long rank;

    public String description;

    /**
     * Foreign Key to the ListEntity on which this card was created.
     * Many-to-One relation
     */
    @ManyToOne(targetEntity = ListEntity.class, cascade = CascadeType.MERGE,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "ListEntity", referencedColumnName = "id", nullable = true)
    public ListEntity list;

    @ManyToMany(fetch = FetchType.EAGER)
    public Set<Tag> tags = new HashSet<>();

    /**
     * No-arg constructor explicitly required
     **/
    public Card() {
    }

    /**
     * Setter for the list field
     *
     * @param list
     */
    public void setList(ListEntity list) {
        this.list = list;
    }

    /**
     * @return the list field
     */
    public long getId() {
        return id;
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
        Card card = (Card) o;
        return id == card.id && rank == card.rank &&
                Objects.equals(name, card.name) && Objects.equals(description, card.description)
                && Objects.equals(list, card.list);
    }

    /**
     * Hash code method
     *
     * @return the hash code as an int
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, list, rank);
    }

    /**
     * To string method
     *
     * @return a string containing all attributes
     */
    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rank=" + rank +
                ", list=" + list +
                ", description= " + description +
                '}';
    }
}
