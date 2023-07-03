package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    Task task;

    @BeforeEach
    void setUp(){
        task = new Task();
    }

    @Test
    void setCard() {
        Card card = new Card();
        task.setCard(card);
        assertEquals(card, task.getCard());
    }

    @Test
    void setId() {
        task.setId(2L);
        assertEquals(2, task.getId());
    }

    @Test
    void getId() {
        task.setId(2L);
        assertEquals(2, task.getId());
    }

    @Test
    void equals(){
        Task task2 = new Task();
        assertEquals(task, task2);
    }

    @Test
    void hash(){
        Task task2 = new Task();
        assertEquals(task.hashCode(), task2.hashCode());
    }

    @Test
    void notEqualFalseComplete() {
        Task task1 = new Task();
        task1.completed = false;
        task1.id = 1;
        task1.text = "test text";

        Task task2 = new Task();
        task2.completed = true;
        task2.id = 1;
        task2.text = "test text";

        assertNotEquals(task1, task2);
    }

    @Test
    void trueEqualsTrueComplete() {
        Task task1 = new Task();
        task1.completed = true;
        task1.id = 1;
        task1.text = "test text";

        Task task2 = new Task();
        task2.completed = true;
        task2.id = 1;
        task2.text = "test text";

        assertEquals(task1, task2);
    }

    @Test
    void notEqualDifferentRanks() {
        Task task1 = new Task();
        task1.completed = true;
        task1.id = 1;
        task1.text = "test text";
        task1.rank = 0;

        Task task2 = new Task();
        task2.completed = true;
        task2.id = 1;
        task2.text = "test text";
        task2.rank = 1;

        assertNotEquals(task1, task2);
    }

    @Test
    void trueEqualSameRanks() {
        Task task1 = new Task();
        task1.completed = true;
        task1.id = 1;
        task1.text = "test text";
        task1.rank = 1;

        Task task2 = new Task();
        task2.completed = true;
        task2.id = 1;
        task2.text = "test text";
        task2.rank = 1;

        assertEquals(task1, task2);
    }

    @Test
    void toStringTestEmpty(){
        assertEquals("Task{id=0, text='null', rank=0, completed=false, card=null}", task.toString());
    }
}