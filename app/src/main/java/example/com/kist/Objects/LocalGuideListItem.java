package example.com.kist.Objects;

/**
 * Created by pr0 on 5/8/17.
 */

public class LocalGuideListItem {
    int resourceId;
    String name, blurb, type, des1, des2, des3, longitude, latitude, address, phone, website,
            taurl, fbLink, instaLink, openingHrs, gettingThere, closest, email;           //type can only be "Eats", "Drinks" or "Attractions"

    public LocalGuideListItem(int resourceId, String name, String blurb, String type) {
        this.resourceId = resourceId;
        this.name = name;
        this.blurb = blurb;
        this.type = type;
    }

    public String getClosest() {
        return closest;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setClosest(String closest) {
        this.closest = closest;
    }

    public LocalGuideListItem() {

    }

    public int getResourceId() {
        return resourceId;
    }

    public String getBlurb() {
        return blurb;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setBlurb(String blurb) {
        this.blurb = blurb;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public String getDes1() {
        return des1;
    }

    public String getDes2() {
        return des2;
    }

    public String getDes3() {
        return des3;
    }

    public String getFbLink() {
        return fbLink;
    }

    public String getGettingThere() {
        return gettingThere;
    }

    public String getInstaLink() {
        return instaLink;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getOpeningHrs() {
        return openingHrs;
    }

    public String getPhone() {
        return phone;
    }

    public String getTaurl() {
        return taurl;
    }

    public String getWebsite() {
        return website;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDes1(String des1) {
        this.des1 = des1;
    }

    public void setDes2(String des2) {
        this.des2 = des2;
    }

    public void setDes3(String des3) {
        this.des3 = des3;
    }

    public void setFbLink(String fbLink) {
        this.fbLink = fbLink;
    }

    public void setGettingThere(String gettingThere) {
        this.gettingThere = gettingThere;
    }

    public void setInstaLink(String instaLink) {
        this.instaLink = instaLink;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setOpeningHrs(String openingHrs) {
        this.openingHrs = openingHrs;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setTaurl(String taurl) {
        this.taurl = taurl;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
