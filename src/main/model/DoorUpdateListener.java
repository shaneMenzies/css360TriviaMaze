package model;

/**
 * Interface for performing an action when a DoorController updates.
 *
 * @author Shane Menzies
 * @version 10/27/24
 */
public interface DoorUpdateListener {

    /**
     * Performs some function after the linked DoorController's state has updated.
     *
     * @param theController DoorController which updated.
     */
    void doUpdate(DoorController theController);
}