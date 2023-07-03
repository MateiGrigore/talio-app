package server;

import commons.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;
import server.database.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Controller
@RequestMapping("/api")
public class AppController {
    /**
     * TODO: clean up
     * TODO: add javadoc
     * TODO: add checks to see if ids exist for an embedded entity
     * (ex. card being created in list, check if list exists)
     * TODO: unify the access modifiers
     * TODO: add tests
     * TODO: allow for editing
     * TODO: implement deletion
     */

    private BoardController boardController;
    private ListController listController;
    private CardController cardController;
    private UserController userController;
    private TagController tagController;
    private TaskController taskController;

    private RandomPassword randomPassword;
    private Map<Object, Consumer<Card>> listeners = new HashMap<>();

    private String serverPassword;

    /**
     * Dependency injection:
     * sets-up the Controller with the correct repositories
     *
     * @param boards         Board Repository type
     * @param lists          List Repository type
     * @param cards          Card Repository type
     * @param tasks          Task Repository type
     * @param users          User Repository type
     * @param randomPassword The service that generates the server password
     * @param tags           Tag Repository type
     */

    public AppController(BoardRepository boards, ListRepository lists,
                         CardRepository cards, TaskRepository tasks, UserRepository users,
                         TagRepository tags,
                         RandomPassword randomPassword) {
        this.boardController = new BoardController(boards);
        this.listController = new ListController(lists, cards, boards);
        this.cardController = new CardController(cards, lists, listController);
        this.userController = new UserController(users);
        this.tagController = new TagController(boards, tags);
        this.taskController = new TaskController(tasks, cards, cardController);
        this.randomPassword = randomPassword;
    }

    /**
     * @param user  User to be added
     * @param title Title of the board to be added
     * @return User that was added
     */
    @MessageMapping("/add-board/{title}")
    @SendTo("/topic/join-board")
    public User addJoinBoard(User user, @DestinationVariable String title) {

        user = userController.getUserByUsername(user.username);

        Board board = new Board();
        board.title = title;

        boardController.addBoard(board);

        return userController.joinBoard(user, board);
    }


    /**
     * @param tag     Tag to be added
     * @param boardID ID of the board to be added to
     * @return Tag that was added
     */
    @MessageMapping("/add-tag/{boardID}")
    public Tag addTag(Tag tag, @DestinationVariable String boardID) {
        Tag newTag = tagController.addTag(tag);
        Board board = boardController.getBoardByID(Long.parseLong(boardID));
        return tagController.addTagToBoard(newTag, board);
    }

    /**
     * @param tag     Tag to be removed
     * @param boardID ID of the board to be removed from
     * @return Tag that was removed
     */
    @MessageMapping("/remove-tag/{boardID}")
    public Tag removeTag(Tag tag, @DestinationVariable String boardID) {
        System.out.println("Removing tag: " + tag + " from board: " + boardID);
        Board board = boardController.getBoardByID(Long.parseLong(boardID));
        Tag t = tagController.getTagByID(tag.id);
        return tagController.removeTagFromBoard(t, board);
    }

    /**
     * @param user    User to be added
     * @param boardID ID of the board to be joined
     * @return User that was added
     */
    @MessageMapping("/join-board/{boardID}")
    @SendTo("/join-board")
    public User joinBoard(User user, @DestinationVariable String boardID) {

        user = userController.getUserByUsername(user.username);

        Board board = boardController.getBoardByID(Long.parseLong(boardID));

        return userController.joinBoard(user, board);
    }

    /**
     * @param user    User from which the board is to be left
     * @param boardID ID of the board to be left
     */
    @MessageMapping("/leave-board/{boardID}")
    public void leaveBoard(User user, @DestinationVariable String boardID) {
        user = userController.getUserByUsername(user.username);

        Board board = boardController.getBoardByID(Long.parseLong(boardID));

        userController.leaveBoard(user, board);
    }

    /**
     * @param user User to be added
     * @return User that was added
     */
    @MessageMapping("/add-user")
    @SendTo("/users")
    public User addUser(User user) {
        User u = userController.addUser(user);
        System.out.println("User: " + u + " added");
        return u;
    }

    /**
     * Websocket endpoint for adding a board,
     * broadcasts to all clients subscribed to /topic/new-board
     *
     * @param board Board to be added
     * @return Board that was added
     */
    @MessageMapping("/add-board")
    @SendTo("/new-board")
    public Board addBoard(Board board) {
        return boardController.addBoard(board);
    }

