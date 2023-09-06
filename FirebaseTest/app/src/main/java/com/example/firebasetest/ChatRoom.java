package com.example.firebasetest;
public class ChatRoom {
    private String roomId; // 채팅방의 고유 ID
    private String profileImageUrl; // 상대방 프로필 사진 URL
    private String nickname; // 상대방 닉네임
    private String itemImageUrl; // 판매하는 아이템 사진 URL
    private String lastMessage; // 마지막 메세지 내용
    private String myName;
    private long lastMessageTimestamp; // 마지막 메세지의 타임스탬프 (최근 메세지 정렬에 사용)

    String seller;
    String id;
    String endPrice;
    int intEndPrice;
    double doubleEndPrice;


    String bidType;
    String buyerUid;
    String documentId;
    String userUid;
    String userEmail;
    String futureMillis;
    String buyer;

    // 생성자, getter, setter, toString 등의 필요한 메소드를 추가합니다.

    // 예를 들어, 생성자는 다음과 같이 작성할 수 있습니다.
    public ChatRoom() {
        // 기본 생성자
    }

    public ChatRoom(String roomId, String bidType, String buyer, String buyerUid, String documentId, String endPrice, String id, String itemImageUrl, String lastMessage, String myName, String nickname, String profileImageUrl, String seller ) {
        this.roomId = roomId;
        this.bidType = bidType;
        this.buyer = buyer;
        this.seller = seller;
        this.documentId = documentId;
        this.endPrice = endPrice;
        this.id = id;
        this.lastMessageTimestamp = lastMessageTimestamp;
        this.myName = myName;
        this.nickname = nickname;
        this.itemImageUrl = itemImageUrl;
        this.lastMessage = lastMessage;
        this.profileImageUrl = profileImageUrl;
        this.buyerUid = buyerUid;


    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getItemImageUrl() {
        return itemImageUrl;
    }

    public void setItemImageUrl(String itemImageUrl) {
        this.itemImageUrl = itemImageUrl;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public long getLastMessageTimestamp() {
        return lastMessageTimestamp;
    }

    public void setLastMessageTimestamp(long lastMessageTimestamp) {
        this.lastMessageTimestamp = lastMessageTimestamp;
    }

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEndPrice() {
        return endPrice;
    }

    public void setEndPrice(String endPrice) {
        this.endPrice = endPrice;
    }

    public int getIntEndPrice() {
        return intEndPrice;
    }

    public void setIntEndPrice(int intEndPrice) {
        this.intEndPrice = intEndPrice;
    }

    public double getDoubleEndPrice() {
        return doubleEndPrice;
    }

    public void setDoubleEndPrice(double doubleEndPrice) {
        this.doubleEndPrice = doubleEndPrice;
    }

    public String getBidType() {
        return bidType;
    }

    public void setBidType(String bidType) {
        this.bidType = bidType;
    }

    public String getBuyerUid() {
        return buyerUid;
    }

    public void setBuyerUid(String buyerUid) {
        this.buyerUid = buyerUid;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getFutureMillis() {
        return futureMillis;
    }

    public void setFutureMillis(String futureMillis) {
        this.futureMillis = futureMillis;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    // toString 메소드를 추가하여 객체 정보를 문자열로 반환할 수 있도록 합니다.
}