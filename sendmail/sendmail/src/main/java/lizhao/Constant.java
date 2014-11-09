package lizhao;

public interface Constant {
    
    byte USER_STATUS_NO_LOGIN = 0;
    
    byte USER_STATUS_TICKETIMG = 1;
    
    String MESSAGE_PATTER = "\"message\":\"[^\"]+\"";
    
    String ORDER_SUCCESS_REGEX = ".*\"orderDBList\".*";
    
    String ORDER_FAILE_REGEX = ".*\"orderCacheDTO\".*";
    
    String ORDER_TRAIN_DATE_PATTERN = "\"start_train_date_page\":\"[^\"]+\"";

    String ORDER_TRAIN_TO = "\"to_station_name\":\"[^\"]+\"";
    
    String ORDER_TRAIN_FROM = "\"from_station_name\":\"[^\"]+\"";
}
