package client.utils;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class MouseHandler implements EventHandler<MouseEvent> {

    private final EventHandler<MouseEvent> onDraggedEventHandler;


    private final EventHandler<MouseEvent> onDoubleClickedEventHandler;


    private boolean dragging = false;



    /**Constructor for the mouse event handles
     * @param onDraggedEventHandler the event handler for dragging
     * @param onDoubleClickedEventHandler the event for double-clicking
     */
    public MouseHandler(EventHandler<MouseEvent> onDraggedEventHandler,
                        EventHandler<MouseEvent> onDoubleClickedEventHandler) {
        this.onDraggedEventHandler = onDraggedEventHandler;
        this.onDoubleClickedEventHandler = onDoubleClickedEventHandler;
    }

    /**Handler
     * @param event the event which occurred
     */
    @Override
    public void handle(MouseEvent event) {
        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            //also open the card editor with right click for easier testing
            if(event.isSecondaryButtonDown()){
                onDoubleClickedEventHandler.handle(event);
            }
            if(event.getClickCount() == 2){
                onDoubleClickedEventHandler.handle(event);
            }
            dragging = false;
        }
        else if (event.getEventType() == MouseEvent.DRAG_DETECTED) {
            dragging = true;
        }
        else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            if(dragging) onDraggedEventHandler.handle(event);

        }
        else if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
            dragging = false;
            if(event.getClickCount() == 2){
                onDoubleClickedEventHandler.handle(event);
            }
        }
        else if (event.getEventType() == MouseEvent.MOUSE_RELEASED){
            dragging = false;
        }
    }
}
