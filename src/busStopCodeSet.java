public class busStopCodeSet {

  String busStopCodeID;
  String code;
  String road;
  String description;
  String layout_num;
  String max_pages;
  String summary;
  String createDate;

  public busStopCodeSet(final String busStopCodeID, final String code, final String road, final String description,
      final String layout_num, final String max_pages, final String summary, final String createDate) {
    // TODO Auto-generated constructor stub
    this.busStopCodeID = busStopCodeID;
    this.code = code;
    this.description = description;
    this.layout_num = layout_num;
    this.max_pages = max_pages;
    this.summary = summary;
    this.createDate = createDate;
  }

  public String getBusStopCodeID() {
    // TODO Auto-generated method stub

    return busStopCodeID;
  }

}
