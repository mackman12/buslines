package sbab.app.busline;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sbab.app.integration.IntegrationService;
import sbab.app.integration.ObjectType;
import sbab.app.integration.journeypattern.JourneyPattern;
import sbab.app.integration.journeypattern.JourneyPatternResponse;
import sbab.app.integration.line.Line;
import sbab.app.integration.line.LineResponse;
import sbab.app.integration.stoppoint.StopPoint;
import sbab.app.integration.stoppoint.StopPointResponse;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.groupingBy;

@RequiredArgsConstructor
@Slf4j
@Component
public class BusLineRunner implements CommandLineRunner {

    private final IntegrationService integrationService;

    @Override
    public void run(String... args) throws Exception {
        log.info("Getting top 10 bus lines based on number of bus stops..");
        final Comparator<Map.Entry<String, List<JourneyPattern>>> sortByValueCount = Comparator.comparing(s -> s.getValue().stream().count());

        var journeyPatternsGrouped = getJourneyPatternsGroupedByLine();

        var top10BusLines = journeyPatternsGrouped.entrySet()
            .stream()
            .sorted(sortByValueCount.reversed())
            .limit(10)
            .map(journeyPatterns -> buildBusLine(journeyPatterns))
            .collect(Collectors.toList());

        logTop10BusLines(top10BusLines);
        logBusStopsForTop1(top10BusLines);
    }

    private void logBusStopsForTop1 (List<BusLine> busLines) throws Exception {
        var top1BusStop = busLines.stream()
            .findFirst()
            .orElseThrow(() -> new Exception("No bus stop found"));

        log.info("Bus stops for the top 1 bus line ".concat(top1BusStop.getLineNumber()).concat(":"));
        log.info(top1BusStop.getBusStops().toString());
    }

    private void logTop10BusLines(List<BusLine> busLines){
        var lines = busLines.stream()
            .map(busLine -> busLine.getLineNumber())
            .toList();

        log.info("The top 10 bus lines with the most stops: ");
        IntStream.range(0, lines.size())
            .forEach(index -> log.info((index + 1) + ". Line-number: " + lines.get(index)));
    }

    private Map<String, List<JourneyPattern>> getJourneyPatternsGroupedByLine() {
        var lineResponse = (LineResponse) integrationService.request(ObjectType.LINE);
        var journeyPatternResponse = (JourneyPatternResponse) integrationService.request(ObjectType.JOURNEY_PATTERN);

        var lines = filterBusLines(lineResponse);

        var journeyPatterns = filterJourneyPatternByBusLines(journeyPatternResponse, lines);
        var journeyPatternsGrouped = journeyPatterns.stream()
            .collect(groupingBy(journeyPattern -> journeyPattern.getLineNumber()));

        return journeyPatternsGrouped;
    }

    private BusLine buildBusLine(Map.Entry<String, List<JourneyPattern>> journeyPatterns) {
        return BusLine.builder()
            .lineNumber(journeyPatterns.getKey())
            .busStops(buildBusStops(journeyPatterns))
            .build();
    }

    private List<String> buildBusStops(Map.Entry<String, List<JourneyPattern>> journeyPatterns) {
        var stopPointResponse = (StopPointResponse) integrationService.request(ObjectType.STOP_POINT);
        var stopPoints = stopPointResponse.getResponseData().getResult();

        return journeyPatterns.getValue()
            .stream()
            .map(journeyPattern -> getStopPointName(stopPoints, journeyPattern))
            .distinct()
            .collect(Collectors.toList());
    }

    private String getStopPointName(List<StopPoint> stopPoints, JourneyPattern journeyPattern) {
        return stopPoints.stream()
            .filter(stopPoint -> stopPoint.getStopPointNumber()
                .equalsIgnoreCase(journeyPattern.getJourneyPatternPointNumber()))
            .findFirst()
            .orElse(buildDefaultBusStop(journeyPattern))
            .getStopPointName();
    }

    private StopPoint buildDefaultBusStop(JourneyPattern journeyPattern) {
        return StopPoint.builder()
            .stopPointName(journeyPattern.getJourneyPatternPointNumber())
            .stopPointNumber(journeyPattern.getJourneyPatternPointNumber())
            .build();
    }

    private List<Line> filterBusLines(LineResponse lineResponse){
        var busLines = lineResponse.getResponseData()
            .getResult()
            .stream()
            .filter(line -> line.getDefaultTransportModeCode()
                .equalsIgnoreCase("BUS"))
            .collect(Collectors.toList());

        return busLines;
    }

    private List<JourneyPattern> filterJourneyPatternByBusLines(JourneyPatternResponse journeyPatternResponse, List<Line> busLines){
        var busJourneyPatterns = journeyPatternResponse.getResponseData()
            .getResult()
            .stream()
            .filter(journeyPattern -> busLinesContainsJourneyPattern(busLines, journeyPattern))
            .distinct()
            .collect(Collectors.toList());

        return busJourneyPatterns;
    }

    private boolean busLinesContainsJourneyPattern(List<Line> busLines, JourneyPattern journeyPattern) {
        return busLines.stream()
            .map(busLine -> busLine.getLineNumber())
            .collect(Collectors.toList())
            .contains(journeyPattern.getLineNumber());
    }

}