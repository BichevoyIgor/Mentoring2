package ru.bichevoy;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

@Data
@Slf4j
public class GameRoom {
    static AtomicInteger idGenerator = new AtomicInteger();

    private final int id;

    @Setter(AccessLevel.NONE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ClientHandler gamer1;

    @Setter(AccessLevel.NONE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ClientHandler gamer2;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ClientHandler mover;


    public void changeMover() {
        if (mover == gamer1) {
            mover = gamer2;
        } else {
            mover = gamer1;
        }
    }

    public GameRoom() {
        this.id = idGenerator.incrementAndGet();
    }

    public boolean haveFreeSpace() {
        return gamer1 == null || gamer2 == null;
    }

    public boolean isEmpty() {
        return gamer1 == null && gamer2 == null;
    }

    public void setGamer(ClientHandler clientHandler) {
        if (gamer1 == null) {
            gamer1 = clientHandler;
        } else if (gamer2 == null) {
            gamer2 = clientHandler;
        }
    }

    /**
     * Проверка наличия такого логина в комнате
     */
    public boolean loginIsPresent(String enteredLogin) {
        if (gamer1 != null
                && gamer1.getLogin() != null
                && gamer1.getLogin().equals(enteredLogin)) {
            return true;
        }
        return gamer2 != null
                && gamer2.getLogin() != null
                && gamer2.getLogin().equals(enteredLogin);
    }

    /**
     * Удаление игрока из комнаты
     */
    public void removeClient(ClientHandler client) {
        if (gamer1 == client) {
            gamer1 = null;
        }
        if (gamer2 == client) {
            gamer2 = null;
        }
    }

    /**
     * Возврат оппонента
     * @param player игрок
     * @return оппонент
     */
    public ClientHandler getOpponent(ClientHandler player) {
        return gamer1 == player ? gamer2 : gamer1;
    }
}

