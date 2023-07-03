package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ListEntityTest {

    ListEntity listEntity;

    @BeforeEach
    void setUp(){
        listEntity = new ListEntity();
    }

    @Test
    void setBoard() {
        Board board = new Board();
        listEntity.setBoard(board);
        assertEquals(board,listEntity.getBoard());
    }

    @Test
    void getBoard(){
        assertEquals(null, listEntity.getBoard());
    }

    @Test
    void testToString() {
        assertEquals("ListEntity{id=0, board=null, name='null'}", listEntity.toString());
    }

    @Test
    void testEquals() {
        ListEntity listEntity1 = new ListEntity();
        assertEquals(listEntity1,listEntity);
    }

    @Test
    void testHashCode() {
        ListEntity listEntity1 = new ListEntity();
        assertEquals(listEntity1.hashCode(),listEntity.hashCode());
    }
}