    /**
     * Websocket endpoint for adding a list,
     * broadcasts to all clients subscribed to /topic/new-list/{boardID}
     *
     * @param list    List to be added
     * @param boardID ID of the board to add the list to
     * @return List that was added
     */
    @MessageMapping("/add-list/{boardID}")
    @SendTo("/topic/new-list/{boardID}")
    public ListEntity addList(ListEntity list, @DestinationVariable String boardID) {
        listController.addList(list, Long.parseLong(boardID));
        return listController.getListByID(list.getId());
    }

    /**
     * Services drag & drop actions after user modifies card's rank
     *
     * @param card    card that was moved
     * @param newRank rank to which the card was moved
     * @param boardID ID of board on which the card was moved
     * @return Card that was modified
     */
    @MessageMapping("/move-card/{newRank},{boardID}")
    @SendTo("/topic/move-card/{boardID}")
    public Card dragDropCard(
            Card card,
            @DestinationVariable long newRank,
            @DestinationVariable long boardID
    ) {
        System.out.println("Actually sent");
        return cardController.moveCardAndRestructure(card, newRank);
    }

    /**
     * Moves a task to a new rank
     *
     * @param task    Task to be moved
     * @param newRank Rank to which the task is being moved
     * @return Task affected
     */
    @MessageMapping("/move-task/{newRank},{cardID}")
    @SendTo("/topic/move-task/{cardID}")
    public Task moveTask(
            Task task,
            @DestinationVariable long newRank
    ) {
        return taskController.moveTaskAndRestructure(task, newRank);
    }

    /**
     * Websocket endpoint for adding a card,
     * broadcasts to all clients subscribed to /topic/new-card/{boardID}
     *
     * @param card Card to be added
     * @return Card that was added
     */
    @MessageMapping("/add-card/{boardID}")
    @SendTo("/topic/new-card/{boardID}")
    public Card addCard(Card card) {
        cardController.addCard(card);
        return cardController.getCardByID(card.getId());
    }

    /**
     * Websocket endpoint for adding a task,
     * broadcasts to all clients subscribed to /topic/new-task/{cardID}
     *
     * @param task Task to be added
     * @return Task that was added
     */
    @MessageMapping("/add-task/{cardID}")
    @SendTo("/topic/new-task/{cardID}")
    public Task addTask(Task task) {
        taskController.addTask(task, task.card.id);
        return taskController.getTasksByID(task.getId());
    }

    /**
     * Websocket endpoint for editing a list
     *
     * @param list the updated list
     * @return the edited list
     */
    @MessageMapping("/edit-list/{boardID}")
    @SendTo("/topic/edit-list/{boardID}")
    public ListEntity editList(ListEntity list) {
        return listController.saveListOverwrite(list);
    }

    /**
     * Websocket endpoint for removing a list
     *
     * @param list the list to be deleted
     * @return the deleted list
     */
    @MessageMapping("/remove-list/{boardID}")
    @SendTo("/topic/remove-list/{boardID}")
    public long removeList(ListEntity list) {
        if (listController.deleteList(list)) {
            return list.getId();
        } else {
            return -1;
        }
    }

    /**
     * Websocket endpoint for removing a card
     *
     * @param card Card to be removed
     * @return ID of the card that was removed
     */
    @MessageMapping("/remove-card/{boardID}")
    @SendTo("/topic/remove-card/{boardID}")
    public long removeCard(Card card) {
        if (cardController.deleteCard(card)) {
            //send a card to updates after deleting
            listeners.forEach((k, l) -> l.accept(card));
            return card.getId();
        } else {
            return -1;
        }
    }

    /**
     * Websocket endpoint for removing a task from a card
     *
     * @param task Task that is to be removed
     * @return Task affected
     */
    @MessageMapping("/remove-task/{cardID}")
    @SendTo("/topic/remove-task/{cardID}")
    public Task removeTask(Task task) {
        System.out.println("HERE DELETING TASK");
        if (taskController.deleteTask(task)) {
            return task;
        } else {
            return null;
        }
    }

    /**
     * Websocket endpoint for editing a task's text field
     *
     * @param task Task that is to be affected
     * @return Task modified
     */
    @MessageMapping("/modify-task/{cardID}")
    @SendTo("/topic/modify-task/{cardID}")
    public Task modifyTask(Task task) {
        if (taskController.editTask(task)) {
            return task;
        } else {
            return null;
        }
    }

