package org.barneys.worldMap;


import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
public class WFCRule {
    private List<Rule> rules;
    private List<TileRule> tileRules;

    public WFCRule() {
    }

    public WFCRule(List<Rule> rules) {
        this.rules = rules;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public List<TileRule> getTileRules() {
        return tileRules;
    }

    public void setTileRules(List<TileRule> tileRules) {
        this.tileRules = tileRules;
    }

    interface RuleI {
        Set<String> sideAccepts(AcceptsSide side);
        Integer getChance();
    }

    record Rule(String label, Integer chance, Set<Integer> floors, Set<Accepts> accepts) implements RuleI {
        public Set<String> sideAccepts(AcceptsSide side) {
            return accepts.stream().filter(accept -> (accept.acceptsSide.contains(side) || accept.acceptsSide.contains(AcceptsSide.ALL))).map(Accepts::label).collect(Collectors.toSet());
        }

        @Override
        public Integer getChance() {
            return chance;
        }
    }

    record TileRule(String label, Integer chance, Set<Integer> floors, AcceptedNeighbors acceptedNeighbors) implements RuleI {
        public Set<String> sideAccepts(AcceptsSide side) {
            return new HashSet<>(acceptedNeighbors.acceptsSideListMap.get(side));
        }

        @Override
        public Integer getChance() {
            return chance;
        }
    }


    record AcceptedNeighbors(Map<AcceptsSide, List<String>> acceptsSideListMap) {

    }

    record Accepts(String label, Integer chance, List<AcceptsSide> acceptsSide) {}

    public enum AcceptsSide {
        LEFT, RIGHT, UP, DOWN, ALL
    }
}
