package ru.bichevoy;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Окончание игры
 * наложение кораблей на сетку
 */

@Data
public class GameService {
    @Setter(AccessLevel.NONE)
    private String[][] battleField;
    @Setter(AccessLevel.NONE)
    private String[][] mirrorBattleFieldOpponent;
    @Setter(AccessLevel.NONE)
    private List<Ship> shipList;
    private int sizeNextShip;
    @Setter(AccessLevel.NONE)
    private Server server;

    public GameService() {
        initGame();
    }

    public GameService(Server server) {
        this();
        this.server = server;
    }

    /**
     * Создание кораблей, игрового поля, поля для выстрелов текущего игрока
     */
    private void initGame() {
        battleField = createBattleField();
        shipList = new ArrayList<>();
        shipList.add(new Ship(4));
        shipList.add(new Ship(3));
        shipList.add(new Ship(3));
        shipList.add(new Ship(2));
        shipList.add(new Ship(2));
        shipList.add(new Ship(2));
        shipList.add(new Ship(1));
        shipList.add(new Ship(1));
        shipList.add(new Ship(1));
        shipList.add(new Ship(1));
        mirrorBattleFieldOpponent = createBattleField();
        sizeNextShip = 4;
    }

    /**
     * Инициализация игрового поля
     */
    private String[][] createBattleField() {
        String[][] field = new String[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                field[i][j] = ". ";
            }
        }
        return field;
    }

    /**
     * Метод проверки все ли корабли расставлены
     */
    public boolean canStart() {
        for (Ship ship : shipList) {
            if (ship.isInit()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Получить строковое представление игрового поля
     */
    public String getStringViewBattleField(String[][] field) {
        StringBuilder sb = new StringBuilder("   A Б В Г Д Е Ж З И К\n");
        for (int i = 1; i <= 10; i++) {
            if (i < 10) {
                sb.append(i).append("  ");
            } else {
                sb.append(i).append(" ");
            }
            for (int j = 0; j < 10; j++) {
                sb.append(field[i - 1][j]);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Получить строковое представление правил игры
     */
    public String getConditions() {
        return "\nВсе готово для начала игры\n" +
                "Вам необходимо разместить свой флот, состоящий из кораблей разной длины:\n" +
                "1 четырёхпалубный корабль,\n" +
                "2 трёхпалубных корабля,\n" +
                "3 двухпалубных корабля,\n" +
                "4 однопалубных корабля.\n" +
                "Размещение должно проходить в соответствии с правилами: корабли не могут касаться друг друга, ни боками, ни углами.\n";
    }

    /**
     * Установка палубы корабля на карту
     */
    public boolean placeShipsShipsOnMap(String text) {
        if (!validateCoordinates(text)) {
            return false;
        }
        List<String> coordinates = Arrays.stream(text.split(","))
                .map(x -> x.toUpperCase().trim()).sorted(String::compareTo).collect(Collectors.toList());

        for (String coordinate : coordinates) {
            int[] c = getCoordinate(coordinate);
            if (isNearShip(c[0], c[1])) {
                return false;
            }
        }
        for (Ship ship : shipList) {
            if (ship.getDecks().length == coordinates.size()) {
                if (ship.isInit()) {
                    ship.setDecks(coordinates.toArray(new String[0]));
                    break;
                }
            }
        }

        for (String coordinate : coordinates) {
            int[] c = getCoordinate(coordinate);
            battleField[c[0]][c[1]] = "0 ";
        }
        return true;
    }

    private int[] getCoordinate(String coordinate) {
        String header = "АБВГДЕЖЗИК";
        char c = coordinate.charAt(0);
        int column = header.indexOf(String.valueOf(c));
        String numberPart = coordinate.replaceAll("[^0-9]", "");
        int line = Integer.parseInt(numberPart);
        return new int[]{line - 1, column};
    }

    /**
     * Проверка области карты на возможность установки корабля
     */
    private boolean isNearShip(int x, int y) {
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i >= 0 && i < 10 && j >= 0 && j < 10 && battleField[i][j].equals("0 ")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Валидация корректности введенных координат
     */
    public boolean validateCoordinates(String text) {
        List<String> coordinates = Arrays.stream(text.split(","))
                .map(x -> x.toUpperCase().trim())
                .collect(Collectors.toList());

        for (String coordinate : coordinates) {
            if (coordinate == null || !coordinate.matches("[АБВГДЕЖЗИК][1-9]|[АБВГДЕЖЗИК]10")) {
                return false;
            }
        }
        String lastCoordinate = null;
        for (String coordinate : coordinates) {
            if (lastCoordinate == null) {
                lastCoordinate = coordinate;
                continue;
            }
            if (!(lastCoordinate.charAt(0) == coordinate.charAt(0)
                    || lastCoordinate.charAt(1) == coordinate.charAt(1))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Метод устанавливает sizeNextShip и возвращает сообщение о том какой корабль ожидается для установки
     */
    public Optional<String> getNextShip() {
        Optional<String> result = Optional.empty();
        for (Ship ship : shipList) {
            if (ship.isInit()) {
                sizeNextShip = ship.getDecks().length;
                return ship.getDecks().length > 2 ? Optional.of(String.format("Укажите координаты %d-палубного корабля, через запятую:", ship.getDecks().length))
                        : Optional.of(String.format("Укажите координаты %d-палубного корабля:", ship.getDecks().length));
            }
        }
        return result;
    }

    /**
     * Выстрел
     */
    public void shot(String coordinates, GameRoom gameRoom, ClientHandler player) {
        if (gameRoom.getMover() != player) {
            player.sendMessage("Ход делает ваш оппонент");
            return;
        }
        coordinates = coordinates.toUpperCase().trim();
        if (!validateCoordinates(coordinates)) {
            player.sendMessage("Введены не верные координаты");
            return;
        }
        if (coordinates.length() > 3) {
            player.sendMessage("Просьба указать одну координату, например А1 или Г5");
            return;
        }

        ClientHandler opponent = gameRoom.getOpponent(player);
        Ship foundedShip = getShipOpponent(opponent, coordinates);
        int[] c = getCoordinate(coordinates);
        drawShotInFields(c, player, opponent, gameRoom, foundedShip);
    }


    private Ship getShipOpponent(ClientHandler opponent, String coordinates) {
        Ship foundedShip = null;
        for (Ship ship : opponent.getGameService().getShipList()) {
            for (int i = 0; i < ship.getDecks().length; i++) {
                if (ship.getDecks()[i].equals(coordinates)) {
                    ship.getDecks()[i] = "*";
                    foundedShip = ship;
                }
            }
        }

        if (foundedShip != null) {
            for (String deck : foundedShip.getDecks()) {
                if (deck.equals("*")) {
                    foundedShip.setAlive(false);
                } else {
                    foundedShip.setAlive(true);
                    break;
                }
            }
        }
        return foundedShip;
    }

    private void drawShotInFields(int[] c, ClientHandler player, ClientHandler opponent, GameRoom gameRoom, Ship foundedShip) {
        String[][] opponentBattleField = opponent.getGameService().getBattleField();
        if (opponentBattleField[c[0]][c[1]].equals("0 ")) {
            opponentBattleField[c[0]][c[1]] = "* ";
            mirrorBattleFieldOpponent[c[0]][c[1]] = "* ";
            player.sendMessage(getStringViewBattleField(mirrorBattleFieldOpponent));
            opponent.sendMessage(getStringViewBattleField(opponentBattleField));
            String answer = (foundedShip != null && foundedShip.isAlive()) ? String.format("%s попал, стреляет еще раз", player.getLogin()) : String.format("%s Потопил, стреляет еще раз", player.getLogin());
            server.broadcastMessage(gameRoom, answer);
            if (checkWin(opponent)) {
                server.broadcastMessage(gameRoom, String.format("\n******Игра окончена %s потопил все корабли******", player.getLogin()));
                initGame();
                opponent.getGameService().initGame();
                if (!gameRoom.haveFreeSpace()) {
                    server.broadcastMessage(gameRoom, getConditions());
                    server.broadcastMessage(gameRoom, getStringViewBattleField(getBattleField()));
                    server.broadcastMessage(gameRoom, String.format("Укажите координаты %d-палубного корабля, через запятую (пример: А1,А2,А3,А4)", getSizeNextShip()));
                }
            }
        } else {
            opponentBattleField[c[0]][c[1]] = "X ";
            mirrorBattleFieldOpponent[c[0]][c[1]] = "X ";
            player.sendMessage(getStringViewBattleField(mirrorBattleFieldOpponent));
            opponent.sendMessage(getStringViewBattleField(opponent.getGameService().getBattleField()));
            server.broadcastMessage(gameRoom, String.format("%s промазал, стреляет игрок %s", player.getLogin(), opponent.getLogin()));
            opponent.sendMessage((opponent.getGameService().getStringViewBattleField(opponent.getGameService().getBattleField())) + "----------------------");
            opponent.sendMessage((opponent.getGameService().getStringViewBattleField(opponent.getGameService().getMirrorBattleFieldOpponent())) + "\nУкажите координаты для выстрела");
            gameRoom.changeMover();
        }
    }

    private boolean checkWin(ClientHandler opponent) {
        List<Ship> opponentShipList = opponent.getGameService().getShipList();
        for (Ship ship : opponentShipList) {
            if (ship.isAlive()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Поставить корабль
     */
    public void placeShipStep(String msg, GameService gameService, ClientHandler clientHandler, GameRoom gameRoom) {
        if (msg.split(",").length != sizeNextShip) {
            clientHandler.sendMessage(String.format("Необходимо указать %d ед. координат", sizeNextShip));
            return;
        }
        if (!gameService.placeShipsShipsOnMap(msg)) {
            clientHandler.sendMessage("Введены не верные координаты");
            return;
        }
        clientHandler.sendMessage("Корабль установлен\n" + gameService.getStringViewBattleField(gameService.getBattleField()));
        Optional<String> nextShip = gameService.getNextShip();
        if (nextShip.isPresent()) {
            clientHandler.sendMessage(nextShip.get());
        } else {
            server.broadcastMessage(gameRoom, String.format("%s расставил все корабли и готов начать игру", clientHandler.getLogin()));
            if (gameRoom.getMover() == null) {
                gameRoom.setMover(clientHandler);
                server.broadcastMessage(gameRoom, String.format("%s получает право первого хода т.к расставил корабли первым", gameRoom.getMover().getLogin()));
                clientHandler.sendMessage("Ожидайте когда оппонент расставит корабли");
            } else {
                clientHandler.sendMessage(clientHandler.getGameService().getStringViewBattleField(clientHandler.getGameService().getBattleField()));
                ClientHandler opponent = gameRoom.getOpponent(clientHandler);
                opponent.sendMessage(opponent.getGameService().getStringViewBattleField(opponent.getGameService().getBattleField()));
                clientHandler.sendMessage("Ожидайте хода соперника");
                opponent.sendMessage("----------------------");
                opponent.sendMessage(opponent.getGameService().getStringViewBattleField(opponent.getGameService().getMirrorBattleFieldOpponent()));
                opponent.sendMessage("Укажите координаты для выстрела");
            }
        }
    }
}


