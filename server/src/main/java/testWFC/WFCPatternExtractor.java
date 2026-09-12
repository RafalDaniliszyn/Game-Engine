package testWFC;

import java.util.*;

import static testWFC.Rules.*;

public class WFCPatternExtractor {

    public Rules extract(String[][] map) {
        Rules rules = new Rules();
        Map<String, TileRule> tileRuleMap = new HashMap<>();
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length; y++) {
                String label = map[x][y];
                HashSet<Integer> floorSet = new HashSet<>();
                floorSet.add(0);
                floorSet.add(1);
                Map<Integer, Connector> connectorMap = new HashMap<>();

                if (x > 0) {
                    Set<String> sockets = new HashSet<>();
                    sockets.add(map[x-1][y]);
                    connectorMap.put(0, new Connector(label, sockets));
                } else {
                    connectorMap.put(0, new Connector(label, new HashSet<>()));
                }

                if (y+1 < map[0].length) {
                    Set<String> sockets = new HashSet<>();
                    sockets.add(map[x][y+1]);
                    connectorMap.put(1, new Connector(label, sockets));
                } else {
                    connectorMap.put(1, new Connector(label, new HashSet<>()));
                }

                if (x+1 < map.length) {
                    Set<String> sockets = new HashSet<>();
                    sockets.add(map[x+1][y]);
                    connectorMap.put(2, new Connector(label, sockets));
                } else {
                    connectorMap.put(2, new Connector(label, new HashSet<>()));
                }

                if (y > 0) {
                    Set<String> sockets = new HashSet<>();
                    sockets.add(map[x][y-1]);
                    connectorMap.put(3, new Connector(label, sockets));
                } else {
                    connectorMap.put(3, new Connector(label, new HashSet<>()));
                }


//                    if (tileRuleMap.containsKey(label)) {
//                        Map<Integer, Connector> connectorMapToUpdate = tileRuleMap.get(label).connectorMap();
//                        for (Map.Entry<Integer, Connector> connectorEntrySet : connectorMapToUpdate.entrySet()) {
//                            Integer key = connectorEntrySet.getKey();
//                            Connector value = connectorEntrySet.getValue();
//                            if (connectorMap.containsKey(key)) {
//                                value.sockets().addAll(connectorMap.get(key).sockets());
//                            }
//                        }
//                    } else {
//                        TileRule tileRule = new TileRule(label, 0.333, floorSet, null, connectorMap);
//                        tileRuleMap.put(label, tileRule);
//                    }

                connectorMap.forEach((side, connector) -> {
                    if (connector.sockets().isEmpty()) {
                        connector.sockets().add(label);
                    }
                });
                TileRule tileRule = new TileRule(label, 0.333, floorSet, null, connectorMap);
                tileRuleMap.put(label, tileRule);
                rules.getTileRules().add(tileRule);
            }
        }

        //Set<TileRule> tileRules = new HashSet<>(tileRuleMap.values());
        //rules.setTileRules(tileRules.stream().toList());
        //rules.setTileRules(tileRuleMap.values().stream().toList());
        return rules;
    }
}