    /**
     * Gets any updates made to cards
     *
     * @return the card that suffered modifications
     */
    @GetMapping("/cards/updates")
    public DeferredResult<ResponseEntity<Card>> getUpdates() {
        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        var res = new DeferredResult<ResponseEntity<Card>>(500L, noContent);

        var key = new Object();
        listeners.put(key, card -> {
            res.setResult(ResponseEntity.ok(card));
        });
        res.onCompletion(() -> {
            listeners.remove(key);
        });

        return res;
    }

    /**
     * Websocket endpoint for removing a board
     *
     * @param board Board to be removed
     * @return id of the Board that was removed
     */
    @MessageMapping("/remove-board")
    @SendTo("/topic/remove-board")
    public long removeBoard(Board board) {
        if (boardController.deleteBoard(board)) {
            return board.id;
        } else {
            return -1;
        }
    }

    /**
     * Websocket endpoint for editing a card
     *
     * @param card Card to be edited
     * @return ID of the card that was edited
     */
    @MessageMapping("/edit-card/{boardID}")
    @SendTo("/topic/edit-card/{boardID}")
    public Card editCard(Card card) {
        return cardController.saveCardOverwrite(card);
    }

    /**
     * Websocket endpoint for editing a board
     *
     * @param board the Board to be edited
     * @return Board that was edited
     */
    @MessageMapping("/edit-board")
    @SendTo("/topic/edit-board")
    public Board editBoard(Board board) {
        return boardController.saveBoardOverwrite(board);
    }

    /**
     * Endpoint that updates a Card's ListEntity
     *
     * @param listID  ID of ListEntity affected
     * @param moved   moved card
     * @param boardID ID of board on which the card is
     * @return list entity
     */
    @MessageMapping("/adjust-card/{boardID},{listID}")
    @SendTo("/topic/adjust-card/{boardID}")
    public ListEntity editCardsList(
            Card moved,
            @DestinationVariable long boardID,
            @DestinationVariable long listID
    ) {
        return cardController.editCardsList(moved, listID);
    }

    /**
     * Squashes cards in the list from which
     * a card was moved from;
     * Solves the issue where moving the first card
     * (or any non-last card) to another list temporarily
     * left empty space
     *
     * @param listID  ID of list affected
     * @param boardID ID of board on which that list is
     * @return updated ListENtity
     */
    @MessageMapping("/squash-cards/{listID},{boardID}")
    @SendTo("/topic/squash-cards/{boardID}")
    public ListEntity squashCardsInList(
            @DestinationVariable long listID,
            @DestinationVariable long boardID
    ) {
        System.out.println("Update executed");
        return cardController.squashRanks(listID);
    }

    /**
     * Squashes tasks in the card
     * where a task was moved or deleted
     *
     * @param cardID ID of card affected
     * @return updated Card
     */
    @MessageMapping("/squash-tasks/{cardID}")
    @SendTo("/topic/squash-tasks/{cardID}")
    public Card squashTasksInCard(
            @DestinationVariable long cardID
    ) {
        return taskController.squashTaskRanks(cardID);
    }

    /**
     * @param username username of user to be added
     * @return User that was added
     */
    @GetMapping("/get-joined-boards/{username}")
    @ResponseBody
    public List<Board> getJoinedBoards(@PathVariable String username) {
        return userController.getJoinedBoardsByUsername(username);
    }

    /**
     * @return all users in the database
     */
    @GetMapping("/get-all-users")
    @ResponseBody
    public List<User> getAllUsers() {
        return userController.getAllUsers();
    }


    /**
     * @param boardID ID of board on which the tags reside
     * @return List of tags for that board
     */
    @GetMapping("/get-all-tags/{boardID}")
    @ResponseBody
    public List<Tag> getTagsForBoard(@PathVariable long boardID) {
        List<Tag> t = tagController.getTagsForBoard(boardID);
        System.out.println(t);
        return t;
    }

    /**
     * Helper endpoint for getting the Nth list in a board
     * (where N is the number of the list in board,
     * counting from the left)
     *
     * @param boardId ID of board on which the lists reside
     * @param n       specified n param, see above
     * @return ListEntity on that position
     */
    @GetMapping("lists/{boardId}/{n}")
    @ResponseBody
    public ListEntity getNthListFromBoard(@PathVariable long boardId, @PathVariable long n) {
        return listController.getNthListFromBoard(boardId, n);
    }

    /**
     * Responds to 'GET' request for ./data/board
     * Fetches data for all boards
     *
     * @return JSON list of all boards in repository
     */
    @GetMapping("/get-all-boards")
    @ResponseBody
    public List<Board> getAllBoards() {
        return boardController.getAllBoards();
    }

