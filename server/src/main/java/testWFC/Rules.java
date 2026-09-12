package testWFC;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.*;

import static testWFC.Rules.AcceptsSide.*;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
public class Rules {

    private List<TileRule> tileRules;

    public Rules() {
        tileRules = new ArrayList<>();
    }


    public List<TileRule> getTileRules() {
        return tileRules;
    }

    public void setTileRules(List<TileRule> tileRules) {
        this.tileRules = tileRules;
    }

    interface RuleI {
        Set<String> sideAccepts(AcceptsSide side, int rotation);
        double getChance();
    }



    record Connector(@JsonProperty("plug") String plug, @JsonProperty("sockets") Set<String> sockets) {
    }


    //TODO: acceptedNeighbors raczej do usunięcia
    record TileRule(String label, double chance, Set<Integer> floors,
                    AcceptedNeighbors acceptedNeighbors, @JsonProperty("connectorMap") Map<Integer, Connector> connectorMap) implements RuleI {

        /**
         * Checks if the specified plug matches the socket on the given side.
         *
         * @param plug  The identifier of the plug to be checked.
         * @param side  The side where the socket is located.
         * @return      {@code true} if the plug matches the socket on the specified side; {@code false} otherwise.
         */
        public boolean matches(String plug, AcceptsSide side) {
            int indexBySide = getIndexBySide(side);
            if (connectorMap.containsKey(indexBySide)) {
                return connectorMap.get(indexBySide).sockets().contains(plug);
            }
            return false;
        }

        public Set<String> sideAccepts(AcceptsSide side, int rotation) {
            if (rotation != 0) {
                return getRotatedNeighbors(side, rotation);
            }
            return new HashSet<>(acceptedNeighbors.acceptsSideListMap.get(side));
        }

        public double getChance() {
            return chance;
        }

        public Set<String> getRotatedNeighbors(AcceptsSide side, int rotation) {
            int indexSide = getIndexBySide(side);
            int resultIndex = indexSide - rotation < 0 ? 4 - rotation : indexSide - rotation;
            AcceptsSide resultSide = getSideByIndex(resultIndex);
            return new HashSet<>(acceptedNeighbors.acceptsSideListMap.get(resultSide));
        }

        public Connector getRotatedConnector(int indexSide, int rotation) {
            int resultIndex;
            if (indexSide != 0) {
                int tmp = indexSide == 2 ? 6 : 5;
                resultIndex = indexSide - rotation < 0 ? tmp - rotation : indexSide - rotation;
            } else {
                resultIndex = indexSide - rotation < 0 ? 4 - rotation : indexSide - rotation;
            }
            return connectorMap.get(resultIndex);
        }

        public static int getIndexBySide(AcceptsSide side) {
            return switch (side) {
                case LEFT -> 0;
                case UP -> 1;
                case RIGHT -> 2;
                case DOWN -> 3;
            };
        }

        public static AcceptsSide getSideByIndex(int index) {
            return switch (index) {
                case 0 -> LEFT;
                case 1 -> UP;
                case 2 -> RIGHT;
                case 3 -> DOWN;
                default -> null;
            };
        }

        public static AcceptsSide getReflection(int index) {
            return switch (index) {
                case 0 -> RIGHT;
                case 1 -> DOWN;
                case 2 -> LEFT;
                case 3 -> UP;
                default -> null;
            };
        }
    }




    record AcceptedNeighbors(Map<AcceptsSide, List<String>> acceptsSideListMap) {

    }

    record Accepts(String label, Integer chance, List<AcceptsSide> acceptsSide) {}

    public enum AcceptsSide {
        LEFT, RIGHT, UP, DOWN
    }
}
