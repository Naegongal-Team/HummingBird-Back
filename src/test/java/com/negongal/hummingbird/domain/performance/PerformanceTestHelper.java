package com.negongal.hummingbird.domain.performance;

import com.negongal.hummingbird.domain.artist.domain.Artist;
import com.negongal.hummingbird.domain.chat.domain.ChatRoom;
import com.negongal.hummingbird.domain.performance.domain.Performance;
import com.negongal.hummingbird.domain.performance.domain.PerformanceDate;
import com.negongal.hummingbird.domain.performance.domain.PerformanceHeart;
import com.negongal.hummingbird.domain.performance.domain.TicketType;
import com.negongal.hummingbird.domain.performance.domain.Ticketing;
import com.negongal.hummingbird.domain.user.domain.User;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.test.util.ReflectionTestUtils;

public class PerformanceTestHelper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static PerformanceHeart createPerformanceHeart(Long id, User user, Performance performance) {
        PerformanceHeart performanceHeart = PerformanceHeart.builder().user(user).performance(performance).build();
        ReflectionTestUtils.setField(performanceHeart, "id", id);
        return performanceHeart;
    }
    public static Performance createPerformance(Long id, String artistName) {
        return createOne(id, artistName, new String[] {"2024-02-27 20:30", "2024-02-28 20:30"});
    }

    public static Performance createOne(Long id, String artistName, String[] strDates) {
        Artist artist = Artist.builder().name(artistName).build();
        Performance performance = Performance.builder().name(artist + " 콘서트").artist(artist).build();
        ReflectionTestUtils.setField(performance, "id", id);

        ChatRoom chatRoom = new ChatRoom("room" + id, performance);
        ReflectionTestUtils.setField(performance, "chatRoom", chatRoom);
        ReflectionTestUtils.setField(performance, "performanceDates", createPerformanceDateList(performance, strDates));
        ReflectionTestUtils.setField(performance, "performanceHearts", new ArrayList<>());
        return performance;
    }

    public static List<PerformanceDate> createPerformanceDateList(Performance p, String[] strDates) {
        List<PerformanceDate> dateList = new ArrayList<>();
        for(String strDate : strDates) {
            dateList.add(new PerformanceDate(p, LocalDateTime.parse(strDate, formatter)));
        }
        return dateList;
    }

    public static List<Ticketing> createTicketingList(Performance p, String[] strDates) {
        List<Ticketing> ticketingList = new ArrayList<>();
        for(String strDate : strDates) {
            ticketingList.add(Ticketing.builder()
                    .performance(p)
                    .ticketType(TicketType.REGULAR)
                    .startDate(LocalDateTime.parse(strDate, formatter))
                    .platform("인터파트")
                    .link("https://tickets.interpark.com/")
                    .build());
        }
        return ticketingList;
    }

    public static String[] getArtistNames() {
        return new String[] {"Harry Styless", "Charlie Puths", "Slowdives", "Novo Amors", "Sam Smiths"};
    }

    public static String[][] getDates() {
        return new String[][] {
                {"2024-05-05 20:30", "2024-05-06 20:30"},
                {"2024-01-10 20:30", "2024-01-11 20:30"},
                {"2024-04-27 20:30", "2024-04-28 20:30"},
                {"2024-06-14 20:30", "2024-06-15 20:30"},
                {"2024-04-27 20:30", "2024-04-28 20:30"}
        };
    }

    public static String[][] getTicketingDates() {
        return new String[][]  {
                {"2024-04-04 20:30", "2024-04-05 20:30"},
                {"2024-01-01 20:30", "2024-01-02 20:30"},
                {"2024-03-12 20:30", "2024-03-13 20:30"},
                {"2024-05-14 20:30", "2024-05-15 20:30"},
                {"2024-04-18 20:30", "2024-04-19 20:30"}
        };
    }

}
