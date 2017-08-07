package example.com.kist.Objects;

/**
 * Created by pr0 on 7/8/17.
 */

public class NextItem {
    int resourceId;
    String name, blurb, description, getting1, gettng2, getting3;

    public NextItem() {

    }

    public int getResourceId() {
        return resourceId;
    }

    public String getName() {
        return name;
    }

    public String getBlurb() {
        return blurb;
    }

    public String getDescription() {
        return description;
    }

    public String getGetting1() {
        return getting1;
    }

    public String getGetting3() {
        return getting3;
    }

    public String getGettng2() {
        return gettng2;
    }

    public void setGetting1(String getting1) {
        this.getting1 = getting1;
    }

    public void setGetting3(String getting3) {
        this.getting3 = getting3;
    }

    public void setGettng2(String gettng2) {
        this.gettng2 = gettng2;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBlurb(String blurb) {
        this.blurb = blurb;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
