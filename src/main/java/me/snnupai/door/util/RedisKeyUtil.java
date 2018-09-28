package me.snnupai.door.util;


import me.snnupai.door.async.EventType;

public class RedisKeyUtil {
    private static String SPLIT = ":";
//    private static String BIZ_LIKE = "LIKE";
//    private static String BIZ_DISLIKE = "DISLIKE";
    private static String BIZ_EVENTQUEUE = "EVENT_QUEUE";
//
//    private static String BIZ_FANS = "FANS";
//    private static String BIZ_FOLLOW_ENTITY = "FOLLOW_ENTITY";
//    private static String BIZ_TIMELINE = "TIMELINE";
//    private static String COLLECTION = "COLLECTION";
//
//    public static String getFansKey(int entityType, String loveId) {
//        return BIZ_FANS + SPLIT + entityType + SPLIT + loveId;
//    }
//
//
//    public static String getFollowEntityKey(int entityType, String id){
//        return BIZ_FOLLOW_ENTITY + SPLIT + entityType + SPLIT + id;
//    }
//
//    public static String getLikeKey(int entityType, Long entityId) {
//        return BIZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
//    }
//
//    public static String getDisLikeKey(int entityType, Long entityId) {
//        return BIZ_DISLIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
//    }
//
    public static String getEventQueueKey() {
        return BIZ_EVENTQUEUE;
    }
//
//
//    public static String getTimelineKey(int userId) {
//        return BIZ_TIMELINE + SPLIT + String.valueOf(userId);
//    }
//
//    public static String getCollectionKey(int entityType, Long userId) {
//        return COLLECTION + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(userId);
//    }

    public static String getKey(int eventType, int entityType, String entityId) {
        return  eventType + SPLIT + entityType + SPLIT + entityId;
    }

    public static String getVal(int secondEntityType, String secondEntityId) {
        return secondEntityType + SPLIT + secondEntityId;
    }
}
