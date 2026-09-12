package testWFC;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Deque;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WFCPatternExtractorTest {

    WFCPatternExtractor wfcPatternExtractor = new WFCPatternExtractor();


    @Test
    void extract() {
        //GIVEN
//        PixelWFC pixelWFC = new PixelWFC();
//        Deque<String>[][] mapDeque = pixelWFC.generateAll(0, 11);
        String[][] map = new String[5][5];
        String mapString = readTest();
        String[] split = mapString.replaceAll(" ", "").split(",");
        int counter = 0;
        for (int i = 0; i < map.length; i++) {
            for (int i1 = 0; i1 < map[0].length; i1++) {
                map[i][i1] = split[counter];
                counter++;
            }
        }

//String[][] map = new String[mapDeque.length][mapDeque[0].length];
//        for (int i = 0; i < mapDeque.length; i++) {
//            for (int i1 = 0; i1 < mapDeque[0].length; i1++) {
//                map[i][i1] = mapDeque[i][i1].peekFirst();
//            }
//        }

        //WHEN
        Rules rules = wfcPatternExtractor.extract(map);


        //THEN
        List<Rules.TileRule> tileRules = rules.getTileRules();
        for (Rules.TileRule tileRule : tileRules) {
            Map<Integer, Rules.Connector> connectorMap = tileRule.connectorMap();

            System.out.printf("     %s \n%s    "+ tileRule.label() +"    %s\n     %s",
                    connectorMap.getOrDefault(1, new Rules.Connector("", null)).sockets(),
                    connectorMap.getOrDefault(0, new Rules.Connector("", null)).sockets(),
                    connectorMap.getOrDefault(2, new Rules.Connector("", null)).sockets(),
                    connectorMap.getOrDefault(3, new Rules.Connector("", null)).sockets());
            System.out.println();
            System.out.println();

        }
        saveTest(rules);
        System.out.println(rules.getTileRules());
    }

    private String readTest() {
        String result = "";
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get("C:\\Users\\Rafal\\Desktop\\serwer\\server\\src\\test\\java\\testWFC\\mapData.txt"));
            StringBuilder builder = new StringBuilder();
            for (String line : lines) {
                builder.append(line);
            }
            result =  builder.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private void saveTest(Rules rules) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT); // ładne formatowanie

        try {
            // Zapis do pliku output.txt
            mapper.writeValue(new File("C:\\Users\\Rafal\\Desktop\\serwer\\server\\src\\main\\resources\\map\\testSave.json"), rules);
            System.out.println("JSON został zapisany do output.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}