/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.utils;

import commons.*;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class ServerUtils {
    private static String serverAddress = "http://localhost:8080/";

    private static String websocketServer = "ws://localhost:8080/websocket";

    private static StompSession session;

    private static User user;

    private static final ExecutorService EXEC = Executors.newSingleThreadExecutor();

    /**
     * @param addr the server address to set
     */
    public void setServer(String addr) {
        serverAddress = "http://" + addr;
        websocketServer = "ws://" + addr + "/websocket";
    }

    /**
     * Connects to the server
     */
    public void connectToWebsocket() {
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        try {
            session = stomp.connect(websocketServer, new StompSessionHandlerAdapter() {
            }).get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param dest    the destination to subscribe to
     * @param type    the type of the message
     * @param handler the handler to handle the message
     * @param <T>     the type of the message
     */
    public <T> void registerForMessages(String dest, Class<T> type, Consumer<T> handler) {
        session.subscribe(dest, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return type;
            }

            @SuppressWarnings("unchecked")
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                handler.accept((T) payload);
            }
        });
    }

    /**
     * Registers for cards updates
     *
     * @param consumer the consumer of cards
     */
    public void registerForUpdates(Consumer<Card> consumer) {

        EXEC.submit(() -> {
            while (!Thread.interrupted()) {
                var res = ClientBuilder.newClient()
                        .target(serverAddress + "api/cards/updates")
                        .request(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .get(Response.class);

                if (res.getStatus() == 204) {
                    continue;
                }
                var card = res.readEntity(Card.class);
                consumer.accept(card);
            }
        });
    }

    /**
     * Stops the long polling thread
     */
    public void stop() {
        EXEC.shutdown();
    }

    /**
     * @param dest    the destination to send to
     * @param payload the payload to send
     */
    public void sendToWebsocket(String dest, Object payload) {
        session.send(dest, payload);
    }

    /**
     * @param board the Board to be added
     * @return the Board that was added or the board with the same id if it already existed
     */
    public Board addBoard(Board board) {
        Entity<Board> requestBody = Entity.entity(board, APPLICATION_JSON);

        GenericType<Board> requestBodyType = new GenericType<Board>() {
        };

        return ClientBuilder.newClient()
                .target(serverAddress + "api/boards")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .post(requestBody, requestBodyType);
    }

    /**
     * @return the User that was created
     */
    public User getUser() {
        return user;
    }

    /**
     * @param username the username of the user to be created
     */
    public void createUser(String username) {
        user = new User(username, null);

        sendToWebsocket("/app/add-user", user);

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<User> userlist = getAllUsers();
        for (User u : userlist) {
            if (u.getUsername().equals(username)) {
                user = u;
                System.out.println("User created: " + user.getUsername() + " " + user);
                break;
            }
        }
    }

    private List<User> getAllUsers() {
        GenericType<List<User>> responseBodyType = new GenericType<List<User>>() {
        };

        return ClientBuilder.newClient(new ClientConfig())
                .target(serverAddress).path("api/get-all-users")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(responseBodyType);
    }

    /**
     * Sends a GET request to ./api/boards
     * Gets all the boards in the database
     *
     * @return a List of Boards
     */
    public List<Board> getAllBoards() {
        GenericType<List<Board>> responseBodyType = new GenericType<List<Board>>() {
        };

        return ClientBuilder.newClient(new ClientConfig())
                .target(serverAddress).path("api/get-all-boards")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(responseBodyType);
    }

    /**
     * Sends a new board to the server via websockets
     *
     * @param board the board to be added to the server
     */
    public void sendNewBoardToServer(Board board) {
        sendToWebsocket("/app/add-board", board);
    }

    /**
     * @param list    the List to be added
     * @param boardID the id of the board to add the list to
     */
    public void sendNewListToServer(ListEntity list, Long boardID) {
        sendToWebsocket("/app/add-list/" + boardID, list);
    }

    /**
     * @param card    the Card to be added
     * @param boardID the id of the board to add the card to
     */
    public void sendNewCardToServer(Card card, Long boardID) {
        sendToWebsocket("/app/add-card/" + boardID, card);
    }

    /**
     * Method to send an update request to the server
     *
     * @param list the list attributes to be updated
     */
    public void updateList(ListEntity list) {
        sendToWebsocket("/app/edit-list/" + list.board.id, list);
    }

    /**
     * Method to send an update request to the server
     *
     * @param card the card attributes to be updated
     * @param id   the id of the updated card
     */
    public void updateCard(Card card, long id) {
        sendToWebsocket("/app/edit-card/" + id, card);
    }

    /**
     * Sends a request to update a board
     *
     * @param board the board to be updated
     */
    public void updateBoard(Board board) {
        sendToWebsocket("/app/edit-board", board);
    }

    /**
     * Sends a request to the server to delete a specific card
     *
     * @param card the card being deleted
     */
    public void deleteCard(Card card) {
        sendToWebsocket("/app/remove-card/" + card.list.board.id, card);
    }

    /**
     * Sends a websocket request to delete a task
     *
     * @param task Task to be deleted
     */
    public void deleteTask(Task task) {
        sendToWebsocket("/app/remove-task/" + task.card.id, task);
    }

    /**
     * Sends a websocket request to change task's description
     *
     * @param task Task to be modified
     */
    public void editTask(Task task) {
        sendToWebsocket("/app/modify-task/" + task.card.id, task);
    }

    /**
     * Edits the card's rank on the database.
     *
     * @param cardId  desired card's ID
     * @param newRank rank to be moved to
     */
    public void updateRanks(long cardId, long newRank) {
        Card referenced = getCardByID(cardId);

        if (referenced == null) throw new RuntimeException("Error: Moved card not found!");

        Entity<Card> requestBody = Entity.entity(referenced, APPLICATION_JSON);

        GenericType<Card> requestBodyType = new GenericType<Card>() {
        };

        Card card = getCardByID(cardId);

        sendToWebsocket("/app/move-card/" + newRank + "," + card.list.board.id, card);
    }

    /**
     * Updates a rank of a given task
     * @param task Task that is being moved
     * @param newRank Rank to which the new task is moved
     */
    public void updateTaskRanks(Task task, long newRank) {
        sendToWebsocket("/app/move-task/" + newRank + "," + task.card.id, task);
    }

    /**
     * Server utility that handles user moving a card between boards
     *
     * @param cardId        ID of card being moved
     * @param newCardListID ID of list onto which the card was moved
     */
    public void updateCardsList(long cardId, long newCardListID) {
        Card referenced = getCardByID(cardId);

        if (referenced == null)
            throw new RuntimeException("Error: List to which the card was moved not found!");
        sendToWebsocket(
                "/app/adjust-card/"
                        + referenced.list.board.id + "," + newCardListID, referenced
        );
    }

    /**
     * Helper method that fetches Nth list in a board,
     * counting from left to right;
     * used during front-end calculations where it is needed
     * to determine what list a dragged card belongs to
     *
     * @param boardID ID of board on which the search for list is conducted
     * @param n       parameter specifying the position of the list
     * @return the ListEntity on that position
     */
    public ListEntity getNthListInBoard(long boardID, long n) {
        GenericType<ListEntity> responseBodyType = new GenericType<>() {
        };

        return ClientBuilder.newClient()
                .target(serverAddress + "api/lists/" + boardID + "/" + n)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(responseBodyType);
    }

    /**
     * Connects the client with the squash method for drag & drop
     *
     * @param listID ID of list where cards are squashed
     */
    public void squashRanks(long listID) {
        ListEntity list = getListByID(listID);

        sendToWebsocket("/app/squash-cards/" + listID + "," + list.board.id, null);
    }

    /**
     * Connects the client with the squash method for task moving
     * @param cardID ID of card where tasks are squashed
     */
    public void squashTaskRanks(long cardID) {
        Card card = getCardByID(cardID);
        if(card == null) return;
        sendToWebsocket("/app/squash-tasks/" + card.id, null);
    }

    /**
     * Sends a websocket request to add a new task to DB
     *
     * @param task   Task to be added
     * @param cardID ID of card on which it is being created
     */
    public void sendNewTaskToServer(Task task, long cardID) {
        sendToWebsocket("/app/add-task/" + cardID, task);
    }


    /**
     * Sends a request to server to get a card by ID
     *
     * @param cardID ID of desired card
     * @return Card entity from server
     */
    public Card getCardByID(long cardID) { // works
        GenericType<Card> responseBodyType = new GenericType<Card>() {
        };

        return ClientBuilder.newClient()
                .target(serverAddress + "api/cards/" + cardID)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(responseBodyType);
    }

    /**
     * A simple HTTP request for getting a list by its ID
     *
     * @param listID ID of requested list
     * @return ListEntity that with the requested ID
     */
    public ListEntity getListByID(long listID) {
        GenericType<ListEntity> responseBodyType = new GenericType<ListEntity>() {
        };

        return ClientBuilder.newClient()
                .target(serverAddress + "api/lists/" + listID)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(responseBodyType);
    }

    /**
     * HTTP request to fetch all tasks on a card
     *
     * @param cardID ID of card for which the tasks need to be fetched
     * @return List of tasks fetched
     */
    public List<Task> getTasksOnCard(long cardID) {
        GenericType<List<Task>> responseBodyType = new GenericType<List<Task>>() {
        };

        return ClientBuilder.newClient()
                .target(serverAddress + "api/cards/" + cardID + "/tasks")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(responseBodyType);

    }

    /**
     * Sends a request to the server to delete a specific list
     *
     * @param list the list to be deleted
     */
    public void deleteList(ListEntity list) {
        sendToWebsocket("/app/remove-list/" + list.board.id, list);
    }

    /**
     * Sends a request to the server to delete a specific board
     *
     * @param board the board to be deleted
     */
    public void deleteBoard(Board board) {
        sendToWebsocket("/app/remove-board", board);
    }

    /**
     * Gets all the list from a board
     *
     * @param board with the lists
     * @return a List of ListEntities
     */
    public List<ListEntity> getListsFromBoard(Board board) {
        GenericType<List<ListEntity>> responseBodyType = new GenericType<List<ListEntity>>() {
        };

        return ClientBuilder.newClient(new ClientConfig())
                .target(serverAddress).path("api/boards/" + board.id + "/lists")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(responseBodyType);
    }

    /**
     * Sends a GET request to ./api/lists/listId/cards
     * Gets all the cards from a specific list
     *
     * @param listId the id of the list
     * @return all cards from the list
     */
    public List<Card> getAllCardsFromList(long listId) {
        GenericType<List<Card>> responseBodyType = new GenericType<List<Card>>() {
        };

        return ClientBuilder.newClient()
                .target(serverAddress).path("api/lists/" + listId + "/cards")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(responseBodyType);
    }

    /**
     * @param board the board to get the cards from
     * @return a HashMap with the list id as key and a list of cards as value
     */
    public HashMap<Long, List<Card>> getCardHashMapFromBoard(Board board) {
        HashMap<Long, List<Card>> cardHashMap = new HashMap<>();
        List<ListEntity> lists = getListsFromBoard(board);
        for (ListEntity list : lists) {
            List<Card> cards = getAllCardsFromList(list.id);
            cardHashMap.put(list.id, Objects.requireNonNullElseGet(cards, ArrayList::new));
        }
        return cardHashMap;
    }

    /**
     * @return a list of all the boards the user is a member of
     */
    public List<Board> getJoinedBoards() {
        GenericType<List<Board>> responseBodyType = new GenericType<List<Board>>() {
        };

        List<Board> l = ClientBuilder.newClient()
                .target(serverAddress).path("api/get-joined-boards/" + user.username)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(responseBodyType);

        System.out.println("AAA Joined boards: " + l);
        return l;
    }

    /**
     * Fetches the number of all tasks on a given card
     *
     * @param cardID ID of card on which the tasks are
     * @return Integer : number of tasks on a card
     */
    public Integer getNumberOfTasksOnCard(long cardID) {
        GenericType<Integer> responseBodyType = new GenericType<Integer>() {
        };

        return ClientBuilder.newClient()
                .target(serverAddress).path("api/cards/" + cardID + "/tasks/numAll")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(responseBodyType);
    }

    /**
     * Fetches the number of _complete_ tasks on a given card
     *
     * @param cardID ID of card on which the tasks are
     * @return Integer : number of tasks on a board
     */
    public Integer getNumberOfTasksCompletedOnCard(long cardID) {
        GenericType<Integer> responseBodyType = new GenericType<Integer>() {
        };

        return ClientBuilder.newClient()
                .target(serverAddress).path("api/cards/" + cardID + "/tasks/numComplete")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(responseBodyType);
    }

    /**
     * Deletes all boards from server
     *
     * @param boards the list of all boards from server
     */
    public void deleteAllBoards(List<Board> boards) {
        sendToWebsocket("/app/remove-boards", boards);
    }

    /**
     * Request to get the server password from the server
     *
     * @return the server password
     */
    public String getServerPassword() {
        GenericType<String> responseBodyType = new GenericType<String>() {
        };
        return ClientBuilder.newClient()
                .target(serverAddress).path("api/password")
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(responseBodyType);
    }

    /**
     * @param board the board to get the tags from
     * @return a list of all the tags on the board
     */
    public List<Tag> getTags(Board board) {
        GenericType<List<Tag>> responseBodyType = new GenericType<List<Tag>>() {
        };

        return ClientBuilder.newClient()
                .target(serverAddress).path("api/get-all-tags/" + board.id)
                .request(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .get(responseBodyType);
    }
}