    /**
     * Provides a JSON response that contains a list of all ListEntities
     *
     * @return ListEntity of ListEntities
     */
    @GetMapping("/lists")
    @ResponseBody
    public List<ListEntity> getAllLists() {
        return this.listController.getAllLists();
    }

    /**
     * Endpoint for GET requests at ./cards
     *
     * @return A List of all Cards from the database
     */
    @GetMapping("/cards")
    @ResponseBody
    public List<Card> getAllCards() {
        return cardController.getAllCards();
    }

    /**
     * Endpoint for GET requests at ./tasks
     *
     * @return A List of all Tasks from the database
     */
    @GetMapping("/tasks")
    @ResponseBody
    public List<Task> getAllTasks() {
        return taskController.getAllTasks();
    }

    /**
     * Return all lists belonging to a certain board
     *
     * @param boardId id of the board
     * @return all the lists belonging
     */
    @GetMapping("/boards/{boardId}/lists")
    @ResponseBody
    public List<ListEntity> getListsByBoardId(@PathVariable long boardId) {
        return listController.getListsByBoardId(boardId);
    }

    /**
     * Return all cards belonging to a certain list
     *
     * @param listId id of the list
     * @return all the cards belonging
     */
    @GetMapping("/lists/{listId}/cards")
    @ResponseBody
    public List<Card> getCardsByListId(@PathVariable long listId) {
        return cardController.getCardsByListId(listId);
    }

    /**
     * Return all tasks belonging to a certain card
     *
     * @param cardId id of the card
     * @return all the tasks belonging
     */
    @GetMapping("/cards/{cardId}/tasks")
    @ResponseBody
    public List<Task> getTasksByCardId(@PathVariable long cardId) {
        return taskController.getTasksByCardId(cardId);
    }

    /**
     * Request for deleting all boards from the server
     *
     * @param boards The list of boards from server
     * @return the new list of boards after deletion (empty array)
     */
    @MessageMapping("/remove-boards")
    @SendTo("/topic/remove-boards")
    public List<Board> deleteAllBoards(List<Board> boards) {
        boardController.deleteAllBoards(boards);
        return boardController.getAllBoards();
    }

    /**
     * Responds to 'GET' request for ./boards/id
     * Fetches the board with specified id
     *
     * @param id id of the board
     * @return Board searched for
     */
    @GetMapping("boards/{id}")
    @ResponseBody
    public Board getBoardByID(@PathVariable long id) {
        return boardController.getBoardByID(id);
    }

    /**
     * Endpoint for GET requests at ./lists/id
     *
     * @param id id of the list
     * @return ListEntity searched for by id
     */
    @GetMapping("lists/{id}")
    @ResponseBody
    public ListEntity getListByID(@PathVariable long id) {
        return listController.getListByID(id);
    }

    /**
     * Endpoint for GET requests at ./cards/id
     *
     * @param id id of the card
     * @return The Card searched for by id
     */
    @GetMapping("cards/{id}")
    @ResponseBody
    public Card getCardByID(@PathVariable long id) {
        return cardController.getCardByID(id);
    }

    /**
     * Endpoint for GET requests at ./tasks/id
     *
     * @param id id of the task
     * @return The Task searched for by id
     */
    @GetMapping("tasks/{id}")
    @ResponseBody
    public Task getTasksByID(@PathVariable long id) {
        return taskController.getTasksByID(id);
    }

    /**
     * Fetches the number of tasks on a card
     *
     * @param id ID of card on which the tasks reside
     * @return Integer : number of tasks
     */
    @GetMapping("cards/{id}/tasks/numAll")
    @ResponseBody
    public Integer getNumberOfAllTasks(@PathVariable long id) {
        Card card = cardController.getCardByID(id);
        if (card == null) return -1;

        return taskController.getNumberOfTasks(card);
    }

    /**
     * Fetches the number of _complete_ tasks on a card
     *
     * @param id ID of card on which the tasks reside
     * @return Integer : number of _complete_ tasks
     */
    @GetMapping("cards/{id}/tasks/numComplete")
    @ResponseBody
    public Integer getNumberOfCompletedTasks(@PathVariable long id) {
        Card card = cardController.getCardByID(id);
        if (card == null) return -1;

        return taskController.getNumberOfCompletedTasks(card);
    }

    /**
     * Endpoint that generates and returns the server password
     *
     * @return the server password
     */
    @GetMapping("password")
    @ResponseBody
    public String getServerPassword() {
        if (serverPassword == null) {
            serverPassword = randomPassword.generateRandomPassword();
            System.out.println("Admin username: admin, password:" + serverPassword);
        }
        return serverPassword;
    }
}
