package commons;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    public String text;

    public long rank;

    public boolean completed;

    /**
     * Foreign Key to the Card on which this Task was created.
     * Many-to-One relation
     */
    @ManyToOne(targetEntity = Card.class, cascade = CascadeType.DETACH)
    @JoinColumn(name = "Card", referencedColumnName = "id")
    public Card card;

    /**
     * No-arg constructor explicitly required
     **/
    public Task() {

    }

    /**
     * sett for the card attribute
     *
     * @param card card to be set
     */
    public void setCard(Card card) {
        this.card = card;
    }

    /**
     * setter for the id
     *
     * @param id id to be set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * get for the id
     *
     * @return hte task's id
     */
    public Long getId() {
        return id;
    }

    /**
     * getter for the card
     *
     * @return the card that this task belongs to
     */
    public Card getCard() {
        return card;
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

        Task task = (Task) o;

        if (id != task.id) return false;
        if (rank != task.rank) return false;
        if (completed != task.completed) return false;
        if (!Objects.equals(text, task.text)) return false;
        return Objects.equals(card, task.card);
    }


    /**
     * Hashing method
     *
     * @return the hash code as an int
     */
    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (int) (rank ^ (rank >>> 32));
        result = 31 * result + (completed ? 1 : 0);
        result = 31 * result + (card != null ? card.hashCode() : 0);
        return result;
    }

    /**
     * To string method
     *
     * @return a string containing all attributes
     */
    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", rank=" + rank +
                ", completed=" + completed +
                ", card=" + card +
                '}';
    }
}
