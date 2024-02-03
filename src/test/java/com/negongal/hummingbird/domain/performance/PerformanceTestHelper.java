package com.negongal.hummingbird.domain.performance;

import com.negongal.hummingbird.domain.artist.domain.Artist;
import com.negongal.hummingbird.domain.chat.domain.ChatRoom;
import com.negongal.hummingbird.domain.performance.domain.Performance;
import com.negongal.hummingbird.domain.performance.domain.PerformanceDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.test.util.ReflectionTestUtils;

public class PerformanceTestHelper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static Performance createPerformance(Long id, String artistName) {
        return createOne(id, artistName, new String[] {"2024-02-27 20:30", "2024-02-28 20:30"});
    }

    public static List<Performance> createList() {
        List<Performance> performanceList = new ArrayList<>();
        performanceList.add(createOne(1L, "Harry Styles",
                new String[] {"2024-02-05 20:30", "2024-02-06 20:30"}));
        performanceList.add(createOne(2L, "Jeff Bernat",
                new String[] {"2024-01-01 20:30", "2024-01-02 20:30"}));
        performanceList.add(createOne(3L, "Slowdive",
                new String[] {"2024-03-27 20:30", "2024-03-28 20:30"}));
        performanceList.add(createOne(4L, "Novo Amor",
                new String[] {"2024-02-14 20:30", "2024-02-15 20:30"}));
        performanceList.add(createOne(5L, "Sam Smith",
                new String[] {"2024-02-27 20:30", "2024-02-28 20:30"}));
        return performanceList;
    }

    public static Performance createOne(Long id, String artistName, String[] strDates) {
        Artist artist = Artist.builder().name(artistName).performanceList(new ArrayList<>()).build();
        Performance performance = Performance.builder().name(artist + " 콘서트").artist(artist).build();
        ReflectionTestUtils.setField(performance, "id", id);

        ChatRoom chatRoom = new ChatRoom("room" + id, performance);
        ReflectionTestUtils.setField(performance, "chatRoom", chatRoom);
        ReflectionTestUtils.setField(performance, "dateList", createDate(performance, strDates));

        return performance;
    }

    public static List<PerformanceDate> createDate(Performance p, String[] strDates) {
        List<PerformanceDate> dateList = new ArrayList<>();
        for(String strDate : strDates) {
            dateList.add(new PerformanceDate(p, LocalDateTime.parse(strDate, formatter)));
        }
        return dateList;
    }

}
