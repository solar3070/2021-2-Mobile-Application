package mobile.example.network.test.myxmlparser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;


public class KobisXmlParser {

//    xml에서 읽어들일 태그를 구분한 enum  → 정수값 등으로 구분하지 않고 가독성 높은 방식을 사용
    private enum TagType { NONE, RANK, MOVIE_NM, OPEN_DT, MOVIE_CD };     // 해당없음, rank, movieNm, openDt, movieCd

    //    parsing 대상인 tag를 상수로 선언
    private final static String FAULT_RESULT = "faultResult";
    private final static String ITEM_TAG = "dailyBoxOffice";
    private final static String RANK_TAG = "rank";
    private final static String MOVIE_NAME_TAG = "movieNm";
    private final static String OPEN_DATE_TAG = "openDt";
    private final static String MOVIE_CODE = "movieCd";

    private XmlPullParser parser;

    public KobisXmlParser() {
//        xml 파서 관련 변수들은 필요에 따라 멤버변수로 선언 후 생성자에서 초기화
//        파서 준비
        XmlPullParserFactory factory = null;

//        파서 생성
        try {
            factory = XmlPullParserFactory.newInstance();
            parser = factory.newPullParser();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

    }


    public List<DailyBoxOffice> parse(String xml) {
        List<DailyBoxOffice> resultList = new ArrayList();
        DailyBoxOffice dbo = null;
        TagType tagType = TagType.NONE;     //  태그를 구분하기 위한 enum 변수 초기화

        try {
            // 파싱 대상 지정
            parser.setInput(new StringReader(xml));
            // 태그 유형 구분 변수 준비
            int eventType = parser.getEventType();
            // parsing 수행 - for 문 또는 while 문으로 구성
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        String tag = parser.getName();
                        if (tag.equals(ITEM_TAG)) {
                            dbo = new DailyBoxOffice();
                        } else if (tag.equals(RANK_TAG)) {
                            tagType = TagType.RANK;
                        } else if (tag.equals(MOVIE_NAME_TAG)) {
                            tagType = TagType.MOVIE_NM;
                        } else if (tag.equals(OPEN_DATE_TAG)) {
                            tagType = TagType.OPEN_DT;
                        } else if (tag.equals(MOVIE_CODE)) {
                            tagType = TagType.MOVIE_CD;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals(ITEM_TAG)) {
                            resultList.add(dbo);
                        }
                        break;
                    case XmlPullParser.TEXT:
                        switch (tagType) {
                            case RANK:
                                dbo.setRank(parser.getText());
                                break;
                            case MOVIE_NM:
                                dbo.setMovieNm(parser.getText());
                                break;
                            case OPEN_DT:
                                dbo.setOpenDt(parser.getText());
                                break;
                            case MOVIE_CD:
                                dbo.setMovieCD(parser.getText());
                                break;
                        }
                        tagType = TagType.NONE;
                        break;
                }
                eventType = parser.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }
}
